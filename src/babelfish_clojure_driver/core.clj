(ns babelfish-clojure-driver.core
  (:refer-clojure :exclude [macroexpand-1])
  (:require [clojure.tools.analyzer :as ana]
            [clojure.tools.analyzer.jvm :as ana.jvm]
            [clojure.tools.analyzer.ast :refer [postwalk]]
            [clojure.tools.analyzer.env :refer [with-env]]
            [clojure.tools.analyzer.passes.elide-meta :refer [elides elide-meta]]
            [msgpack.core :as msg]
            [babelfish-clojure-driver.msgpack-extensions]
            [babelfish-clojure-driver.parse :refer [parse-recur]])
  (:import java.lang.System
           java.io.DataInputStream)
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
          (postwalk elide-meta)))))

(defn- with-err
  "Returns an errored output"
  [status msg]
  {:ast nil
   :status status
   :errors [{:level :error
             :message msg}]})

(defn clean-ast
  "Removes the :env map from the AST nodes and converts quoted symbols to
  string preceded by ' because, even if in Clojure is ok to have collections
  as keys, in most languages is not. For example, the tree containing a map
  with something quoted would not be serializable to JSON."
  [m]
  (cond
    (map? m) (into {}
                   (filter (comp not nil?)
                           (map (fn [entry]
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
                                m)))

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

(defn- label
  "Adds the language, language version and driver to the output map"
  [output]
  (-> output
      (assoc :language "clojure")
      (assoc :language_version "1.0.0")
      (assoc :driver "clojure:1.0.0")))

(defn- unpack
  "Unpacks the request from msgpack or returns nil if it was not possible. 
  It returns nil instead of an error because the exceptions thrown by the
  msgpack library are not descriptive enough to understand what is going on.
  Instead, just know that if nil is returned, the request was not correct."
  [stream]
  (try
    (msg/unpack-stream stream)
    (catch Exception e nil)))

(defn- pack
  "Tries to pack the result into msgpack. If it can't do so, it will return a
  fatal error"
  [output]
  (try
    (msg/pack output)
    (catch Exception e (msg/pack (with-err :fatal (.getMessage e))))))

(defn process-req
  "Processes an incoming msgpack-encoded parse request"
  [stream]
  (let [req (unpack stream)]
    (-> (cond
          (nil? req)
          (with-err :fatal "unable to decode request from msgpack")

          (= "ParseAST" (get req "action"))
          (parse (get req "content"))

          :else
          (with-err :fatal (str "unknown action: " (get req "action"))))
        (label)
        (pack))))

(defn- write
  "Writes the output to the standard output"
  [output]
  (.write System/out output))

(defn -main
  [& args]
  (-> (DataInputStream. System/in)
      (process-req)
      (write)))
