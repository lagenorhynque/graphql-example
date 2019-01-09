(ns graphql-example.test-helper.core
  (:require [cheshire.core :as cheshire]
            [clj-http.client :as client]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [duct.core :as duct]
            [graphql-example.util.core :as util]
            [integrant.core :as ig]
            [orchestra.spec.test :as stest]))

(duct/load-hierarchy)

;;; fixtures

(defn instrument-specs [f]
  (stest/instrument)
  (f))

;;; macros for testing context

(defn test-system []
  (-> (io/resource "graphql_example/config.edn")
      duct/read-config
      (duct/prep-config [:duct.profile/dev :duct.profile/test])))

(s/fdef with-system
  :args (s/cat :binding (s/coll-of any?
                                   :kind vector
                                   :count 2)
               :body (s/* any?)))

(defmacro with-system [[bound-var binding-expr] & body]
  `(let [~bound-var (ig/init ~binding-expr)]
     (try
       ~@body
       (finally (ig/halt! ~bound-var)))))

;;; HTTP client

(def ^:private url-prefix "http://localhost:")

(defn- server-port [system]
  (get-in system [:duct.server/pedestal :io.pedestal.http/port]))

(defn http-get [system url & {:as options}]
  (client/get (str url-prefix (server-port system) url)
              (merge {:accept :json
                      :throw-exceptions? false} options)))

(defn http-post [system url body & {:as options}]
  (client/post (str url-prefix (server-port system) url)
               (merge {:body body
                       :content-type :application/graphql
                       :accept :json
                       :throw-exceptions? false} options)))

;;; JSON conversion

(defn ->json [obj]
  (-> obj
      cheshire/generate-string
      util/transform-keys-to-snake))

(defn <-json [str]
  (cheshire/parse-string str true))
