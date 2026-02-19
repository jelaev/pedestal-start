(ns app.main
  (:require
   [io.pedestal.http :as http]
   [io.pedestal.http.route :as route]
   [app.routes :refer [routes routes-dev]])
  (:gen-class))

;; If need open CORS
(defn- allow-all-origins [_] true)

;; Def two maps, for develop and production
(def service-dev-map
  {::http/routes #(route/expand-routes routes-dev) ;; Add routes
   ::http/type :jetty
   ::http/allowed-origins {:creds true :allowed-origins allow-all-origins}
   ::http/port 8030
   ::http/join? false})

(def service-map
  {::http/routes (route/expand-routes routes) ;; Add routes
   ::http/type :jetty
   ::http/port 8030
   ::http/allowed-origins {:creds true :allowed-origins allow-all-origins}
   ::http/host "0.0.0.0"
   ::http/join? false})

;; Start server and add default-interceptors from pedestal.io
(defn service [service-map]
  (-> service-map
      (http/default-interceptors)
      (http/create-server)))

;; Entry poitn for start jar
(defn -main [& args]
  (http/start (service service-map
                       )))

;; All repl in development tools
;; Set atom for server
(defonce server (atom nil))

;; Start server in dev environment
(defn start-dev []
  (reset! server
          (http/start (service service-dev-map))))

;; Stop server in dev environment
(defn stop-dev []
  (http/stop @server)
  (reset! server nil))

;; Restart server
(defn restart []
  (stop-dev)
  (start-dev))
