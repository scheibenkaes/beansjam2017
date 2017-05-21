(ns dons.logic)

(def target-influence 50)

(defn winner
  "Determine the winner :player or :opponent, nil if none yet."
  [{:keys [player-influence opponent-influence]}]
  (cond (>= player-influence target-influence)   :player
        (>= opponent-influence target-influence) :opponent
        :default                                 nil))

(defn ->card [& {:keys [id img title effect text planet? cost description]
                 :or   {img     "img/card.png" title "No Title" effect identity text ""
                        planet? false          cost  1}}]
  {:id          id
   :cost        cost
   :card?       true
   :planet?     planet?
   :img         img
   :title       title
   :description description
   :text        text
   :effect      effect
   :internal/id (gensym)})

;;; CARDS

(defn noob []
  (->card :id :card/noob
          :title "Space Noob"
          :description "Sie sind noch etwas grün hinter den Ohren."
          :effect (fn [state]
                    (update-in state [:stats :influence] (partial + 1)))))

(defn collector []
  (->card :id :card/collector
          :title "Eintreiber"
          :effect (fn [state]
                    (update-in state [:stats :money] (partial + 1)))))

(defn concealer []
  (->card :id :card/concealer
          :title "Space Hehler"
          :cost 2
          :effect (fn [state]
                    (update-in state [:stats :money] (partial + 2)))))

(defn goons []
  (->card :id :card/goons
          :title "Schlägertypen"
          :cost 3
          :effect (fn [state]
                    (update-in state [:stats :influence] (partial + 2)))))

;;; PLANETS

(defn jackson []
  (->card :id :planet/jackson
          :planet? true
          :cost 5
          :title "Jackson"
          :effect (fn [state]
                    (-> state
                        (update-in [:stats :influence] (partial + 4))
                        (update-in [:status :money] (partial + 1))))))

(defn mars []
  (->card :id :planet/mars
          :planet? true
          :cost 6
          :title "Mars"
          :effect (fn [state]
                    (update-in state [:stats :influence] (partial + 5)))))

(def all-cards
  #{(noob) (collector) (concealer) (goons) (jackson) (mars)})

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
   :blackmarket-deck   []
   :winner             nil})

(def num-cards-at-begin 5)

(defn initial-deck []
  (concat
   (repeatedly 7 collector)
   (repeatedly 3 noob)))

(def num-cards-blackmarket 5)

(defn initial-blackmarket-deck []
  (concat
   (repeatedly 1 jackson)
   (repeatedly 1 mars)
   (repeatedly 10 goons)
   (repeatedly 10 concealer)))

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
  [[_ {card :card who :who}] {:keys [player-hand
                                     opponent-hand
                                     cards-being-played] :as state}]
  (let [trigger-effect (fn [state]
                         ((:effect card) state))
        hand-k (if (= who :player) :player-hand :opponent-hand)
        hand (get state hand-k)

        idx  (some (fn [[i c]] (when (= (:internal/id card)
                                       (:internal/id c))
                                i)) hand)]
    (println "PLAY i" (:internal/id card) " " idx)
    (assert (int? idx) (str "No card with id " (:internal/id card) " in hand " hand))


    (-> state
        (update-in [:cards-being-played] conj card)
        (update-in [hand-k] dissoc idx)
        trigger-effect)))


(defmethod game-event :event/opponents-turn
  [_ {:keys [opponent-hand blackmarket] :as state}]
  (println "I'm a robot" (:stats state))
  (if-let [[idx card] (first opponent-hand)]
    (game-event [:event/play-card {:card card :who :opponent}] state)

    ;; Keine Karten mehr auf der Hand.
    ;;
    ;; AI soll die teuersten Karten die er bekommen kann kaufen
    (do
      (let [money            (get-in state [:stats :money])
            cards-for-sale   (sort-by :cost > (vals blackmarket))
            first-affordable (some (fn [{cost :cost :as c}]
                                     (when (>= money cost) c)) cards-for-sale)]
        (println "AI has " money " $")
        (println "AI I can afford " first-affordable)
        (if first-affordable
          (game-event [:event/buy-card {:who :opponent :card first-affordable}] state)
          (assoc state :ai/done? true))))))

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
  (let [was-player? (= who :player)
        deck        (if was-player? player-deck opponent-deck)
        deck-k      (if was-player? :player-deck :opponent-deck)

        hand   (if was-player? player-hand opponent-hand)
        hand-k (if was-player? :player-hand :opponent-hand)

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
    (as-> state s
      (assoc s
             deck-k new-deck
             hand-k new-hand
             influence-k (+ influence-old influence-gained)
             discard-k (if (not enough-in-deck?) discard-after-play [])
             :cards-being-played []
             :stats initial-stats
             :turn (if was-player? :opponent :player))
      (assoc s :winner (winner s)))))

(def dbg (atom {}))

(comment

  (-> @dbg)

  )

;; Karte wird dem Ablagestapel hinzugefügt
;; Karte wird aus dem Schwarzmarkt entfernt
;; Karte wird mit neuer ersetzt
;; Geld wird abgezogen
(defmethod game-event :event/buy-card
  [[_ {:keys [who card]}]
   {:keys [blackmarket
           blackmarket-deck
           player-discard
           opponent-discard
           stats]
    :as   state}]
  (let [was-player? (= who :player)
        discard     (if was-player? player-discard opponent-discard)
        discard-k   (if was-player? :player-discard :opponent-discard)

        card-idx (some (fn [[i c]] (when (= (:internal/id card)
                                           (:internal/id c))
                                    i)) blackmarket)

        blackmarket-new        (dissoc blackmarket card-idx)
        replacement-card       (first blackmarket-deck)
        remaining-black-market (drop 1 blackmarket-deck)
        blackmarket-deck-new   (if (empty? remaining-black-market)
                                 (shuffle (remove
                                           :planet?
                                           (initial-blackmarket-deck)))
                                 remaining-black-market)

        ;; Neue Karte einfügen
        blackmarket-new (assoc blackmarket-new card-idx replacement-card)

        stats (update stats :money (fn [amount]
                                     (- amount (:cost card))))]

    (when (not was-player?)
      (println "storing in dbg")
      (reset! dbg {:card card
                   :state state}))

    (assert (int? card-idx) (str "no card with id " (:internal/id card) " found"))
    (assoc state
           :blackmarket blackmarket-new
           :blackmarket-deck blackmarket-deck-new

           :stats stats

           discard-k (conj discard card))))

(defmethod game-event :default [_ state]
  (assert false "Unknown game event"))
