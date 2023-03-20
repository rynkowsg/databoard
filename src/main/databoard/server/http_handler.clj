(ns databoard.server.http-handler
  (:require
   [clojure.java.io :as io]
   [com.fulcrologic.fulcro.server.api-middleware :as fmw]
   [databoard.server.api-middleware :as dfmw]
   [databoard.server.pathom :as ds-parser]
   [databoard.utils.transit :as du-transit]
   [mount.core :as mount]
   [muuntaja.core :as m]
   [reitit.ring :as rr]
   [reitit.ring.middleware.muuntaja :as rrm-muuntaja]
   [reitit.ring.middleware.parameters :as rrm-parameters]
   [reitit.swagger :as rs]
   [reitit.swagger-ui :as rs-ui]
   [ring.middleware.content-type :refer [wrap-content-type]]
   [ring.middleware.not-modified :refer [wrap-not-modified]]
   [ring.util.response :as ru-response]
   [taoensso.timbre :as log]))

(def routes
  [["" {:no-doc true}
    ["/swagger.json"
     {:get {:swagger {:info {:title "my-api" :description "with reitit-ring"}}
            :handler (rs/create-swagger-handler)}}]]
   ["/app-version"
    {:get {:handler (fn [_] {:status 200 :body {:current "0.1.0"}})}}]
   ["/"
    {:get {:handler (fn [_] (ru-response/response (slurp (io/resource "public/index.html"))))}}]])

(def reitit-router
  (rr/router routes {:data {:muuntaja m/instance
                            :middleware [;; swagger feature
                                         rs/swagger-feature
                                         ;; query-params & form-params
                                         rrm-parameters/parameters-middleware
                                         ;; content-negotiation
                                         rrm-muuntaja/format-middleware]}}))

(defn def-handler []
  (rr/routes
   (rr/create-resource-handler {:root "public" :path "/"})
   (rs-ui/create-swagger-ui-handler {:path "/swagger"
                                     :config {:validatorUrl nil :operationsSorter "alpha"}})
   (-> (rr/create-default-handler)
       (dfmw/wrap-api-with-ring-req {:uri "/api"
                                     :parser (fn [env tx]
                                               (log/info "Process" tx)
                                               (ds-parser/parser env tx))})
       fmw/wrap-transit-params
       fmw/wrap-transit-response
       wrap-content-type
       wrap-not-modified)))

(defn create-handler []
  (rr/ring-handler reitit-router (def-handler)))

(mount/defstate handler
  :start
  (create-handler))

#_(comment
   ;; test GET /app-version
   (-> {:request-method :get
        :uri "/app-version"
        :headers {"content-type" "application/json"
                  "accept" "application/json"}}

       ((create-handler)) :body slurp)

   ;; test /api with [:server/time]
   (-> {:request-method :post
        :uri "/api"
        :headers {"content-type" "application/transit+json"
                  "accept" "application/transit+json",}
        :body (du-transit/write-is [:server/time] :json)}
       ((create-handler)) :body (du-transit/read-str :json)))
