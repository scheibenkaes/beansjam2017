(ns dons.events
    (:require [re-frame.core :as re-frame]
              [dons.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 :set-player-don
 (fn  [db [_ don]]
   (assoc db :player/don don)))

#_(re-frame/dispatch [:set-player-don [:don/pedrox]])
