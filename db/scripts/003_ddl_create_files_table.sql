create table if not exists files(
    id   serial primary key,
    name varchar not null,
    path varchar not null unique
);