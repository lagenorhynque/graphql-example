(ns graphql-example.resolver.clojure-game-geek-test
  (:require [clojure.test :as t]
            [graphql-example.resolver.clojure-game-geek :as sut]
            [graphql-example.test-helper.core :as helper :refer [with-system]]
            [venia.core :as venia]))

(t/use-fixtures
  :once
  helper/instrument-specs)

(t/deftest test-member-by-id
  (with-system [sys (helper/test-system)]
    (let [{:keys [status body]}
          (helper/http-post sys "/graphql"
                            (venia/graphql-query
                             #:venia{:queries [[:member_by_id {:id "1410"}
                                                [:member_name
                                                 [:ratings [[:game [:name
                                                                    [:rating_summary [:count
                                                                                      :average]]
                                                                    [:designers [:name
                                                                                 [:games [:name]]]]]]
                                                            :rating]]]]]}))]
      (t/is (= 200 status))
      (t/is (= {:data {:member_by_id {:member_name "bleedingedge"
                                      :ratings [{:game {:name "Zertz"
                                                        :rating_summary {:count 2
                                                                         :average 4.0}
                                                        :designers [{:name "Kris Burm"
                                                                     :games [{:name "Zertz"}]}]}
                                                 :rating 5}
                                                {:game {:name "Tiny Epic Galaxies"
                                                        :rating_summary {:count 1
                                                                         :average 4.0}
                                                        :designers [{:name "Scott Almes"
                                                                     :games [{:name "Tiny Epic Galaxies"}]}]}
                                                 :rating 4}
                                                {:game {:name "7 Wonders: Duel"
                                                        :rating_summary {:count 3
                                                                         :average 4.333333333333333}
                                                        :designers [{:name "Antoine Bauza"
                                                                     :games [{:name "7 Wonders: Duel"}]}
                                                                    {:name "Bruno Cathala"
                                                                     :games [{:name "7 Wonders: Duel"}]}]}
                                                 :rating 4}]}}}
               (helper/<-json body))))))
