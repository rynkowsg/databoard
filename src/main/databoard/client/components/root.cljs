(ns databoard.client.components.root
  (:require
   [com.fulcrologic.fulcro-i18n.i18n :as i18n]
   [com.fulcrologic.fulcro-i18n.i18n :refer [tr]]
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.dom :as dom]
   [databoard.client.components.login :as dcc-login]
   [databoard.client.translations.locale :as dct-locale]))

(defn get-current-locale-initial-state []
  (comp/get-initial-state i18n/Locale {:locale dct-locale/init-locale}))
#_(get-current-locale-initial-state)

(comp/defsc Root [_this {:root/keys [login] ::i18n/keys [current-locale]}]
  {:query [{:root/login (comp/get-query dcc-login/Login)}
           {::i18n/current-locale (comp/get-query i18n/Locale)}]
   :initial-state {:root/login {}
                   ::i18n/current-locale {:locale dct-locale/init-locale}}}
  (let [transl-exists? (some? (::i18n/translations current-locale))
        _ (js/console.log "[ui] render Root, locale" (:ui/locale-name current-locale) "exists?" transl-exists? (::i18n/translations current-locale))]
    (when transl-exists?
      (dom/div
       (dom/h1 (tr "Databoard"))
       (dom/h4 (tr "My data & charts"))
       (dom/hr)
       (dcc-login/ui-login login)))))

;; still loading blinks
;;  - it render it first, then hide it, then show again
;;  - go back to code without df/load and see how many times render then
