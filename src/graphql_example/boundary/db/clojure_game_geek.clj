(ns graphql-example.boundary.db.clojure-game-geek)

(defn find-game-by-id
  [db game-id]
  (->> db
       deref
       :games
       (filter #(= game-id (:id %)))
       first))

(defn find-member-by-id
  [db member-id]
  (->> db
       deref
       :members
       (filter #(= member-id (:id %)))
       first))

(defn list-designers-for-game
  [db game-id]
  (let [designers (:designers (find-game-by-id db game-id))]
    (->> db
         deref
         :designers
         (filter #(contains? designers (:id %))))))

(defn list-games-for-designer
  [db designer-id]
  (->> db
       deref
       :games
       (filter #(-> % :designers (contains? designer-id)))))

(defn list-ratings-for-game
  [db game-id]
  (->> db
       deref
       :ratings
       (filter #(= game-id (:game_id %)))))

(defn list-ratings-for-member
  [db member-id]
  (->> db
       deref
       :ratings
       (filter #(= member-id (:member_id %)))))

(defn ^:private apply-game-rating
  [game-ratings game-id member-id rating]
  (->> game-ratings
       (remove #(and (= game-id (:game_id %))
                     (= member-id (:member_id %))))
       (cons {:game_id game-id
              :member_id member-id
              :rating rating})))

(defn upsert-game-rating
  [db game-id member-id rating]
  (swap! db update :ratings apply-game-rating game-id member-id rating))
