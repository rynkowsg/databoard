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

(def initial-transl-marker [df/marker-table :initial-transl])

(comp/defsc Root [this {:root/keys [login] ::i18n/keys [current-locale] :as props}]
  {:query [initial-transl-marker
           {:root/login (comp/get-query dcc-login/Login)}
           {::i18n/current-locale (comp/get-query i18n/Locale)}]
   :initial-state (fn [_] {:root/login (comp/get-initial-state dcc-login/Login {})
                           ::i18n/current-locale (get-current-locale-initial-state)})
   :UNSAFE_componentWillMount (fn [this] (dcu-language/load-translations-with-marker this :initial-transl))
   :initLocalState (fn [_ _] {:transl-on-start (atom :none)})} ;; :none :loading :ready
  (let [transl-loading? (df/loading? (get props initial-transl-marker))
        transl-ready? (let [initial-transl (comp/get-state this :transl-on-start)]
                        (swap! initial-transl #(cond
                                                 (and (= % :none) (not transl-loading?)) :none
                                                 (and (= % :none) transl-loading?) :loading
                                                 (and (= % :loading) transl-loading?) :loading
                                                 (and (= % :loading) (not transl-loading?)) :ready
                                                 :ready :ready))
                        @initial-transl)
        _ (js/console.log "[ui] render Root, transl-ready?" transl-ready? "transl-loading?:" transl-loading? (:ui/locale-name current-locale) (::i18n/translations current-locale))]
    (when (= transl-ready? :ready)
      (dom/div
       (dom/h1 (tr "Databoard"))
       (dom/h4 (tr "My data & charts"))
       (dom/hr)
       (dcc-login/ui-login login)))))

;; still loading blinks
;;  - it render it first, then hide it, then show again
;;  - go back to code without df/load and see how many times render then
