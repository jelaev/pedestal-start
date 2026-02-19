(ns app.main
  (:require
   [io.pedestal.http.jetty :as jetty]
   [io.pedestal.connector :as conn]
   [io.pedestal.environment :as env]
   [app.routes :refer [routes]])
  (:gen-class))

;; Make connector
(defn create-connector
  "Make Jetty connector"
  ([]
   (create-connector 8030))
  ([port]
   (-> (conn/default-connector-map port)
       (conn/with-default-interceptors)
       (conn/optionally-with-dev-mode-interceptors)
       (conn/with-routes routes)
       (jetty/create-connector nil))))

;; Live cycle

(defonce server (atom nil))

(defn start!
  "Run server"
  ([]
   (start! 8030))
  ([port]
   (when-let [current @server]
     (conn/stop! current))
   (let [connector (create-connector port)]
     (conn/start! connector)
     (reset! server connector)
     (println (str "Server started on http://localhost:" port))
     connector)))

(defn stop!
  "Stops server"
  []
  (when-let [current @server]
    (conn/stop! current)
    (reset! server nil)
    (println "Server stopped")))

(defn restart!
  "Restart server"
  ([]
   (restart! 8030))
  ([port]
   (stop!)
   (start! port)))

(defn -main
  [& args]
  (let [port (or (some-> args first Integer/parseInt) 8030)]
    (start! port)))
