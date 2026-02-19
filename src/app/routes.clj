(ns app.routes
  (:require
   [io.pedestal.http.route.definition.table :as table]
   [app.interceptors :as interceptors]
   [app.handlers :as handlers]))


(def common-interceptors
  [interceptors/timing-interceptor
   interceptors/logging-interceptor
   interceptors/cors-interceptor])

(def api-interceptors
  (conj common-interceptors
        interceptors/json-content-type-interceptor))


;; Http routes can include interceptors
;; for live reload in dev-mode need `hanler (syntax-quote)
(def routes
  ;;Health
  #{["/health" :get (conj common-interceptors `handlers/health-check)
     :route-name :health]
    ;;Greeting
    ["/greet" :get (conj common-interceptors `handlers/greet)
     :route-name :greet]
    ["/echo" :post (conj api-interceptors `handlers/echo)
     :route-name :echo]})

