(ns pav-keypairs-test.resource
	(:require [liberator.core :refer [defresource]]
						[pav-keypairs-test.encryption-manager :refer [generate-keypair encrypt decrypt]]))


(defresource get-keys
	:allowed-methods [:get]
	:available-media-types ["application/json"]
	:handle-ok (generate-keypair "RSA" 2048))

(defresource encrypt-payload
	:allowed-methods [:post]
	:available-media-types ["application/json"]
	:post! (fn [ctx] (let [{k :public-key p :payload} (get-in ctx [:request :body])]
										 {:record (encrypt k p)}))
	:handle-created :record)

(defresource decrypt-payload
	:allowed-methods [:post]
	:available-media-types ["application/json"]
	:post! (fn [ctx] (let [{k :private-key p :payload} (get-in ctx [:request :body])]
										 {:record (decrypt k p)}))
	:handle-created :record)
