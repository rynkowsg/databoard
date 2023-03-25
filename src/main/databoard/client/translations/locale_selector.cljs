(ns databoard.client.translations.locale-selector
  (:require
   [com.fulcrologic.fulcro-i18n.i18n :as i18n]
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.fulcro.dom.events :as evt]
   [databoard.client.translations.locale-def :refer [locales]]))

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
        on-change (fn [evt] (when-let [locale (keyword (evt/target-value evt))] ;; extract the locale value
                              (set-locale (comp/any->app this) locale)))]
    (dom/select :.fulcro$i18n$locale_selector
      {:onChange on-change :value selected-locale}
      (map-indexed mk-option available-locales))))

(def ui-locale-selector (comp/factory LocaleSelector))

(defn get-locale-selector-initial-state []
  (comp/get-initial-state
   LocaleSelector
   {:locales (->> locales (map #(comp/get-initial-state i18n/Locale %)) (into []))}))
#_(get-locale-selector-initial-state)

;; locale updates

;; TODO: consider updating meta
(defn set-locale [app l]
  (comp/transact! app `[(i18n/change-locale {:locale ~(keyword l)})]))
#_(set-locale databoard.client.app/app "en")
#_(set-locale databoard.client.app/app :pl)
