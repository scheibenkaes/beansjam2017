(ns dons.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(defn from-game-state
  ""
  [db key]
  (-> db :game/game key))

(re-frame/reg-sub
 :player-don
 (fn [db]
   (:player/don db)))

(re-frame/reg-sub
 :player-influence
 (fn [db]
   (from-game-state db :player-influence)))

(re-frame/reg-sub
 :opponent-influence
 (fn [db]
   (from-game-state db :opponent-influence)))

(re-frame/reg-sub
 :player-hand
 (fn [db]
   (from-game-state db :player-hand)))

(re-frame/reg-sub
 :game-stats
 (fn [db]
   (from-game-state db :stats)))
