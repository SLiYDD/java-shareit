drop all objects;

create table if not exists users
(
    id    bigint primary key auto_increment not null,
    name  varchar(255)                                        not null,
    email varchar(512) unique                                 not null
);

create table if not exists items
(
    id          bigint auto_increment not null,
    name        varchar(255)                            not null,
    description varchar(1000)                           not null,
    available   boolean                                 not null,
    owner_id     bigint                                  not null references users (id),
    request_id   bigint                                  ,
    constraint pk_item primary key (id)
);

create table if not exists bookings
(
    id         bigint primary key auto_increment not null,
    start_date TIMESTAMP without time zone                         not null,
    end_date   TIMESTAMP without time zone                         not null,
    item_id    bigint                                              not null references items (id),
    booker_id  bigint                                              not null references users (id),
    status     varchar(25)
);

create table if not exists requests
(
  id bigint primary key auto_increment not null,
  description varchar(100) not null,
  requester_id bigint not null references users(id)
);

create table if not exists comments
(
    id bigint primary key auto_increment not null,
    comment_text varchar(1000) not null,
    item_id bigint not null references items(id),
    author_id bigint not null references users(id),
    created   TIMESTAMP without time zone NOT NULL
)