(defproject graphql-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.8.1"
  :dependencies [[camel-snake-kebab "0.4.1"]
                 [clojure.java-time "0.3.2"]
                 [com.walmartlabs/lacinia-pedestal "0.12.0"]
                 [duct.module.cambium "1.1.0" :exclusions [org.slf4j/slf4j-api]]
                 [duct.module.pedestal "2.1.0"]
                 [duct/core "0.8.0"]
                 [duct/module.sql "0.6.0"]
                 [honeysql "0.9.8"]
                 [io.pedestal/pedestal.jetty "0.5.7"]
                 [io.pedestal/pedestal.service "0.5.7"]
                 [mysql/mysql-connector-java "8.0.18"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/java.jdbc "0.7.10"]]
  :plugins [[duct/lein-duct "0.12.1"]]
  :middleware [lein-duct.plugin/middleware]
  :main ^:skip-aot graphql-example.main
  :resource-paths ["resources" "target/resources"]
  :prep-tasks     ["javac" "compile" ["run" ":duct/compiler"]]
  :profiles
  {:repl {:prep-tasks   ^:replace ["javac" "compile"]
          :repl-options {:init-ns user}}
   :dev  [:shared :project/dev :profiles/dev]
   :test [:shared :project/dev :project/test :profiles/test]
   :uberjar [:shared :project/uberjar]

   :shared {}
   :project/dev  {:source-paths   ["dev/src"]
                  :resource-paths ["dev/resources"]
                  :dependencies   [[clj-http "3.10.0"]
                                   [com.bhauman/rebel-readline "0.1.4"]
                                   [com.gearswithingears/shrubbery "0.4.1"]
                                   [eftest "0.5.9"]
                                   [fipp "0.6.22"]
                                   [hawk "0.2.11"]
                                   [integrant/repl "0.3.1"]
                                   [orchestra "2019.02.06-1"]
                                   [pjstadig/humane-test-output "0.10.0"]
                                   [vincit/venia "0.2.5"]]
                  :aliases {"rebel" ^{:doc "Run REPL with rebel-readline."}
                            ["trampoline" "run" "-m" "rebel-readline.main"]}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {}
   :project/uberjar {:aot :all
                     :uberjar-name "graphql-example.jar"}

   :profiles/dev {}
   :profiles/test {}})
