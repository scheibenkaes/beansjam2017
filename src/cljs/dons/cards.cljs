(ns dons.cards)

(defn Card []
  (fn [{:keys [title img id description cost effect-desc planet?] :as card}]
    (let [desc (or description title)]
      [:div.card {:class (str id) :id (str (:internal/id card))}
       [:figure.image {:title desc} [:img {:src img}]]
       [:span.title {:title desc} title (when planet? " ♁")]
       [:span.cost {:title cost} cost]
       [:span.description description
        [:br] effect-desc]
       ])))
