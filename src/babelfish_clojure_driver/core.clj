(ns babelfish-clojure-driver.core
  (:refer-clojure :exclude [macroexpand-1])
  (:require [clojure.tools.analyzer :as ana]
            [clojure.tools.analyzer.jvm :as ana.jvm]
            [clojure.tools.analyzer.ast :refer [postwalk]]
            [clojure.tools.analyzer.env :refer [with-env]]
            [clojure.tools.analyzer.passes.elide-meta :refer [elide-meta]]
            [clojure.tools.analyzer.passes.source-info :refer [source-info]]
            [clojure.data.json :as json]
            [babelfish-clojure-driver.parse :refer [parse-recur]])
  (:import java.lang.System
           java.io.InputStreamReader
           java.io.BufferedReader)
  (:gen-class))

(def empty-env {:context :ctx/statement
                :locals {}
                :ns 'user})

;; clojure.core needs to have no mappings!
(def env (atom {:namespaces {'user {:mappings {}
                                    :aliases {}
                                    :ns 'user}
                             'clojure.core {:mappings {}
                                            :aliases {}
                                            :ns 'clojure.core}}}))

(defn- parse-forms
  "Extension to tools.analyzer/-parse for JVM special forms and recur without expansion"
  [form env]
  ((case (first form)
     monitor-enter        ana.jvm/parse-monitor-enter
     monitor-exit         ana.jvm/parse-monitor-exit
     clojure.core/import* ana.jvm/parse-import*
     reify*               ana.jvm/parse-reify*
     deftype*             ana.jvm/parse-deftype*
     case*                ana.jvm/parse-case*
     do                   ana/parse-do
     if                   ana/parse-if
     new                  ana/parse-new
     quote                ana/parse-quote
     set!                 ana/parse-set!
     try                  ana/parse-try
     throw                ana/parse-throw
     def                  ana/parse-def
     .                    ana/parse-dot
     let*                 ana/parse-let*
     letfn*               ana/parse-letfn*
     loop*                ana/parse-loop*
     recur                parse-recur
     fn*                  ana/parse-fn*
     var                  ana/parse-var
     #_:else              ana/parse-invoke)
   form env))

(defn- ast
  "Returns the AST of a single Clojure form without the macros expanded"
  [form]
  (binding [ana/macroexpand-1 (fn [form env] form)
            ana/create-var   (fn [sym env]
                               (doto (intern (:ns env) sym)
                                 (reset-meta! (meta sym))))
            ana/parse        parse-forms
            ana/var?         var?]
    (with-env env
      (-> (ana/analyze form empty-env)
          (postwalk source-info)
          (postwalk elide-meta)))))

(defn- with-err
  "Returns an errored output"
  [status msg]
  {:ast nil
   :status status
   :errors [{:level :error
             :message msg}]})

(defn- clean-ast-entry [clean-ast entry]
  (let [k (first entry)
        v (second entry)]
    (cond
      (= k :env) nil
      (= k :raw-forms) nil
      (= k :var) nil

      (coll? k) (if (= (str (first k)) "quote")
                  (vector (str "'" (str (last k)))
                          (clean-ast v))
                  (vector k
                          (clean-ast v)))

      :else (vector k (clean-ast v)))))

(defn clean-ast
  "Removes the :env map from the AST nodes and converts quoted symbols to
  string preceded by ' because, even if in Clojure is ok to have collections
  as keys, in most languages is not. For example, the tree containing a map
  with something quoted would not be serializable to JSON."
  [m]
  (cond
    (map? m) (into {}
                   (filter (comp not nil?)
                           (map (partial clean-ast-entry clean-ast) m)))

    (coll? m) (map clean-ast m)

    :else m))

(defn- parse-ast
  "Parses all the forms in the given Clojure source code and returns a list
  with the AST of all the top level forms"
  [src]
  (with-open [r (java.io.PushbackReader. (java.io.StringReader. src))]
    (binding [*read-eval* false]
      (loop [expr (read r false :end)
             forms []]
        (if (= :end expr)
          forms
          (recur (read r false :end)
                 (concat forms [(ast expr)])))))))

(defn parse
  "Parses the given Clojure source code and returns the status, ast and errors, 
  if any"
  [src]
  (try
    {:ast (map clean-ast (parse-ast src))
     :status :ok
     :errors []}
    (catch Exception e (with-err :error (.getMessage e)))))

(defn- unpack
  "Unpacks the request from JSON or returns an error if it was not possible." 
  [stream]
  (try
    (let [line (.readLine stream)]
      (if (nil? line)
        :end
        (json/read-str line 
                       :key-fn keyword 
                       :eof-error? false 
                       :eof-value :end)))
    (catch Exception e (with-err :fatal (.getMessage e)))))

(defn- pack
  "Tries to pack the result into JSON. If it can't do so, it will return
  an error"
  [output]
  (try
    (json/write-str output)
    (catch Exception e (json/write-str (with-err :error (.getMessage e))))))

(defn process-req
  "Processes an incoming json-encoded parse request"
  [stream]
  (let [req (unpack stream)]
    (pack (cond
          (= req :end) (System/exit 0)

          ;; if req has status it means the unpack failed and it was converted
          ;; to a result
          (= (:status req) :fatal) req

          :else (parse (:content req))))))

(defn -main
  [& args]
  (loop [r (BufferedReader. (InputStreamReader. System/in))]
    (do
      (-> r 
          process-req 
          println)
      (recur r))))
