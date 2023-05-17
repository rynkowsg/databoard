(ns databoard.server.api-middleware
  (:require
   [com.fulcrologic.fulcro.server.api-middleware :as fmw]))

(defn wrap-api-with-ring-req
  "It is copied from (com.fulcrologic.fulcro.server.api-middleware/wrap-api), enriched with logs.

  Wrap Fulcro API request processing. Required options are:

   - `:uri` - The URI on the server that handles the API requests.
   - `:parser` - A function `(fn [eql-query] eql-response)` that can process the query.

   IMPORTANT: You must install Fulcro's `wrap-transit-response` and
   `wrap-transit-params`, or other middleware that handles content negotiation,
   like https://github.com/metosin/muuntaja, to your list of middleware
   handlers after `wrap-api`."
  [handler {:keys [uri parser]}]
  (when-not (and (string? uri) (fn? parser))
    (throw (ex-info "Invalid parameters to `wrap-api`. :uri and :parser are required. See docstring." {})))
  (fn [request]
    ;; eliminates overhead of wrap-transit
    (if (= uri (:uri request))
      (let [;;_ (taoensso.timbre/info :request (with-out-str (clojure.pprint/pprint request)))
            env {:ring/request request}
            parser' (fn [tx] (parser env tx))]
        ;; Fulcro's middleware, like ring-transit, places the parsed request in
        ;; the request map on `:transit-params`, other ring middleware, such as
        ;; metosin/muuntaja, places the parsed request on `:body-params`.
        (fmw/handle-api-request (or (:transit-params request) (:body-params request)) parser'))
      (handler request))))
(comment
 ;; This is an enriched copy of
 com.fulcrologic.fulcro.server.api-middleware/wrap-api
 ;; It adds :ring/request into pathom env.
 ,)
