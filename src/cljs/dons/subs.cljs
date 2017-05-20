(ns dons.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :player-don
 (fn [db]
   (:player/don db)))


(re-frame/reg-sub
 :player-influence
 (fn [db]
   (-> db
       :game/game
       :player-influence)))

(re-frame/reg-sub
 :game-stats
 (fn [db]
   (:game/stats db)))
