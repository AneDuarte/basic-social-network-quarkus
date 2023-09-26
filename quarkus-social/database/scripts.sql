CREATE DATABASE quarkus-social;

CREATE TABLE "usuario" (
    id bigserial not null primary key,
    name varchar(100) not null,
    age integer not null
)

CREATE TABLE "post" (
    id bigserial not null primary key,
    post_text varchar(300) not null,
    dateTime timestamp not null,
    user_id bigint not null references usuario(id)
)