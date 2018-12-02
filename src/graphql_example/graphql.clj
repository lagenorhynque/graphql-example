(ns graphql-example.graphql
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [com.walmartlabs.lacinia.pedestal :as lacinia]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [graphql-example.resolver.clojure-game-geek :as clojure-game-geek]
            [integrant.core :as ig]))

(defn resolver-map
  [example-db]
  {:query/game-by-id (clojure-game-geek/game-by-id example-db)
   :query/member-by-id (clojure-game-geek/member-by-id example-db)
   :mutation/rate-game (clojure-game-geek/rate-game example-db)
   :BoardGame/rating-summary (clojure-game-geek/rating-summary example-db)
   :BoardGame/designers (clojure-game-geek/board-game-designers example-db)
   :Member/ratings (clojure-game-geek/member-ratings example-db)
   :GameRating/game (clojure-game-geek/game-rating->game example-db)
   :Designer/games (clojure-game-geek/designer-games example-db)})

(defmethod ig/init-key ::schema
  [_ {:keys [example-db]}]
  (-> (io/resource "graphql_example/graphql-schema.edn")
      slurp
      edn/read-string
      (util/attach-resolvers (resolver-map example-db))
      schema/compile))

(defmethod ig/init-key ::service
  [_ {:keys [schema options]}]
  (lacinia/service-map schema options))
