(ns graphql-example.resolver.clojure-game-geek
  (:require [com.walmartlabs.lacinia.resolve :refer [resolve-as]]
            [graphql-example.boundary.db.clojure-game-geek :as db.clojure-game-geek]))

(defn game-by-id
  [db]
  (fn [_ args _]
    (db.clojure-game-geek/find-game-by-id db (:id args))))

(defn member-by-id
  [db]
  (fn [_ args _]
    (db.clojure-game-geek/find-member-by-id db (:id args))))

(defn rate-game
  [db]
  (fn [_ args _]
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
          game)))))

(defn rating-summary
  [db]
  (fn [_ _ board-game]
    (let [ratings (map :rating (db.clojure-game-geek/list-ratings-for-game db (:id board-game)))
          n (count ratings)]
      {:count n
       :average (if (zero? n)
                  0
                  (/ (apply + ratings)
                     (float n)))})))

(defn board-game-designers
  [db]
  (fn [_ _ board-game]
    (db.clojure-game-geek/list-designers-for-game db (:id board-game))))

(defn member-ratings
  [db]
  (fn [_ _ member]
    (db.clojure-game-geek/list-ratings-for-member db (:id member))))

(defn game-rating->game
  [db]
  (fn [_ _ game-rating]
    (db.clojure-game-geek/find-game-by-id db (:game_id game-rating))))

(defn designer-games
  [db]
  (fn [_ _ designer]
    (db.clojure-game-geek/list-games-for-designer db (:id designer))))
