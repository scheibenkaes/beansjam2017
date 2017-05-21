(ns dons.events
    (:require [re-frame.core :as re-frame]
              [dons.db :as db]
              [dons.logic :as logic]))

(def dbg (atom {}))


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
 (fn  [db [_ [card]]]
   (let [game (logic/game-event [:event/play-card {:card card :who :player}] (:game/game db))]
     (reset! dbg {:game game :event [:play card]})
     (assoc db :game/game game))))

(comment
  (get (:blackmarket @dbg) 2)

  (:stats @dbg)

  (-> @dbg :game :blackmarket (get 2))

  (-> @dbg :event)
  
  )


(re-frame/reg-event-fx
 :turn-done
 (fn  [cofx [_ who]]
   (let [game        (logic/game-event [:event/turn-done who] (:game/game (:db cofx)))
         was-player? (= who :player)]
     (if was-player?
       {:db       (assoc (:db cofx) :game/game game)
        :dispatch [:run-opponent nil]}
       {:db (assoc (:db cofx) :game/game game)}))))

(re-frame/reg-event-fx
 :run-opponent
 (fn  [cofx [_ _]]
   (println "running AI")
   (let [game      (-> cofx :db :game/game)
         new-state (logic/game-event [:event/opponents-turn nil] game)
         ai-done?  (:ai/done? new-state)
         new-state (if ai-done? (dissoc new-state :ai/done?) new-state)
         effects   {:db (assoc (:db cofx) :game/game new-state)}]
     (if ai-done?
       (merge effects {:dispatch [:turn-done :opponent]} )
       (merge effects {:dispatch-later [{:ms 500 :dispatch [:run-opponent nil]}]})))))


(re-frame/reg-event-db
 :buy-card
 (fn  [db [_ {:keys [who card] :as which}]]
   (let [new-state (logic/game-event [:event/buy-card which] (:game/game db))]
     (reset! dbg {:game new-state :event [:buy card]})
     (assoc db :game/game new-state))))

(re-frame/reg-event-db
 :set-player-don
 (fn  [db [_ don]]
   (assoc db :player/don don)))

#_(re-frame/dispatch [:set-player-don [:don/pedrox]])
