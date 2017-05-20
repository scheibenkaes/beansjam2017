(ns dons.player
  (:require [re-frame.core :as re-frame]
            [dons.dons :as dons]))

(defn Player
  []
  (let [player-don (re-frame.core/subscribe [:player-don])
        influence (re-frame.core/subscribe [:player-influence])]
    (fn []
      [:div.player-area.columns
       [:div.don.column
        [:div.columns
         [:div.column [dons/Don @player-don]]
         [:div.column.player-influence.is-three-quarters (str "💪 " @influence)]]]
       [:div.hand.column.hand "Hand goes here"]
       [:div.hand.column.controls.is-2 "Controlls here"]])))
