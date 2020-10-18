(ns vat-rates-http.core-test
  (:require [clojure.test :refer :all]
            [vat-rates-http.core :as core]
            [io.pedestal.http :as http]
            [io.pedestal.test :as http-test]
            [vat-rates-http.db :as db]
            [vat-rates-http.migrate :as migrate]
            [cheshire.core :as json]))

(def service (::http/service-fn (http/create-servlet core/service-map)))

(migrate/migrate (db/pool {}))

(def expected-rate
  (json/generate-string {:rate 0.13 :valid_since "2020-10-18T13:14:50Z"}))


(deftest
  Api-test
  (is (= expected-rate (:body (http-test/response-for service :get "/get_rate?iso2=ru"))))
  (is (= 404 (:status (http-test/response-for service :get "/get_rate?iso2=us")))))
