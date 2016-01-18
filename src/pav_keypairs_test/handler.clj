(ns pav-keypairs-test.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
						[compojure.handler :as handler]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
						[ring.middleware.json :refer [wrap-json-body wrap-json-response]]
						[pav-keypairs-test.resource :refer [get-keys encrypt-payload decrypt-payload]]))

(defroutes app-routes
  (GET "/keys" [] get-keys)
	(POST "/encrypt" _ encrypt-payload)
	(POST "/decrypt" _ decrypt-payload)
  (route/not-found "Not Found"))

(def app
	(-> (routes app-routes)
		(wrap-json-body {:keywords? true})
		(handler/site)
		(wrap-json-response)))
