create table if not exists cities(
    id   serial primary key,
    name varchar not null unique
);