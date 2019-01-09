(ns dev
  (:refer-clojure :exclude [test])
  (:require [graphql-example.graphql :as graphql]
            [clojure.java.io :as io]
            [clojure.repl :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.walk :as walk]
            [com.walmartlabs.lacinia :as lacinia]
            [duct.core :as duct]
            [duct.core.repl :as duct-repl]
            [eftest.runner :as eftest]
            [fipp.edn :refer [pprint]]
            [integrant.core :as ig]
            [integrant.repl :refer [clear halt go init prep]]
            [integrant.repl.state :refer [config system]]
            [orchestra.spec.test :as stest])
  (:import (clojure.lang IPersistentMap)))

(duct/load-hierarchy)

(defn read-config []
  (duct/read-config (io/resource "graphql_example/config.edn")))

(defn reset []
  (let [result (integrant.repl/reset)]
    (with-out-str (stest/instrument))
    result))

;;; unit testing

(defn test
  ([]
   (eftest/run-tests (eftest/find-tests "test")
                     {:multithread? false}))
  ([sym]
   (eftest/run-tests (eftest/find-tests sym)
                     {:multithread? false})))

;;; GraphQL

(defn q [query-string]
  (-> system
      :graphql-example.graphql/schema
      (lacinia/execute query-string nil nil)))

(defn simplify [m]
  (walk/postwalk
   (fn [node]
     (cond
       (instance? IPersistentMap node) (into {} node)
       (seq? node) (vec node)
       :else node))
   m))

;;; namespace settings

(clojure.tools.namespace.repl/set-refresh-dirs "dev/src" "src" "test")

(when (io/resource "local.clj")
  (load "local"))

(def profiles
  [:duct.profile/dev :duct.profile/local])

(integrant.repl/set-prep! #(duct/prep-config (read-config) profiles))
