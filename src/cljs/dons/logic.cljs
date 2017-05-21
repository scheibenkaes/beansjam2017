(ns dons.logic)


(defn ->card [& {:keys [id img title effect text planet? cost]
                 :or   {img     "img/card.png" title "No Title" effect identity text ""
                        planet? false          cost  1}}]
  {:id      id
   :cost    cost
   :card?   true
   :planet? planet?
   :img     img
   :title   title
   :text    text
   :effect  effect})

;;; CARDS

(def noob
  (->card :id :card/noob
          :title "Space Noob"
          :effect (fn [state]
                    (update-in state [:stats :influence] (partial + 1)))))

(def collector
  (->card :id :card/collector
          :title "Eintreiber"
          :effect (fn [state]
                    (update-in state [:stats :money] (partial + 1)))))

(def concealer
  (->card :id :card/concealer
          :title "Space Hehler"
          :effect (fn [state]
                    (update-in state [:stats :money] (partial + 2)))))

(def goons
  (->card :id :card/goons
          :title "Schlägertypen"
          :effect (fn [state]
                    (update-in state [:stats :influence] (partial + 2)))))

;;; PLANETS

(def jackson
  (->card :id :planet/jackson
          :planet? true
          :cost 5
          :titel "Jackson"))

(def mars
  (->card :id :planet/mars
          :planet? true
          :cost 6
          :titel "Mars"))

(def all-cards
  #{noob collector concealer goons jackson mars})

(def all-planets
  (filter :planet? all-cards))


;;; events

(def initial-stats
  {:influence 0
   :money     0})


(def initial-state
  {:player-hand        (sorted-map)
   :opponent-hand      (sorted-map)
   :game-state         :state/pre-game
   :player-deck        []
   :opponent-deck      []
   :cards-being-played []
   :player-discard     []
   :opponent-discard   []
   :turn               :player
   :player-influence   0
   :opponent-influence 0
   :stats              initial-stats
   :blackmarket        (sorted-map)
   :blackmarket-deck   []})

(def num-cards-at-begin 5)

(defn initial-deck []
  (concat
   (repeat 7 collector)
   (repeat 3 noob)))

(def num-cards-blackmarket 5)

(defn initial-blackmarket-deck []
  (concat
   (repeat 1 jackson)
   (repeat 1 mars)
   (repeat 10 goons)
   (repeat 10 concealer)))

(defn to-hand [coll]
  (into (sorted-map)
        (map-indexed (fn [idx c] [idx c]) coll)))

(defmulti game-event (fn [[e _] state] e))

(defmethod game-event :event/setup
  [_ state]
  (let [[player-hand player-deck]      (split-at num-cards-at-begin (shuffle (initial-deck)))
        [opponent-hand opponent-deck]  (split-at num-cards-at-begin (shuffle (initial-deck)))
        [blackmarket blackmarket-deck] (split-at num-cards-blackmarket (shuffle (initial-blackmarket-deck)))]
    (assoc state
           :game-state :state/started
           :player-deck player-deck
           :opponent-deck opponent-deck
           :blackmarket (to-hand blackmarket)
           :blackmarket-deck blackmarket-deck
           :player-hand (to-hand player-hand)
           :opponent-hand (to-hand opponent-hand))))

(defmethod game-event :event/play-card
  [[_ {idx :idx card :card who :who}] {:keys [player-hand
                                              opponent-hand
                                              cards-being-played] :as state}]
  (let [trigger-effect (fn [state]
                         ((:effect card) state))
        hand-k (if (= who :player) :player-hand :opponent-hand)]
    (-> state
        (update-in [:cards-being-played] conj card)
        (update-in [hand-k] dissoc idx)
        trigger-effect)))

(defmethod game-event :event/opponents-turn
  [_ {:keys [opponent-hand] :as state}]
  (println "I'm a robot")
  (if-let [[idx card] (first opponent-hand)]
    (game-event [:event/play-card {:idx idx :card card :who :opponent}] state)
    (assoc state :ai/done? true)))

(def hand-size 5)

;; * Einfluss wird gut geschrieben
;;
;; * Geld verfällt
;;
;; * Gespielte Karten auf den Ablagestapel
;;
;; * Sind weniger als 5 Karten im Nachziehstapel?
;; Ja: Gespielte + Abgelegte bilden neues Deck
;;     Es werden 5 - x Karten vom Deck gezogen
;; Nein: 5 Ziehen
;; 
;; * Der andere ist dran
;; 
(defmethod game-event :event/turn-done
  [[_ who] {:keys [player-hand
                   player-deck
                   player-discard
                   player-influence

                   opponent-hand
                   opponent-deck
                   opponent-discard
                   opponent-influence                   
                   
                   stats
                   cards-being-played]
            :as state}]
  (let [was-player?      (= who :player)
        deck             (if was-player? player-deck opponent-deck)
        deck-k           (if was-player? :player-deck :opponent-deck)
        
        hand             (if was-player? player-hand opponent-hand)
        hand-k           (if was-player? :player-hand :opponent-hand)
        
        discard          (if was-player? player-discard opponent-discard)
        discard-k        (if was-player? :player-discard :opponent-discard)
        influence-k      (if was-player? :player-influence :opponent-influence)
        influence-old    (if was-player? player-influence opponent-influence)
        influence-gained (:influence stats)
        
        ;; Nachziehen
        discard-after-play (concat discard cards-being-played)
        enough-in-deck?    (> (count deck) hand-size)

        deck (if enough-in-deck? deck (shuffle discard-after-play))
        
        new-hand (to-hand (take hand-size deck))
        new-deck (drop hand-size deck)]
    (assoc state
           deck-k new-deck
           hand-k new-hand
           influence-k (+ influence-old influence-gained)
           discard-k (if enough-in-deck? discard-after-play [])
           :cards-being-played []
           :stats initial-stats
           :turn (if was-player? :opponent :player))))


(defmethod game-event :default [_ state] state)
