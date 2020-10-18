(ns vat-rates-http.core
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [vat-rates-http.db :as db]
            [clojure.java.jdbc :as j]
            [honeysql.core :as honey]
            [cheshire.core :as json]))

(defn get-rate [req]
  (let [country (get-in req [:query-params :iso2])
        query {:select [:rate :valid_since]
               :from [:rates]
               :where [:= :country_iso2 country]}]
    (if-let [rates (->> (honey/format query)
                        (j/query (:db req))
                        not-empty)]
      {:body (json/generate-string (first rates))
       :status 200}
      {:status 404})))

(def attach-db
  {:name ::attach-database
   :enter (fn [context]
            (let [conn (db/pool {})]
              (assoc-in context [:request :db] conn)))})

(def routes
  (route/expand-routes
   #{["/get_rate" :get [attach-db #'get-rate] :route-name :index]}))

(defonce server (atom nil))

(def service-map
  {::http/routes (fn [] (deref #'routes))
    ::http/type :jetty
    ::http/port 3000})

(defn start-server []
  (when (empty? @server)
    (reset! server (http/start (http/create-server service-map)))))

(defn -main [& args]
  (start-server))

(comment (-main))
