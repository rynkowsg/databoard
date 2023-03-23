(ns databoard.client.components.root
  (:require
   [com.fulcrologic.fulcro-i18n.i18n :as i18n]
   [com.fulcrologic.fulcro-i18n.i18n :refer [tr]]
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.dom :as dom]
   [databoard.client.components.login :as dcc-login]
   [databoard.client.ui.language :as dcu-language]))

(defn get-current-locale-initial-state []
  (comp/get-initial-state i18n/Locale {:locale (dcu-language/get-init-locale)}))
#_(get-current-locale-initial-state)

(comp/defsc Root [_this {:root/keys [login]}]
  {:query [{:root/login (comp/get-query dcc-login/Login)}
           {::i18n/current-locale (comp/get-query i18n/Locale)}]
   :initial-state (fn [_] {:root/login (comp/get-initial-state dcc-login/Login {})
                           ::i18n/current-locale (get-current-locale-initial-state)})}
  (let [_ (js/console.log "[ui] render Root")]
    (dom/div
     (dom/h1 (tr "Databoard"))
     (dom/h4 (tr "My data & charts"))
     (dom/hr)
     (dcc-login/ui-login login))))
