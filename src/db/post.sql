create table post (
    id serial primary key not null,
    name text not null,
    author varchar(250) not null,
    text text not null,
    link text unique not null,
    created timestamp not null
);