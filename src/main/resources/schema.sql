create table if not exists users
(
    id        bigserial
    constraint users_pk
    primary key,
    username  varchar(255) not null,
    full_name varchar(255) not null,
    password  varchar(255) not null,
    status    integer      not null
    );

alter table users
    owner to postgres;

create unique index if not exists users_username_uindex
    on users (username);

create table if not exists roles
(
    id   serial
    constraint roles_pk
    primary key,
    name varchar(255) not null
    );

alter table roles
    owner to postgres;

create unique index if not exists roles_name_uindex
    on roles (name);

create table if not exists users_roles
(
    id       serial
    constraint users_roles_pk
    primary key,
    user_id  bigint  not null
    constraint users_roles_users_id_fk
    references users,
    role_id integer not null
    constraint users_roles_roles_id_fk
    references roles
);

alter table users_roles
    owner to postgres;

create unique index if not exists users_roles_id_uindex
    on users_roles (id);

create table if not exists user_tokens
(
    id            bigserial
    constraint user_tokens_pk
    primary key,
    access_token  varchar(255),
    refresh_token varchar(255),
    user_id       bigint
    constraint user_tokens_users_id_fk
    references users
    );

alter table user_tokens
    owner to postgres;

create unique index if not exists user_tokens_id_uindex
    on user_tokens (id);

create table if not exists addresses
(
    id          integer          not null
    constraint addresses_pk
    primary key,
    country     varchar(255)     not null,
    city        varchar(255)     not null,
    latitude    double precision not null,
    longitude   double precision not null,
    postal_code varchar(255),
    phone       varchar(255),
    description varchar(255),
    customer_id bigint
    constraint addresses_users_id_fk
    references users
    );

alter table addresses
    owner to postgres;

create unique index if not exists addresses_id_uindex
    on addresses (id);

create table if not exists orders
(
    id bigserial
    constraint orders_pk
    primary key,
    description varchar(255),
    status integer not null,
    courier_id bigint
    constraint orders_users_id_fk_2
    references users,
    customer_id bigint not null
    constraint orders_users_id_fk
    references users,
    order_date timestamp default now(),
    delivery_price double precision,
    delivery_weight double precision,
    address_from_id integer not null
    constraint orders_addresses_id_fk
    references addresses,
    address_to_id integer not null
    constraint orders_addresses_id_fk_2
    references addresses
    );

alter table orders owner to postgres;

create unique index if not exists orders_id_uindex
    on orders (id);

create table if not exists reviews
(
    id       bigserial
    constraint reviews_pk
    primary key,
    review   varchar(255),
    rating   integer not null,
    order_id bigint  not null
    constraint reviews_orders_id_fk
    references orders
    );

alter table reviews
    owner to postgres;

create unique index if not exists reviews_id_uindex
    on reviews (id);

-- roles
INSERT INTO roles (name)
VALUES ('CUSTOMER');
INSERT INTO roles (name)
VALUES ('COURIER');
INSERT INTO roles (name)
VALUES ('ADMIN');

-- admin user
INSERT INTO users (username, full_name, password, status)
VALUES ('admin@admin.az', 'ADMIN ADMIN', '$2a$10$kBkGEu.4VaZYe/Hu4ptS1OaI6pZ4Zc/Z/p.DRhV64tLfoX7slMBQK', 1);
-- password: admin123456

-- admin user with admin role
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 3);