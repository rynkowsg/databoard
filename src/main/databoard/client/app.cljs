(ns databoard.client.app
  (:require
   [com.fulcrologic.fulcro-i18n.i18n :as i18n]
   [com.fulcrologic.fulcro-i18n.icu-formatter :as icu]
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.networking.http-remote :as http]
   [com.fulcrologic.fulcro.react.version18 :refer [with-react18]]
   ;[com.fulcrologic.fulcro.rendering.keyframe-render :as keyframe-render]
   [com.fulcrologic.fulcro.rendering.multiple-roots-renderer :as multiple-roots-renderer]
   [databoard.client.components.root :as dcc-root]
   [databoard.client.ui.language :as dcu-language]))

(defonce app
  (with-react18
   (app/fulcro-app
    {:client-did-mount (fn [app] (dcu-language/load-current-locale-translations app))
     :remotes {:remote (http/fulcro-http-remote {:url "/api"})}
     :optimized-render! multiple-roots-renderer/render!
     :shared {::i18n/message-formatter icu/format}
     :shared-fn ::i18n/current-locale})))
     ;:shared-fn (fn [db]
     ;             (js/console.log "Shared Fn" (-> db ::i18n/current-locale))
     ;             (merge {} (::i18n/current-locale db)))})))

(defn ^:export init []
  (app/mount! app dcc-root/Root "app")
  (js/console.log "Loaded"))

(defn ^:export refresh []
  (app/mount! app dcc-root/Root "app")
  (js/console.log "Hot reload"))
