# Users schema

# --- !Ups

CREATE TABLE STORIES (
    id bigint(20) NOT NULL,
    title varchar(255) NOT NULL,
    phase varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE STORIES;