(ns pav-keypairs-test.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
						[cheshire.core :refer [parse-string]]
            [pav-keypairs-test.handler :refer :all]))



(deftest test-app
	(testing "Peformance based test, Log time taken to encrypt & decrypt payloads
						using fresh public/private RSA 2048 key with each iteration"
		(time
			(dotimes [_ 10]
				(let [payload "John went to town"
							body (-> (app (mock/request :get "/keys")) :body (parse-string true))]
					(println public-key private-key))))))
