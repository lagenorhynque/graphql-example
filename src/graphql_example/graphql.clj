(ns graphql-example.graphql
  (:require [com.walmartlabs.lacinia.pedestal :as lacinia]
            [com.walmartlabs.lacinia.schema :as schema]
            [integrant.core :as ig]))

(def hello-schema
  (schema/compile
   {:queries {:hello
              ;; String is quoted here; in EDN the quotation is not required
              {:type 'String
               :resolve (constantly "world")}}}))

(defmethod ig/init-key ::service
  [_ _]
  (lacinia/service-map hello-schema {:graphiql true}))
