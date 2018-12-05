(ns graphql-example.resolver.clojure-game-geek
  (:require [com.walmartlabs.lacinia.resolve :refer [resolve-as]]
            [graphql-example.boundary.db.clojure-game-geek :as db.clojure-game-geek]))

(defn game-by-id [{:keys [db]} args _]
  (db.clojure-game-geek/find-game-by-id db (:id args)))

(defn member-by-id [{:keys [db]} args _]
  (db.clojure-game-geek/find-member-by-id db (:id args)))

(defn rate-game [{:keys [db]} args _]
  (let [{game-id :game_id
         member-id :member_id
         rating :rating} args
        game (db.clojure-game-geek/find-game-by-id db game-id)
        member (db.clojure-game-geek/find-member-by-id db member-id)]
    (cond
      (nil? game)
      (resolve-as nil {:message "Game not found."
                       :status 404})

      (nil? member)
      (resolve-as nil {:message "Member not found."
                       :status 404})

      (not (<= 1 rating 5))
      (resolve-as nil {:message "Rating must be between 1 and 5."
                       :status 400})

      :else
      (do
        (db.clojure-game-geek/upsert-game-rating db game-id member-id rating)
        game))))

(defn rating-summary [{:keys [db]} _ board-game]
  (let [ratings (map :rating (db.clojure-game-geek/list-ratings-for-game db (:id board-game)))
        n (count ratings)]
    {:count n
     :average (if (zero? n)
                0
                (/ (apply + ratings)
                   (float n)))}))

(defn board-game-designers [{:keys [db]} _ board-game]
  (db.clojure-game-geek/list-designers-for-game db (:id board-game)))

(defn member-ratings [{:keys [db]} _ member]
  (db.clojure-game-geek/list-ratings-for-member db (:id member)))

(defn game-rating->game [{:keys [db]} _ game-rating]
  (db.clojure-game-geek/find-game-by-id db (:game_id game-rating)))

(defn designer-games [{:keys [db]} _ designer]
  (db.clojure-game-geek/list-games-for-designer db (:id designer)))
