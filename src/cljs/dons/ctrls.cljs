(ns dons.ctrls
  (:require [re-frame.core :as re-frame]))

(defn Button
  ""
  [f label]
  [:button.button {:on-click f} label])


(defn Ctrls []
  (let [game-state (re-frame/subscribe [:game-state])
        hand (re-frame/subscribe [:player-hand])]
    (fn []
      [:div
       (when (= :state/pre-game @game-state)
        [Button (fn [_]
                  (re-frame/dispatch [:start-game])) "Spiel starten"])
       (when (empty? @hand)
         [Button (fn []) "Zug beenden"])])))
