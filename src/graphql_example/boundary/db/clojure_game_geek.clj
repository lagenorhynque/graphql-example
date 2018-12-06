(ns graphql-example.boundary.db.clojure-game-geek
  (:require [duct.database.sql]))

(defprotocol ClojureGameGeek
  (find-game-by-id [db game-id])
  (find-member-by-id [db member-id])
  (list-designers-for-game [db game-id])
  (list-games-for-designer [db designer-id])
  (list-ratings-for-game [db game-id])
  (list-ratings-for-member [db member-id])
  (upsert-game-rating [db game-id member-id rating]))

(extend-protocol ClojureGameGeek
  duct.database.sql.Boundary
  ;; TODO: implement for SQL DB
  (find-game-by-id [db game-id])
  (find-member-by-id [db member-id])
  (list-designers-for-game [db game-id])
  (list-games-for-designer [db designer-id])
  (list-ratings-for-game [db game-id])
  (list-ratings-for-member [db member-id])
  (upsert-game-rating [db game-id member-id rating]))
