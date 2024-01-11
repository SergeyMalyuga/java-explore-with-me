DROP TABLE IF EXISTS hits;

CREATE TABLE IF NOT EXISTS hits(
id	INTEGER GENERATED ALWAYS AS IDENTITY,
app	VARCHAR(100) NOT NULL,
uri VARCHAR(100) NOT NULL,
ip	VARCHAR(15) NOT NULL,
timestamp TIMESTAMP WITHOUT TIME ZONE,
CONSTRAINT hit_id PRIMARY KEY(id)
);