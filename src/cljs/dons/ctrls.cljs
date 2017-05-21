(ns dons.ctrls
  (:require [re-frame.core :as re-frame]))

(defn Button
  [f label]
  [:button.button {:on-click f} label])


(defn Ctrls []
  (let [game-state    (re-frame/subscribe [:game-state])
        game-started? (re-frame/subscribe [:game-started?])
        hand          (re-frame/subscribe [:player-hand])
        player-turn?  (re-frame/subscribe [:is-player-turn?])]
    (fn []
      (let []
        [:div
         (when-not @game-started?
           [Button (fn [_]
                     (re-frame/dispatch [:start-game])) "Spiel starten"])
         (when (and (empty? @hand) @game-started?)
           [Button (fn [_]
                     (re-frame/dispatch [:player-turn-done])) "Zug beenden"])]
        (when (not @player-turn?)
          [Button (fn []) "AI spielt"])))))
