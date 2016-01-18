## Description

This is a very trivial example of public/private key encryption using java.security API.  

The following Compojure API has three endpoints.

- /keys Used to retrieve a new RSA 2048 private & public key
- /encrypt Takes a public key and payload and encrypts the payload
- /decrypt Takes the private key and payload and decrypts the payload

To see how to interact with the API. See pav-keypairs-test.encryption-test.

## Task

Your task is to optimise the creation of the public/private keys in the pav-keypairs-test.encryption-manager namespace.  
Creating a new key pair with every request is an expensive operation but for this particular scenario they can be pre-computed 
upfront and made available for quick retrieval.

The implementation detail is up to you, free to use external libraries to solve this issue.  

We want to see a significant performance improvement when running the performance based test in pav-keypairs-test.encryption-test.


### Restrictions

- The same key pair should never be issued twice
    
## Run performance based test

    lein test
    lein test-refresh ;;nice for TDD approach

## License

Copyright © 2016 FIXME
