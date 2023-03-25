(ns databoard.client.components.login
  (:require
   [com.fulcrologic.fulcro-i18n.i18n :refer [tr]]
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.dom :as dom]
   [databoard.client.translations.locale-selector :refer [LocaleSelector get-locale-selector-initial-state ui-locale-selector]]))

(comp/defsc Login [_this {:keys [locale-selector]}]
  {:query [{:locale-selector (comp/get-query LocaleSelector)}]
   :initial-state (fn [_] {:locale-selector (get-locale-selector-initial-state)})}
  (let [_ (js/console.log "[ui] render Login")]
    (dom/div
     (dom/h3 (tr "Login"))
     (dom/hr)
     (ui-locale-selector locale-selector)
     (dom/hr))))

(def ui-login (comp/factory Login))
