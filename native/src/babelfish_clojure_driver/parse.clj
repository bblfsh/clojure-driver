(ns babelfish-clojure-driver.parse
  (:require [clojure.tools.analyzer :refer [analyze-in-env]]
            [clojure.string :refer [trim]]
            [clojure.tools.analyzer.utils :refer [ctx]])
  (:import java.io.IOException
           java.io.StringReader
           java.io.BufferedReader))

(defn parse-recur
  "Parse recur is the same parse function as the one in clojure.tools.analyzer
  but it does not validate it."
  [[_ & exprs :as form] env]
  (let [exprs (mapv (analyze-in-env (ctx env :ctx/expr)) exprs)]
    {:op          :recur
     :env         env
     :form        form
     :exprs       exprs
     :meta        (meta form)
     :children    [:exprs]}))

(defn comment-node
  "Creates a new comment node with the given text, line and columns."
  [text line col end-col]
  {:op       :comment
   :env      {}
   :form     nil
   :exprs    [(trim text)]
   :meta     {:line line
              :col col
              :end-line line
              :end-col end-col}
   :children [:exprs]})

(defn parse-comments
  "Parses the comment form as a special form instead of an invoke one."
  [[_ & exprs :as form] env]
  (let [exprs (mapv (analyze-in-env (ctx env :ctx/expr)) exprs)]
    {:op       :comment
     :env      env
     :form     form
     :exprs    exprs
     :meta     (meta form)
     :children [:exprs]}))

(defn extract-comments
  "Extracts the comments with `;` from the source code. `comment` form is not
  extracted, just like `#_` reader macro.
  Returns a list of comments as comment nodes."
  [src]
  (with-open [r (-> src (StringReader.) (BufferedReader.))]
    (loop [in-comment? false
           escaped?    false
           in-str?     false
           text        ""
           line        1
           start-pos   0
           pos         0
           nodes       []]
      (let [ch (try 
                 (let [c (.read r)]
                   (if (>= c 0) (char c) :end))
                 (catch IOException e :end))
            pos (inc pos)]
        (cond
          (= ch :end)
          (if in-comment?
            (concat nodes [(comment-node text line start-pos pos)])
            nodes)

          (and (= ch \;) (not escaped?) (not in-comment?) (not in-str?))
          (recur true false false "" line pos pos nodes)

          (and (= ch \;) in-comment? (= (count text) 0))
          (recur true false false "" line start-pos pos nodes)

          (and (= ch \") (not in-str?) (not escaped?) (not in-comment?))
          (recur false false true "" line start-pos pos nodes)

          (and (= ch \") in-str? (not escaped?))
          (recur false false false "" line start-pos pos nodes)

          (and (= ch \\) (not in-comment?))
          (recur false true in-str? "" line start-pos pos nodes)

          (and (= ch \newline) in-comment?)
          (recur false false false ""
                 (inc line) 0 0
                 (concat nodes [(comment-node text line start-pos pos)]))

          (= ch \newline)
          (recur false false in-str? "" (inc line) 0 0 nodes)

          in-comment?
          (recur true false false (str text ch) line start-pos pos nodes)

          :else
          (recur false false in-str? "" line start-pos pos nodes))))))
