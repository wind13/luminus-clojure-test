(ns luminus-clojure-test.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[luminus-clojure-test started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[luminus-clojure-test has shutdown successfully]=-"))
   :middleware identity})
