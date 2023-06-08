(ns databoard.client.components.login
  (:require
   [com.fulcrologic.fulcro-i18n.i18n :refer [tr]]
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.dom :as dom]
   [com.fulcrologic.fulcro-i18n.i18n-sample-locale-selector :as sample]
   [databoard.client.translations.locale-selector :refer [get-locale-selector-initial-state]]))

(comp/defsc Login [_this {:keys [locale-selector]}]
  {:query [{:locale-selector (comp/get-query sample/LocaleSelector)}]
   :initial-state (fn [_] {:locale-selector (get-locale-selector-initial-state)})}
  (let [_ (js/console.log "[ui] render Login")]
    (dom/div
     (dom/h3 (tr "Login"))
     (dom/hr)
     (sample/ui-locale-selector locale-selector)
     (dom/hr))))

(def ui-login (comp/factory Login))
