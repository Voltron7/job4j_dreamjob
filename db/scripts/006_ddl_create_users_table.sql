create table if not exists users(
    id serial primary key,
    email varchar unique not null,
    name varchar not null,
    password varchar not null
);