(ns pav-keypairs-test.encryption-manager
	(:import (java.security KeyPairGenerator KeyFactory)
					 (java.util Base64)
					 (java.security.spec X509EncodedKeySpec PKCS8EncodedKeySpec)
					 (javax.crypto Cipher)))

(defn generate-keypair
	"Retrieve keypair using a chosen algorithm"
	([algo] (generate-keypair algo 2048))
	([algo num-of-bytes]
	 (let [generator (KeyPairGenerator/getInstance algo)
				 _ (.initialize generator num-of-bytes)
				 keypair (.genKeyPair generator)
				 private-key-bytes (.getEncoded (.getPrivate keypair))
				 public-key-bytes (.getEncoded (.getPublic keypair))]
		 {:public-key  (.encodeToString (Base64/getEncoder) public-key-bytes)
			:private-key (.encodeToString (Base64/getEncoder) private-key-bytes)})))

(defn encrypt [public-key payload]
	"Use public RSA key to encrypt the payload and return as Base64 string"
	(let [factory (KeyFactory/getInstance "RSA")
				public-key-bytes (.decode (Base64/getDecoder) public-key)
				key (.generatePublic factory (X509EncodedKeySpec. public-key-bytes))
				cipher (Cipher/getInstance "RSA")]
		(.init cipher (Cipher/ENCRYPT_MODE) key)
		(.encodeToString (Base64/getEncoder) (.doFinal cipher (.getBytes payload)))))

(defn decrypt [private-key payload]
	"Use public RSA key to decrypt the payload"
	(let [factory (KeyFactory/getInstance "RSA")
				private-key-bytes (.decode (Base64/getDecoder) private-key)
				key (.generatePrivate factory (PKCS8EncodedKeySpec. private-key-bytes))
				cipher (Cipher/getInstance "RSA")]
		(.init cipher (Cipher/DECRYPT_MODE) key)
		(String. (.doFinal cipher (.decode (Base64/getDecoder) payload)))))


(comment
	(let [payload "John went to town"
				{public :public-key private :private-key} (generate-keypair "RSA" 2048)
				encrypted (encrypt public payload)
				decrypted (decrypt private encrypted)]
		(println "Encrypted " encrypted)
		(println "Decrypted " decrypted))

	(time (generate-keypair "RSA" 1024))
	(time (generate-keypair "RSA" 2048))
	(time (generate-keypair "DH" 1024))
	(time (generate-keypair "DSA" 1024)))