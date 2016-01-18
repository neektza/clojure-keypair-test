(ns pav-keypairs-test.resource
	(:require [liberator.core :refer [defresource]]
						[pav-keypairs-test.encryption-manager :refer [generate-keypair]]))


(defresource get-keys
	:allowed-methods [:get]
	:available-media-types ["application/json"]
	:handle-ok (generate-keypair "RSA" 2048))

(defresource encrypt
	:allowed-methods [:get]
	:available-media-types ["application/json"])

(defresource decrypt
	:allowed-methods [:get]
	:available-media-types ["application/json"])
