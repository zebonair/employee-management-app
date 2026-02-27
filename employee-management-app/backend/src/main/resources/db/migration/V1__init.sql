-- Flyway V1: Initial schema for Department and Employee
create table if not exists department (
    id bigserial primary key,
    name varchar(255) not null unique
);

create table if not exists employee (
    id bigserial primary key,
    full_name varchar(255) not null,
    address varchar(512),
    phone varchar(64),
    email varchar(255),
    department_id bigint references department(id) on delete set null
);
