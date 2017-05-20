(ns dons.stats
  (:require [re-frame.core :as re-frame]))


(defn Stats []
  (let [stats (re-frame/subscribe [:game-stats])]
    (fn []
      [:div.stats
       [:div.money (str "$ " (:money @stats))]
       [:div.influence (str "💪 " (:influence @stats))]])))
