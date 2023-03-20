(ns databoard.server.main
  (:require
   [mount.core :as mount]))

;; This is a separate file for the uberjar only. We control the server in dev mode from src/dev/user.clj
(defn -main [& _args]
  (mount/start-with-args {:config "databoard/server/config/prod.edn"}))
