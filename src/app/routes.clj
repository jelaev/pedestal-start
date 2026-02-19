(ns app.routes
  (:require
   [app.handles :refer [main-interceptor simple-handler]]))

;; Http routes can include interceptors
(def routes
  #{["/" :get main-interceptor :route-name :main]
    ;;["/meddocument" :post meddocument-interceptor :route-name :meddocument-new]
    ;;["/simple" :post simple-interceptor :route-name :simple-interceptor-name]
    })

(def routes-dev
  #{["/" :get `main-interceptor :route-name :main]
    ["/simple" :get simple-handler]
    ;;["/meddocument" :post `meddocument-interceptor :route-name :meddocument-new]
    ;;["/simple" :post `simple-interceptor :route-name :simple-interceptor-name]
    })

