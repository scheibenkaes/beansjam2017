(ns dons.events
    (:require [re-frame.core :as re-frame]
              [dons.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))
