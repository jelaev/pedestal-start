(ns app.interceptors
  (:require [io.pedestal.interceptor :as interceptor]
            [io.pedestal.log :as log]))

;; Loggin interceptor
(def logging-interceptor
  "Loggin all response and requests"
  (interceptor/interceptor
   {:name ::logging
    :enter (fn [context]
             (let [request (:request context)]
               (log/info :msg "Incoming request"
                         :method (:request-method request)
                         :uri (:uri request)
                         :query-params (:query-params request))
               context))
    :leave (fn [context]
             (let [response (:response context)]
               (log/info :msg "Outgoing response"
                         :status (:status response))
               context))
    :error (fn [context ex-info]
             (log/error :msg "Error in interceptor chain"
                        :exception ex-info)
             (assoc context :response
                    {:status 500
                     :body {:error "Internal Server Error"
                            :message (ex-message ex-info)}}))}))

;; Request Timing interceptor
(def timing-interceptor
  "Execute time"
  (interceptor/interceptor
   {:name ::timing
    :enter (fn [context]
             (assoc context :start-time (System/nanoTime)))
    :leave (fn [context]
             (let [elapsed (- (System/nanoTime) (:start-time context))
                   elapsed-ms (/ elapsed 1000000.0)]
               (-> context
                   (assoc-in [:response :headers "X-Response-Time"]
                             (str (format "%.2f" elapsed-ms) "ms")))))}))


;; CORS Interceptor
(def cors-interceptor
  "Add CORS headers for all responses"
  (interceptor/interceptor
   {:name ::cors
    :leave (fn [context]
             (update-in context [:response :headers]
                        merge
                        {"Access-Control-Allow-Origin" "*"
                         "Access-Control-Allow-Methods" "GET, POST, PUT, PATCH, DELETE, OPTIONS"
                         "Access-Control-Allow-Headers" "Content-Type, Authorization"
                         "Access-Control-Max-Age" "86400"}))}))

;; Json interceptor
(def json-content-type-interceptor
  "Set Content-Type for JSON response (check map in body)"
  (interceptor/interceptor
   {:name ::json-content-type
    :leave (fn [context]
             (if (and (some? (:response context))
                      (map? (get-in context [:response :body])))
               (-> context
                   (assoc-in [:response :headers "Content-Type"] "application/json")
                   (update-in [:response :body] pr-str))
               context))}))
