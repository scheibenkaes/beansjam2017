(ns dons.cards)

(defn Card []
  (fn [{:keys [title img id description] :as card}]
    (let [desc (or description title)]
      [:div.card {:class (str id) :id (str (:internal/id card))}
       [:figure.image {:title desc} [:img {:src img}]]
       [:span.title {:title desc} title]])))
