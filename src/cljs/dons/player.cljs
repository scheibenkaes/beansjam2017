(ns dons.player
  (:require [re-frame.core :as re-frame]
            [dons.dons :as dons]
            [dons.ctrls :as ctrls]
            [dons.logic :as logic]
            [dons.cards :as cards]))

(defn Hand []
  (let [hand (re-frame/subscribe [:player-hand])
        turn (re-frame/subscribe [:turn])]
    (fn []
      [:div.columns
       (when-not (empty? @hand)
         (assert (sorted? @hand) "Hand not sorted")
         (doall
          (map-indexed (fn [idx [hand-idx card]]
                         ^{:key (str "card" idx)}
                         [:div.column
                          [:div.columns>div.column
                           [cards/Card card]]
                          (when (= :player @turn)
                            [:div.columns>div.column
                             [:button.button {:on-click (fn [_]
                                                          (re-frame/dispatch [:play-card [card]]))} "Karte spielen"]])])
                       @hand)))])))


(defn Player
  []
  (let [player-don (re-frame/subscribe [:player-don])
        influence  (re-frame/subscribe [:player-influence])
        discard    (re-frame/subscribe [:player-discard])]
    (fn []
      (println @discard)
      [:div.player-area.columns
       [:div.don.column.is-2
        [:div.columns
         [:div.column [dons/Don @player-don]]
         [:div.column.player-influence (str "💪 " @influence)]]]
       [:div.hand.column.hand.is-8
        [:div.columns
         [:div.column.is-10 [Hand]]
         [:div.column.is-2 {:title "Ablagestapel"}
          (when (pos? (count @discard))
            [cards/Card {:title "" :id "player-discard" :img "img/card-back.png"}])]]]
       [:div.hand.column.controls.is-2
        [ctrls/Ctrls]]])))
