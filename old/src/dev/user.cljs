(ns user)

(comment
 :cljs/quit
 (js/console.log {:msg "Console from log" :data {:keyword-key :keyword-value "string key" "string value"}})
 (js/alert "Message from alert"))

(defn init []
  (println "Nothing to do. Probably wrong REPL."))

#_ (init)
