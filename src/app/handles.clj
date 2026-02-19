(ns app.handles
  (:require [io.pedestal.log :as log]))

;; Response template
(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

;; Health check
(defn health-check
  "GET /health"
  [_request]
  {:status 200
   :body {:status "healthy"
          :timestamp (System/currentTimeMillis)}
   :headers {"Content-Type" "application/json"}})

;; Greeting
(defn greet
  "GET /greet"
  [request]
  (let [name (or (get-in request [:query-params :name]) "World")]
    {:status 200
     :body {:message (str "Hello, " name "!")
            :greeting "Welcome to Application"}}))

(defn echo
  "POST /echo - return request data"
  [request]
  {:status 200
   :body {:received {:method (:request-method request)
                     :uri (:uri request)
                     :query-params (:query-params request)
                     :body (:edn-body request
                                      (:json-body request
                                                  (:body request)))}}})

(defn simple-handler
  [ctx]
  (response 200 "Work now"))
