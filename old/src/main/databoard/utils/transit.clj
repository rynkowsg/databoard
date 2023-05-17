(ns databoard.utils.transit
  (:require
   [clojure.java.io :as io]
   [cognitect.transit :as ct])
  (:import
   (java.io ByteArrayInputStream ByteArrayOutputStream)))

(defn write-str
  ([x t] (write-str x t nil))
  ([x t opts]
   (let [baos (ByteArrayOutputStream.)
         w (ct/writer baos t opts)
         _ (ct/write w x)
         ret (.toString baos "UTF-8")]
     (.reset baos)
     ret)))
#_(write [:server/time] :json)
#_(write [:server/time] :json-verbose)
#_(write [:server/time] :msgpack)

(defn write-is
  ([x t] (write-is x t nil))
  ([x t opts]
   (let [baos (ByteArrayOutputStream.)
         w (ct/writer baos t opts)
         _ (ct/write w x)
         is (io/input-stream (.toByteArray baos))]
     (.reset baos)
     is)))
#_(-> (write->is [:server/time] :json) slurp)

(defn read-is [is t]
  (let [r (ct/reader is t)]
    (ct/read r)))
#_(-> [:server/time] (write->is :json) (read-is :json))

(defn read-str [s t]
  (read-is (ByteArrayInputStream. (.getBytes s)) t))
#_(read-str "[\"~:server/time\"]" :json)
