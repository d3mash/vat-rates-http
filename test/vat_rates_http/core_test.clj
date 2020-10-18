(ns vat-rates-http.core-test
  (:require [clojure.test :refer :all]
            [vat-rates-http.core :as core]
            [io.pedestal.http :as http]
            [io.pedestal.test :as http-test]
            [vat-rates-http.db :as db]
            [test.migrate :as migrate]))

(def service (::http/service-fn (http/create-servlet core/service-map)))

(migrate/migrate (db/pool {}))

(deftest
  Api-test
  (is (= "()" (:body (http-test/response-for service :get "/get_rate")))))
