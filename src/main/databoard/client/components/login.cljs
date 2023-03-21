(ns databoard.client.components.login
  (:require
   [com.fulcrologic.fulcro.components :as comp]
   [com.fulcrologic.fulcro.dom :as dom]))

(comp/defsc Login [_this {}]
  {:query []
   :initial-state {}}
  (let [_ (js/console.log "[ui] render Login")]
    (dom/div
     (dom/h3 "Login"))))

(def ui-login (comp/factory Login))
