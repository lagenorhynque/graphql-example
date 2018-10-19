(defproject graphql-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[camel-snake-kebab "0.4.0"]
                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [clojure.java-time "0.3.2"]
                 [com.walmartlabs/lacinia "0.30.0"]
                 [com.walmartlabs/lacinia-pedestal "0.10.0" :exclusions [org.clojure/tools.reader]]
                 [duct/core "0.6.2"]
                 [duct/database.sql.hikaricp "0.3.3" :exclusions [integrant]]
                 [duct/module.logging "0.3.1"]
                 [duct/module.sql "0.4.2"]
                 [honeysql "0.9.4"]
                 [mysql/mysql-connector-java "8.0.12"]
                 [org.clojure/clojure "1.9.0"]
                 [org.slf4j/jcl-over-slf4j "1.7.25"]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]]
  :plugins [[duct/lein-duct "0.10.6"]]
  :main ^:skip-aot graphql-example.main
  :resource-paths ["resources" "target/resources"]
  :prep-tasks     ["javac" "compile" ["run" ":duct/compiler"]]
  :profiles
  {:dev  [:project/dev :profiles/dev]
   :repl {:prep-tasks   ^:replace ["javac" "compile"]
          :repl-options {:init-ns user}}
   :uberjar {:aot :all}
   :profiles/dev {}
   :project/dev  {:source-paths   ["dev/src"]
                  :resource-paths ["dev/resources"]
                  :dependencies   [[com.bhauman/rebel-readline "0.1.4"]
                                   [eftest "0.5.3"]
                                   [integrant/repl "0.3.1" :exclusions [integrant]]
                                   [kerodon "0.9.0"]]
                  :aliases {"rebel" ^{:doc "Run REPL with rebel-readline."}
                            ["trampoline" "run" "-m" "rebel-readline.main"]}}})