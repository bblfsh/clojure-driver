(ns babelfish-clojure-driver.parse
  (:require [clojure.tools.analyzer :as ana]
            [clojure.tools.analyzer.utils :refer [-source-info ctx]]))

(defn parse-recur
  "Parse recur is the same parse function as the one in clojure.tools.analyzer
  but it does not validate it."
  [[_ & exprs :as form] env]
  (let [exprs (mapv (ana/analyze-in-env (ctx env :ctx/expr)) exprs)]
    {:op          :recur
     :env         env
     :form        form
     :exprs       exprs
     :children    [:exprs]}))
