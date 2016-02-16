(defproject pav-keypairs-test "0.1.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
								 [liberator "0.13"]
								 [cheshire "5.5.0"]
								 [ring/ring-json "0.3.1" :exclusions [ring/ring-core]]]
  :plugins [[lein-ring "0.9.7"]
						[com.jakemccrary/lein-test-refresh "0.11.0"]]
  :ring {:handler pav-keypairs-test.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
