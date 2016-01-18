(ns pav-keypairs-test.encryption-test
  (:require [clojure.test :refer :all]
						[ring.mock.request :refer [request body content-type header]]
						[cheshire.core :refer [parse-string generate-string]]
            [pav-keypairs-test.handler :refer :all]))


(defn req [method url payload]
	(app (content-type (request method url (generate-string payload)) "application/json")))

(deftest test-app

	(testing "Verify the new key pair is different than the last requested one."
		(let [keypair1 (-> (req :get "/keys" {}) :body (parse-string true))
					keypair2 (-> (req :get "/keys" {}) :body (parse-string true))]
			(is (false? (= keypair1 keypair2)))))

	(testing "Peformance based test, Log time taken to encrypt & decrypt the same payload
						using fresh public/private RSA 2048 key with each iteration"
		(time
			(do
				(println "Time Taken to encrypt & decrypt the same payload with different keys")
				(dotimes [_ 10]
					(let [payload "John went to town"
								body (-> (req :get "/keys" {}) :body (parse-string true))
								encryption-response (-> (req :post "/encrypt" {:public-key (:public-key body) :payload payload})
																				:body)
								decrypted-response (-> (req :post "/decrypt" {:private-key (:private-key body) :payload encryption-response})
																		 	 :body)]
						(is (= payload decrypted-response))))))))
