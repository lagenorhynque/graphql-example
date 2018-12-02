(ns graphql-example.boundary.db.example-db
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [integrant.core :as ig]))

(defmethod ig/init-key ::db
  [_ _]
  (-> (io/resource "cgg-data.edn")
      slurp
      edn/read-string
      atom))

(defmethod ig/halt-key! ::db
  [_ _]
  nil)
