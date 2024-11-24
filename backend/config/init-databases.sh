#!/bin/sh
set -e

psql --username postgres --dbname postgres <<-EOSQL
  CREATE DATABASE platform;
  CREATE ROLE platform WITH ENCRYPTED PASSWORD 'platform' LOGIN;
  GRANT ALL PRIVILEGES ON DATABASE platform TO platform;

  CREATE DATABASE keycloak;
  CREATE ROLE keycloak WITH ENCRYPTED PASSWORD 'keycloak' LOGIN;
  GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;
EOSQL

psql --username postgres --dbname platform <<-EOSQL
  GRANT ALL ON SCHEMA public TO platform;
EOSQL

psql --username postgres --dbname keycloak <<-EOSQL
  GRANT ALL ON SCHEMA public TO keycloak;
EOSQL
