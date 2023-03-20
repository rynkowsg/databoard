(ns databoard.server.http-server
  (:require
   [clojure.pprint :refer [pprint]]
   [databoard.server.config :as ds-config]
   [databoard.server.http-handler :as dsh-handler]
   [mount.core :as mount]
   [org.httpkit.server :as hs]
   [taoensso.timbre :as log]))

(mount/defstate server
  :start
  (let [cfg (merge (::hs/config ds-config/config))]
    (log/info "Starting HTTP Server with config" (with-out-str (pprint cfg)))
    (let [cfg' (merge cfg {:legacy-return-value? false})]
      (hs/run-server dsh-handler/handler cfg')))
  :stop
  (hs/server-stop! server))

(comment
 (mount/start-with-args {:config "databoard/server/config/dev.edn"}
                        #'databoard.server.config/config
                        #'databoard.server.http-handler/handler
                        #'databoard.server.http-server/server)
 (mount/stop #'databoard.server.http-server/server))
