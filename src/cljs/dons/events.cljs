(ns dons.events
    (:require [re-frame.core :as re-frame]
              [dons.db :as db]
              [dons.logic :as logic]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 :start-game
 (fn  [db _]
   (let [game (logic/game-event [:event/setup nil] (:game/game db))]
     (assoc db :game/game game))))

(re-frame/reg-event-db
 :play-card
 (fn  [db [_ [hand-idx card]]]
   (let [game (logic/game-event [:event/play-card {:idx hand-idx :card card}] (:game/game db))]
     (assoc db :game/game game))))

(def dbg (atom {}))

(comment
  (@dbg))

(re-frame/reg-event-db
 :player-turn-done
 (fn  [db [_ _]]
   (let [game (logic/game-event [:event/turn-done :player] (:game/game db))]
     (reset! dbg game)
     (assoc db :game/game game))))

(re-frame/reg-event-db
 :set-player-don
 (fn  [db [_ don]]
   (assoc db :player/don don)))

#_(re-frame/dispatch [:set-player-don [:don/pedrox]])
