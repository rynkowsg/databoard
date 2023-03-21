(ns databoard.client.components.root
  (:require
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.dom :as dom]
   [databoard.client.components.login :as dcc-login]))

(comp/defsc Root [_this {:root/keys [login]}]
  {:query [{:root/login (comp/get-query dcc-login/Login)}]
   :initial-state (fn [_] {:root/login (comp/get-initial-state dcc-login/Login {})})}
  (let [_ (js/console.log "[ui] render Root")]
    (dom/div
     (dom/h1 "Databoard")
     (dom/h4 "My data & charts")
     (dom/hr)
     (dcc-login/ui-login login))))
