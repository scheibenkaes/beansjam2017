(ns dons.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(defn from-game-state
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
 :turn
 (fn [db]
   (from-game-state db :turn)))


(re-frame/reg-sub
 :game-state
 (fn [db]
   (from-game-state db :game-state)))

(re-frame/reg-sub
 :cards-being-played
 (fn [db]
   (from-game-state db :cards-being-played)))

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

(re-frame/reg-sub
 :player-hand
 (fn [db] (from-game-state db :player-hand)))

(re-frame/reg-sub
 :player-deck
 (fn [db] (from-game-state db :player-deck)))

(re-frame/reg-sub
 :opponent-hand
 (fn [db] (from-game-state db :opponent-hand)))

; (fset 'to-game-sub
;   [?\C-a ?\C-\M-k ?\C-y ?\C-a ?\M-\( ?r ?e ?- ?f ?r ?a ?m ?e ?/ ?r ?e ?g ?. backspace ?- ?s ?u ?b return ?\C-\M-f return ?\( ?f ?n ?\[ ?d ?b ?\C-f ?  ?\( ?f ?r ?o ?m ?- ?g ?a ?m ?e ?- ?s ?t ?a ?t ?e ?  ?d ?b ?  ?\C-y ?\C-s ?: return]
;)


(re-frame/reg-sub
 :opponent-deck
 (fn [db] (from-game-state db :opponent-deck)))
