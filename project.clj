(defproject luminus-clojure-test "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [selmer "1.0.4"]
                 [markdown-clj "0.9.87"]
                 [ring-middleware-format "0.7.0"]
                 [metosin/ring-http-response "0.6.5"]
                 [bouncer "1.0.0"]
                 [org.webjars/bootstrap "4.0.0-alpha.2"]
                 [org.webjars/font-awesome "4.5.0"]
                 [org.webjars.bower/tether "1.1.1"]
                 [org.webjars/jquery "2.2.2"]
                 [org.clojure/tools.logging "0.3.1"]
                 [compojure "1.5.0"]
                 [ring-webjars "0.1.1"]
                 [ring/ring-defaults "0.2.0"]
                 [mount "0.1.10"]
                 [cprop "0.1.7"]
                 [org.clojure/tools.cli "0.3.3"]
                 [luminus-nrepl "0.1.4"]
                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                 [luminus-immutant "0.1.9"]
                 [metosin/compojure-api "1.0.2"]
                 [luminus-log4j "0.1.3"]]

  :min-lein-version "2.0.0"

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj"]
  :resource-paths ["resources" "target/cljsbuild"]

  ;; start -- clojure script support.
  :cljsbuild {:builds {:app {:source-paths ["src-cljs"]
                             :figwheel true
                             :compiler {:output-to     "target/cljsbuild/public/js/app.js"
                                        :output-dir    "target/cljsbuild/public/js/out"
                                        :source-map    true
                                        :externs       ["react/externs/react.js"]
                                        :optimizations :none
                                        :main "<app>.core"
                                        :asset-path "/js/out"
                                        :pretty-print  true}}}}
  ;; end -- clojure script support.

  :main luminus-clojure-test.core

  :plugins [[lein-cprop "1.0.1"] [lein-cljsbuild "1.1.1"]]
  :target-path "target/%s/"
  :profiles
  {:uberjar {:omit-source true
             
             :aot :all
             :uberjar-name "luminus-clojure-test.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}
   ;; start --clojure script support
   :hooks ['leiningen.cljsbuild]
   :cljsbuild {:jar true
               :builds {:app
                        {:compiler
                         {:optimizations :advanced
                          :pretty-print false}}}}
   ;; end -- clojure script support.
   :dev           [:project/dev :profiles/dev]
   :test          [:project/test :profiles/test]
   :project/dev  {:dependencies [[prone "1.1.0"]
                                 [ring/ring-mock "0.3.0"]
                                 [ring/ring-devel "1.4.0"]
                                 [pjstadig/humane-test-output "0.8.0"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.14.0"]]
                  
                  
                  :source-paths ["env/dev/clj" "test/clj"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:resource-paths ["env/dev/resources" "env/test/resources"]}
   :profiles/dev {:env {:dev        true
                        :port       3000
                        :nrepl-port 7000
                        :log-level  :trace}}
   :profiles/test {}})
