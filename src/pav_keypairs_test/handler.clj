(ns pav-keypairs-test.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
						[pav-keypairs-test.resource :refer [get-keys
																								encrypt
																								decrypt]]))

(defroutes app-routes
  (GET "/keys" [] get-keys)
	(POST "/encrypt" _ encrypt)
	(POST "/decrypt" _ decrypt)
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
