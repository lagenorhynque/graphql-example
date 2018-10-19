(ns graphql-example.server
  (:require [com.walmartlabs.lacinia.pedestal :as lacinia]
            [com.walmartlabs.lacinia.schema :as schema]
            [integrant.core :as ig]
            [io.pedestal.http :as http]))

(def hello-schema
  (schema/compile
   {:queries {:hello
              ;; String is quoted here; in EDN the quotation is not required
              {:type 'String
               :resolve (constantly "world")}}}))

(defmethod ig/init-key ::service
  [_ service]
  (-> (lacinia/service-map hello-schema {:graphiql true})
      (merge service)
      (assoc
       ;; all origins are allowed in dev mode
       ::http/allowed-origins {:creds true :allowed-origins (constantly true)})))

(defmethod ig/init-key ::server
  [_ {:keys [service dev?]}]
  (println (str "\nCreating your " (when dev? "[DEV] ") "server..."))
  (-> service
      http/create-server
      http/start))

(defmethod ig/halt-key! ::server
  [_ server]
  (http/stop server))
