create table transaction
(
    id          bigserial
        primary key,
    amount      numeric(38, 2) not null,
    description varchar(255),
    status      varchar(255)
        constraint transaction_status_check
            check ((status)::text = ANY
        ((ARRAY ['PROCESSING'::character varying, 'COMPLETED'::character varying, 'FAILED'::character varying, 'PENDING_RETRY'::character varying])::text[])),
    timestamp   timestamp(6)   not null,
    tax_rule_id bigint
        constraint fkcepj7udhyangm81re144ymm67
            references tax_rule
);

alter table transaction