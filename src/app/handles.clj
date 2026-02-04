(ns app.handles)

;; Response template
(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

(def main-interceptor
  {:name :main-interceptor
   :enter
   (fn [context]
     (try
       (let [request (:request context)
             body (:body request)]
         (assoc context :response {:status 200
                                   :body (format "<h1>Started</h1><br/> %s" body)}))
       (catch Exception e
         (assoc context :response (response 500 (pr-str (.getMessage e) (.printStackTrace e)))))))})