(ns babelfish-clojure-driver.core-test
  (:require [clojure.test :refer :all]
            [msgpack.core :as msg]
            [babelfish-clojure-driver.core :refer :all]))

(defn- err? [result status]
  (do (is (= (:status result) status))
      (is (nil? (:ast result)))
      (is (= (count (:errors result)) 1))))

(defn- ok? [result]
  (do (is (= (:status result) :ok))
      (is (not (nil? (:ast result))))
      (is (= (count (:errors result)) 0))))

(defn- status? [m status]
  (is (= status (get m "status"))))

(defn- stream [req]
  (let [encoded (msg/pack req)]
    (java.io.DataInputStream.
     (java.io.ByteArrayInputStream. encoded))))

(deftest test-parse
  (testing "with valid source"
    (let [result (parse "(defn foo [a b] (+ a b))")]
      (ok? result)))

  (testing "with invalid source"
    (let [result (parse "(println (+ a 1)")]
      (err? result :error))))

(deftest test-process-req
  (testing "with non msgpack input"
    (let [result (process-req (stream "fooooo"))
          output (msg/unpack result)]
      (status? output "fatal")))

  (testing "with invalid action"
    (let [req (stream {:action "fooobar"
                       :content "foslkdfjslkfj"})
          result (process-req req)
          output (msg/unpack result)]
      (status? output "fatal")))

  (testing "with valid input"
    (let [req (stream {:action "ParseAST"
                       :content "(defn foo [a b]
                                      (+ a b))"})
          result (process-req req)
          output (msg/unpack result)]
      (status? output "ok"))))

(deftest test-clean-ast
  (testing "cleaning ast"
    (let [ast [{:env true
                :raw-forms true
                :other {:env true
                        :raw-forms true
                        :var true
                        :other true}
                :var true}
               {[(symbol "quote") (symbol "foo")] {:env true
                                                   :raw-forms true
                                                   :var true
                                                   :other true}}]
          clean (clean-ast ast)]
      (is (= [{:other {:other true}}
              {"'foo" {:other true}}]
             clean)))))
