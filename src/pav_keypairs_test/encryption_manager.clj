(ns pav-keypairs-test.encryption-manager
  (:require [clojure.core.async :as async :refer [>! <!! go chan]])
	(:import (java.security KeyPairGenerator KeyFactory)
					 (java.util Base64)
					 (java.security.spec X509EncodedKeySpec PKCS8EncodedKeySpec)
					 (javax.crypto Cipher)))

(def algo "RSA")
(def num-of-bytes 2048)
(def buffer-size 100)

(defn generate-keypair []
	"Retrieve keypair using a chosen algorithm"
	(let [generator (KeyPairGenerator/getInstance algo)
				_ (.initialize generator num-of-bytes)
				keypair (.genKeyPair generator)
				private-key-bytes (.getEncoded (.getPrivate keypair))
				public-key-bytes (.getEncoded (.getPublic keypair))]
		{:public-key  (.encodeToString (Base64/getEncoder) public-key-bytes)
		 :private-key (.encodeToString (Base64/getEncoder) private-key-bytes)}))

(defn prefill-chan [keypair-chan]
  (go
    (dotimes [n buffer-size]
      (>! keypair-chan (generate-keypair)))))

(defn push-keypair [keypair-chan]
  (go
    (>! keypair-chan (generate-keypair))))

(defn pull-keypair [keypair-chan]
  (<!! keypair-chan))

(defn make-keypair-handler []
  (let [keypair-chan (chan buffer-size)]
    (prefill-chan keypair-chan)
    (fn []
      (push-keypair keypair-chan)
      (pull-keypair keypair-chan))))

(defn encrypt [public-key payload]
	"Use public RSA key to encrypt the payload and return as Base64 string"
	(let [factory (KeyFactory/getInstance algo)
				public-key-bytes (.decode (Base64/getDecoder) public-key)
				key (.generatePublic factory (X509EncodedKeySpec. public-key-bytes))
				cipher (Cipher/getInstance "RSA")]
		(.init cipher (Cipher/ENCRYPT_MODE) key)
		(.encodeToString (Base64/getEncoder) (.doFinal cipher (.getBytes payload)))))

(defn decrypt [private-key payload]
	"Use public RSA key to decrypt the payload"
	(let [factory (KeyFactory/getInstance algo)
				private-key-bytes (.decode (Base64/getDecoder) private-key)
				key (.generatePrivate factory (PKCS8EncodedKeySpec. private-key-bytes))
				cipher (Cipher/getInstance "RSA")]
		(.init cipher (Cipher/DECRYPT_MODE) key)
		(String. (.doFinal cipher (.decode (Base64/getDecoder) payload)))))
