(ns vat-rates-http.db
  (:require [clojure.java.jdbc :as j])
  (:import (com.zaxxer.hikari HikariConfig HikariDataSource)
           (java.util Properties)))

(defn create-pool [opts]
  (let [ds (HikariDataSource.)]
    (.setMaximumPoolSize ds 10)
    (.setDriverClassName ds "org.mariadb.jdbc.Driver")
    (.setJdbcUrl ds "jdbc:mariadb://localhost:3306/vat_rates_test")
    (.addDataSourceProperty ds "user" (System/getenv "MYSQL_USER"))
    (.addDataSourceProperty ds "password" (System/getenv "MYSQL_ROOT_PASSWORD"))

    {:datasource ds}))

(def db
  (create-pool {}))

(j/query db
         ["show tables"])
