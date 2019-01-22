(defproject graphql-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.8.1"
  :dependencies [[camel-snake-kebab "0.4.0"]
                 [clojure.java-time "0.3.2"]
                 [com.walmartlabs/lacinia-pedestal "0.11.0"]
                 [duct.module.pedestal "2.0.1"]
                 [duct/core "0.7.0"]
                 [duct/module.logging "0.4.0"]
                 [duct/module.sql "0.5.0"]
                 [honeysql "0.9.4"]
                 [io.pedestal/pedestal.jetty "0.5.5"]
                 [io.pedestal/pedestal.service "0.5.5"]
                 [mysql/mysql-connector-java "8.0.14"]
                 [org.clojure/clojure "1.10.0"]]
  :plugins [[duct/lein-duct "0.11.2"]]
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
                  :dependencies   [[clj-http "3.9.1"]
                                   [com.bhauman/rebel-readline "0.1.4"]
                                   [com.gearswithingears/shrubbery "0.4.1"]
                                   [eftest "0.5.4"]
                                   [integrant/repl "0.3.1" :exclusions [integrant]]
                                   [orchestra "2018.12.06-2"]
                                   [pjstadig/humane-test-output "0.9.0"]
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
