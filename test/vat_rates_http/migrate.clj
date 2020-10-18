(ns vat-rates-http.migrate
  (:require [vat-rates-http.db :as db]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as j]))

(defn migrate [db]
  (j/execute! db (j/drop-table-ddl :rates {:conditional? true}))
  (j/execute! db (j/create-table-ddl :rates
                      [[:id "int primary key not null auto_increment"]
                       [:country_iso2 "varchar(2) NOT NULL"],
                       [:rate "decimal(10, 2) NOT NULL"],
                       [:valid_since "datetime NOT NULL"]]))
  (j/insert! db :rates {:country_iso2 "ru" :rate 0.13 :valid_since "2020-10-18T16:14:50"}))

(comment
  (migrate (db/pool {})))
