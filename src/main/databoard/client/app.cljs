(ns databoard.client.app
  (:require
   [com.fulcrologic.fulcro.application :as app]
   [com.fulcrologic.fulcro.networking.http-remote :as http]
   [com.fulcrologic.fulcro.react.version18 :refer [with-react18]]
   ;[com.fulcrologic.fulcro.rendering.keyframe-render :as keyframe-render]
   [com.fulcrologic.fulcro.rendering.multiple-roots-renderer :as multiple-roots-renderer]
   [databoard.client.components.root :as dcc-root]))

(defonce app
  (with-react18
   (app/fulcro-app
    {:remotes {:remote (http/fulcro-http-remote {:url "/api"})}
     :optimized-render! multiple-roots-renderer/render!})))

(defn ^:export init []
  (app/mount! app dcc-root/Root "app")
  (js/console.log "Loaded"))

(defn ^:export refresh []
  (app/mount! app dcc-root/Root "app")
  (js/console.log "Hot reload"))
