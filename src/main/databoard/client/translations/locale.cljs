(ns databoard.client.translations.locale
  (:require
   [com.fulcrologic.fulcro-i18n.i18n :as i18n]
   [com.fulcrologic.fulcro.data-fetch :as df]
   [databoard.client.translations.locale-def :refer [locales]]))

(defn get-init-locale []
  (let [app-locales (set (map :locale locales))
        browser-locales (map keyword (.-languages js/navigator)) ;; locales in order of preference
        matching-locales (->> browser-locales
                              (map #(if (contains? app-locales %) % nil))
                              (filter some?))]
    (or (first matching-locales) ;; since matching-locales preserve order, the first is favored
        (if (contains? locales :en) :en (first app-locales)))))
#_(get-init-locale)

(def init-locale (get-init-locale))

(defn load-initial-transl
  "Loads initial translations. Intended to be called before mount"
  [app]
  (df/load! app ::i18n/translations i18n/Locale {:params {:locale init-locale}
                                                 :post-mutation `i18n/translations-loaded}))
