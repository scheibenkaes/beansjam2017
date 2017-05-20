(ns dons.player
  (:require [re-frame.core :as re-frame]
            [dons.dons :as dons]
            [dons.ctrls :as ctrls]
            [dons.logic :as logic]
            [dons.cards :as cards]))

(defn Hand []
  (let []
    (fn []
      [:div.columns
       [:div.column.slot-1 [cards/Card logic/concealer]]
       [:div.column.slot-2 [cards/Card logic/goons]]
       [:div.column.slot-3 [cards/Card (logic/->card :id :test)]]
       [:div.column.slot-4 [cards/Card (logic/->card :id :test)]]
       [:div.column.slot-5 [cards/Card (logic/->card :id :test)]]
       
       ])))

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
