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
      (let []
        [:div.columns
         (when-not (empty? @hand)
           (doall
            (map-indexed (fn [idx [hand-idx card]]
                           ^{:key (str "card" idx)}
                           [:div.column
                            [cards/Card card]
                            (when (= :player @turn)
                              [:button.button {:on-click (fn [_]
                                                           (re-frame/dispatch [:play-card [hand-idx card]]))} "Karte spielen"])])
                         @hand)))]))))


(defn Player
  []
  (let [player-don (re-frame.core/subscribe [:player-don])
        influence (re-frame.core/subscribe [:player-influence])]
    (fn []
      [:div.player-area.columns
       [:div.don.column.is-2
        [:div.columns
         [:div.column [dons/Don @player-don]]
         [:div.column.player-influence (str "💪 " @influence)]]]
       [:div.hand.column.hand.is-8 [Hand]]
       [:div.hand.column.controls.is-2
        [ctrls/Ctrls]]])))
