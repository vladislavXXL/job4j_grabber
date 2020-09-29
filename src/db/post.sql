create table post (
    id serial primary key not null,
    name text not null,
    text text not null,
    link text unique,
    created date not null
);