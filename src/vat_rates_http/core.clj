(ns vat-rates-http.core
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [vat-rates-http.db :as db]
            [clojure.java.jdbc :as j]))

(defn respond-hello [req]
  (prn req)
  {:status 200 :body (j/query (:db req) ["show tables"])})

(def attach-db
  {:name ::attach-database
   :enter (fn [context]
            (let [conn (db/pool {})]
              (assoc-in context [:request :db] conn)))})

(def routes
  (route/expand-routes
   #{["/index" :get [attach-db #'respond-hello] :route-name :index]}))

(defonce server (atom nil))

(defn start-server []
  (when (empty? @server)
    (reset! server (http/start (http/create-server
                                {::http/routes routes
                                 ::http/type :jetty
                                 ::http/port 3000})))))

(defn -main [& args]
  (start-server))

(comment (-main))
