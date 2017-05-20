(ns dons.views
  (:require [re-frame.core :as re-frame]
            [dons.dons :as dons])
  )

(defn Header
  ""
  []
  (let []
    (fn []
      [:div.header.columns
       [:div.column.is-three-quarters
        [:h2 "Dons"]]
       [:div.column.opponent "Opponent goes here"]])))


(defn Main
  ""
  []
  (let []
    (fn []
      [:div.main.columns
       [:div.column
        [:h1 "Interstellarer Schwarz-Markt"]]
       [:div.column "Cards here"]
       [:div.column.stats "Stats here"]]
      )))

(defn PlayerArea
  ""
  []
  (let [player-don (re-frame.core/subscribe [:player-don])]
    (fn []
      [:div.player-area.columns
       [:div.don.column [dons/Don @player-don]]
       [:div.hand.column "Hand goes here"]
       [:div.hand.column "Controlls here"]])))

(defn Game []
  (let []
    (fn []
      [:div.game
        [Header]
        [Main]
        [PlayerArea]])))

(defn main-panel []
  (let []
    (fn []
      [:div.container.is-fluid
       [:div.game-container [Game]]])))
