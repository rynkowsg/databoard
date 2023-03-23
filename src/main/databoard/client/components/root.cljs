(ns databoard.client.components.root
  (:require
   [com.fulcrologic.fulcro-i18n.i18n :as i18n]
   [com.fulcrologic.fulcro-i18n.i18n :refer [tr]]
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.data-fetch :as df]
   [com.fulcrologic.fulcro.dom :as dom]
   [databoard.client.components.login :as dcc-login]
   [databoard.client.ui.language :as dcu-language]))

(defn get-current-locale-initial-state []
  (comp/get-initial-state i18n/Locale {:locale dcu-language/init-locale}))
#_(get-current-locale-initial-state)

(comp/defsc Root [_this {:root/keys [login] ::i18n/keys [current-locale] :as props}]
  {:query [[df/marker-table :translations-loading]
           {:root/login (comp/get-query dcc-login/Login)}
           {::i18n/current-locale (comp/get-query i18n/Locale)}]
   :initial-state (fn [_] {:root/login (comp/get-initial-state dcc-login/Login {})
                           ::i18n/current-locale (get-current-locale-initial-state)})
   :componentWillMount (fn [this] (dcu-language/load-translations-with-marker this :translations-loading))}
  (let [translations-loading? (df/loading? (get props [df/marker-table :translations-loading]))
        _ (js/console.log "[ui] render Root, translations-loading?:" translations-loading? current-locale)]
    (when-not translations-loading?
      (dom/div
       (dom/h1 (tr "Databoard"))
       (dom/h4 (tr "My data & charts"))
       (dom/hr)
       (dcc-login/ui-login login)))))

;; still loading blinks
;;  - it render it first, then hide it, then show again
;;  - go back to code without df/load and see how many times render then
