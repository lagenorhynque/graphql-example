(ns graphql-example.boundary.db.example-db
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [graphql-example.boundary.db.clojure-game-geek :as db.clojure-game-geek]
            [integrant.core :as ig]))

(defn- apply-game-rating [game-ratings game-id member-id rating]
  (->> game-ratings
       (remove #(and (= game-id (:game_id %))
                     (= member-id (:member_id %))))
       (cons {:game_id game-id
              :member_id member-id
              :rating rating})))

(defrecord Boundary [spec]
  db.clojure-game-geek/ClojureGameGeek
  (find-game-by-id [{:keys [spec]} game-id]
    (->> spec
         deref
         :games
         (filter #(= game-id (:id %)))
         first))
  (find-member-by-id [{:keys [spec]} member-id]
    (->> spec
         deref
         :members
         (filter #(= member-id (:id %)))
         first))
  (list-designers-for-game [{:keys [spec] :as db} game-id]
    (let [designers (:designers (db.clojure-game-geek/find-game-by-id db game-id))]
      (->> spec
           deref
           :designers
           (filter #(contains? designers (:id %))))))
  (list-games-for-designer [{:keys [spec]} designer-id]
    (->> spec
         deref
         :games
         (filter #(-> % :designers (contains? designer-id)))))
  (list-ratings-for-game [{:keys [spec]} game-id]
    (->> spec
         deref
         :ratings
         (filter #(= game-id (:game_id %)))))
  (list-ratings-for-member [{:keys [spec]} member-id]
    (->> spec
         deref
         :ratings
         (filter #(= member-id (:member_id %)))))
  (upsert-game-rating [{:keys [spec]} game-id member-id rating]
    (swap! spec update :ratings apply-game-rating game-id member-id rating)))

(defmethod ig/init-key ::db
  [_ _]
  (-> (io/resource "cgg-data.edn")
      slurp
      edn/read-string
      atom
      ->Boundary))

(defmethod ig/halt-key! ::db
  [_ _]
  nil)
