(ns graphql-example.graphql
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [com.walmartlabs.lacinia.pedestal :as lacinia]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [graphql-example.resolver.clojure-game-geek :as clojure-game-geek]
            [integrant.core :as ig]))

(def resolver-map
  {:query/game-by-id clojure-game-geek/game-by-id
   :query/member-by-id clojure-game-geek/member-by-id
   :mutation/rate-game clojure-game-geek/rate-game
   :BoardGame/rating-summary clojure-game-geek/rating-summary
   :BoardGame/designers clojure-game-geek/board-game-designers
   :Member/ratings clojure-game-geek/member-ratings
   :GameRating/game clojure-game-geek/game-rating->game
   :Designer/games clojure-game-geek/designer-games})

(defmethod ig/init-key ::schema
  [_ _]
  (-> (io/resource "graphql_example/graphql-schema.edn")
      slurp
      edn/read-string
      (util/attach-resolvers resolver-map)
      schema/compile))

(defmethod ig/init-key ::service
  [_ {:keys [schema options]}]
  (lacinia/service-map schema options))
