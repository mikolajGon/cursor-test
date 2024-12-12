CREATE USER bookuser WITH PASSWORD 'bookpass';

CREATE USER useruser WITH PASSWORD 'userpass';

CREATE DATABASE bookdb;

GRANT ALL PRIVILEGES ON DATABASE bookdb TO bookuser;

\c bookdb;

GRANT ALL ON SCHEMA public TO bookuser;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON TABLES TO bookuser;

CREATE DATABASE userdb;

GRANT ALL PRIVILEGES ON DATABASE userdb TO useruser;

\c userdb;

GRANT ALL ON SCHEMA public TO useruser;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON TABLES TO useruser;