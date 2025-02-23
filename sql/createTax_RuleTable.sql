create table tax_rule
(
    id          bigserial
        primary key,
    active      boolean        not null,
    description varchar(255),
    rate        numeric(38, 2) not null
);

alter table tax_rule
    owner to "citiUser";