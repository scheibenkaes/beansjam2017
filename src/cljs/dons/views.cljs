(ns dons.views
  (:require [re-frame.core :as re-frame]
            [dons.dons :as dons]
            [dons.cards :as cards]
            [dons.stats :as stats]
            [dons.logic :as logic]
            [dons.player :as player])
  )

(defn Header
  []
  (let [influence (re-frame/subscribe [:opponent-influence])]
    (fn []
      [:div.header.columns
       [:div.column.is-three-quarters
        [:h2 "Dons"]]
       [:div.column.opponent
        [:div (str "💪 " @influence)]]])))


(defn Main
  ""
  []
  (let [being-played (re-frame/subscribe [:cards-being-played])
        blackmarket  (re-frame/subscribe [:blackmarket])
        player-turn? (re-frame/subscribe [:is-player-turn?])
        stats        (re-frame/subscribe [:game-stats])]
    (fn []
      [:div.main
       [:div.columns
        [:div.column.is-2
         [:h1 "Interstellarer Schwarz-Markt"]]
        [:div.column.buying
         [:div.columns
          (doall
           (map-indexed (fn [idx [card-idx card]]
                          (let [too-expensive? (> (:cost card) (-> @stats :money))]
                            ^{:key (str idx)}
                            [:div.column
                             [cards/Card card]
                             (when @player-turn?
                               [:button.button {:disabled (if too-expensive? true false)} "Kaufen"])])) @blackmarket))]]
        [:div.column.is-2 [stats/Stats]]]
       [:div.being-played.columns
        (map-indexed (fn [idx card]
                       ^{:key (str idx)}
                       [:div.column [cards/Card card]])
                     @being-played)]]
      )))

(defn Game []
  (let []
    (fn []
      [:div.game
        [Header]
        [Main]
        [player/Player]])))

(defn main-panel []
  (let []
    (fn []
      [:div.container.is-fluid
       [:div.game-container [Game]]])))
