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
        [:div.influence (str "💪 " @influence)]]])))


(defn Main
  ""
  []
  (let [being-played (re-frame/subscribe [:cards-being-played])
        blackmarket  (re-frame/subscribe [:blackmarket])
        player-turn? (re-frame/subscribe [:is-player-turn?])
        stats        (re-frame/subscribe [:game-stats])]
    (fn []
      (assert (sorted? @blackmarket) "BLACKmarket not sorted")
      [:div.main
       [:div.columns
        [:div.column.is-2
         [:h1 "Interstellarer Schwarz-Markt"]]
        [:div.column.buying
         [:div.columns
          (doall
           (map-indexed (fn [idx [card-idx card]]
                          (let [too-expensive? (> (:cost card) (:money @stats))]
                            ^{:key (str idx)}
                            [:div.column
                             [cards/Card card]
                             (when @player-turn?
                               [:button.button {:disabled (if too-expensive? true false)
                                                :on-click (fn [_]
                                                            (re-frame/dispatch [:buy-card {:who :player
                                                                                           :card card}]))} "Kaufen"])])) @blackmarket))]]
        [:div.column.is-2 [stats/Stats]]]
       [:div.being-played.columns
        [:div.column.is-offset-2]
        [:div.column.is-11>div.columns
         (map-indexed (fn [idx card]
                        ^{:key (str idx)}
                        [:div.column.is-2 [cards/Card card]])
                      @being-played)]]]
      )))

(defn EndScreen
  []
  (let [final-state (re-frame/subscribe [:game])
        player-don (re-frame/subscribe [:player-don])]
    (fn []
      (let [{:keys [winner
                    player-influence
                    opponent-influence]} @final-state
            player-won? (= :player winner)]
        [:div.end-screen.container
         [:div.content
          [:h1 "Spielende"]
          [:div.result (str "Gewinner mit " (max player-influence
                                          opponent-influence)
                     " Einflusspunkten " (if player-won? "Du" "der Computer")
                     "!!1!"
                     )]
          [:div
           [:button.button {:on-click (fn [_]
                                        (re-frame/dispatch [:start-game]))} "Erneut spielen"]]]]))))

(comment
  (re-frame/dispatch [:show-end-screen])


  )

(defn Game []
  (let [winner (re-frame/subscribe [:winner])]
    (fn []
      (if-not @winner
        [:div.game
         [Header]
         [Main]
         [player/Player]]

        [EndScreen]))))

(defn main-panel []
  (let []
    (fn []
      [:div.container.is-fluid
       [:div.game-container [Game]]])))
