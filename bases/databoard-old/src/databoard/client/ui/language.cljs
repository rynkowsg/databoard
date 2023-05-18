(ns databoard.client.ui.language
  (:require
   [clojure.set :as set]
   [com.fulcrologic.fulcro-i18n.i18n :as i18n]
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.data-fetch :as df]
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.fulcro.dom.events :as evt]))

;; supported locales

(def locales
  [{:locale :de :name "Deutsch"}
   {:locale :en :name "English"}
   {:locale :es :name "Español"}
   {:locale :pl :name "Polski"}])

(defn get-init-locale []
  (let [app-locales (map :locale locales)
        browser-locales (map keyword (.-languages js/navigator))
        matching-locales (set/intersection (set app-locales) (set browser-locales))]
    (or (first matching-locales) (first app-locales))))
#_(get-init-locale)

(def init-locale (get-init-locale))

;; component (todo: Extract component to separate file)

(declare set-locale)

(comp/defsc LocaleSelector
  "A reusable locale selector. Generates a simple `dom/select` with CSS class fulcro$i18n$locale_selector.

  Remember that for localization to work you *must* query for `::i18n/current-locale` in your root
  component with the query [{::i18n/current-locale (prim/get-query Locale)}]."
  [this {::keys [available-locales] ::i18n/keys [current-locale]}]
  {:query [{::available-locales (comp/get-query i18n/Locale)}
           {[::i18n/current-locale '_] (comp/get-query i18n/Locale)}]
   :initial-state {::available-locales :param/locales}}
  (let [selected-locale (::i18n/locale current-locale :en)
        mk-option (fn [i {::i18n/keys [locale] :ui/keys [locale-name]}]
                    (dom/option {:key i :value locale} locale-name)) ;; attach locale as a value to option
        on-change (fn [evt] (when-let [locale (keyword (evt/target-value evt))]
                              (set-locale (comp/any->app this) locale)))]
    (dom/select :.fulcro$i18n$locale_selector
      {:onChange on-change :value selected-locale}
      (map-indexed mk-option available-locales))))

(def ui-locale-selector (comp/factory LocaleSelector))

(defn get-locale-selector-initial-state []
  (comp/get-initial-state
   LocaleSelector
   {:locales (->> locales
                  (map #(comp/get-initial-state i18n/Locale %))
                  (into []))}))
#_(get-locale-selector-initial-state)

;; locale updates

;; TODO: consider updating meta
(defn set-locale [app l]
  (comp/transact! app `[(i18n/change-locale {:locale ~(keyword l)})]))
#_(set-locale databoard.client.app/app "en")
#_(set-locale databoard.client.app/app :pl)

(defn load-translations-with-marker [app marker]
  (let [l (-> app app/current-state ::i18n/current-locale second)]
    (df/load! app ::i18n/translations i18n/Locale {:params {:locale l}
                                                   :marker marker
                                                   :post-mutation `i18n/translations-loaded})
    #_(set-locale app l)                                    ;; works but causes the refresh
    #_(app/force-root-render! app)))                        ;; doesn't work
#_(load-current-locale-translations databoard.client.app/app)

#_(comment
   ;; load :de translations
   (df/load! databoard.client.app/app ::i18n/translations i18n/Locale {:params {:locale :de}}))

;; Questions:
;; - Does available-locales make any sense to query?
;;   - Tony was passing available-locales to make the selector smarter, but we probably can hardcode these
;; - At the end this is the information we hardcode, it doesn't have to be resolved.
;; - What is the point of using locale-kw?
;; - Why do I need ::i18n/current-locale in the Root?
;; - :shared-fn ::i18n/current-locale
