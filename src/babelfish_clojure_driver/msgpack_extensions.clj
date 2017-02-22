(ns babelfish-clojure-driver.msgpack-extensions
  (:import java.io.DataOutput)
  (:require [msgpack.core :as msg]))

(defn- keyword->str
  "Transforms a keyword to a string preserving its namespace"
  [k]
  (subs (str k) 1))

;; We need to treat keyword, symbol and character as strings, since other
;; parts of the pipeline of babelfish will not have the knowledge of what is a
;; symbol or a keyword.
;; So, instead of using the clojure extensions already provided by the
;; msgpack library, we extend the Packable protocol for our purposes.
(extend-protocol msg/Packable
  clojure.lang.Keyword
  (msg/packable-pack [kw ^DataOutput s opts]
    (msg/packable-pack (keyword->str kw) s opts))

  clojure.lang.Symbol
  (msg/packable-pack [sym ^DataOutput s opts]
    (msg/packable-pack (str sym) s opts))

  java.lang.Class
  (msg/packable-pack [cls ^DataOutput s opts]
    (msg/packable-pack (.getName cls) s opts))

  clojure.lang.Var
  (msg/packable-pack [v ^DataOutput s opts]
    (msg/packable-pack (meta v) s opts))

  clojure.lang.Namespace
  (msg/packable-pack [nms ^DataOutput s opts]
    (msg/packable-pack (str nms) s opts))

  clojure.lang.Atom
  (msg/packable-pack [a ^DataOutput s opts]
    (msg/packable-pack (binding [*read-eval* false] (read-string (str (.deref a)))) s opts))

  java.util.regex.Pattern
  (msg/packable-pack [rgx ^DataOutput s opts]
    (msg/packable-pack (str rgx) s opts))

  clojure.lang.IPersistentSet
  (msg/packable-pack [st ^DataOutput s opts]
    (msg/packable-pack (seq st) s opts))

  java.lang.Character
  (msg/packable-pack [ch ^DataOutput s opts]
    (msg/packable-pack (str ch) s opts)))
