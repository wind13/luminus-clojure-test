(ns luminus-clojure-test.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [luminus-clojure-test.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[luminus-clojure-test started successfully using the development profile]=-"))
   :middleware wrap-dev})
