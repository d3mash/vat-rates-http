MYSQL_ROOT_PASSWORD=password

.EXPORT_ALL_VARIABLES:
.PHONY: test

repl:
	clj -A:dev -m "tools.repl" -p 3001

test:
	clj -A:dev -m "tools.test"

start-dev:
	clj -A:dev -m "tools.repl" -p 3001 -f "vat-rates-http.core/-main"

up:
	docker-compose up -d

down:
	docker-compose down
