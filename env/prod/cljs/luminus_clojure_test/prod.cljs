(ns luminus-clojure-test.app
  (:require [luminus-clojure-test.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
