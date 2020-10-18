(ns vat-rates-http.db
  (:require [clojure.java.jdbc :as j])
  (:import (com.zaxxer.hikari HikariConfig HikariDataSource)
           (java.util Properties)))

(defonce db (atom nil))

(defn pool [opts]
  (if (empty? @db)
    (let [ds (HikariDataSource.)]
      (.setMaximumPoolSize ds 10)
      (.setDriverClassName ds "org.mariadb.jdbc.Driver")
      (.setJdbcUrl ds "jdbc:mariadb://localhost:3306/vat-rates-test")
      (.addDataSourceProperty ds "user" (System/getenv "MYSQL_USER"))
      (.addDataSourceProperty ds "password" (System/getenv "MYSQL_ROOT_PASSWORD"))

      (reset! db {:datasource ds}))
    @db))

(def q j/query)
(comment (j/query @db "show databases"))
