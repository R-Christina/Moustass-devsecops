create database moustass;

create table User(
    id_user int auto_increment primary key,
    user_name varchar(255)not null,
    email varchar(255) not null unique,
    password_hash text not null,
    post varchar(255) not null,
    public_key text,
    is_admin int default 1,
    force_change int default 1,
    created_at datetime not null,
    updated_at datetime
);

create table signature(
    id_ignature int auto_increment primary key,
    id_user int not null,
    file_name varchar(255) not null,
    file_hash text,
    signatur text,
    public_key text,
    signed_at datetime,
    foreign key (id_user) references User(id_user)
);

create table logs(
    id_log int auto_increment primary key,
    name_user varchar(255)not null,
    act text not null,
    date_time datetime not null,
    result text
);