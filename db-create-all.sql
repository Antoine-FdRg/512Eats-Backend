-- apply changes
create table discount_strategy (
  discount_type                 varchar(31) not null,
  id                            integer not null,
  restaurant_id                 integer,
  nb_orders_required            integer,
  discount_rate                 double,
  role                          varchar(8),
  constraint ck_discount_strategy_role check ( role in ('student','employee','manager')),
  constraint uq_discount_strategy_restaurant_id unique (restaurant_id),
  constraint pk_discount_strategy primary key (id),
  foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict
);

create table dish (
  id                            integer not null,
  name                          varchar(255),
  description                   varchar(255),
  price                         double not null,
  preparation_time              integer not null,
  picture                       varchar(255),
  constraint pk_dish primary key (id),
  foreign key (id) references restaurant (id) on delete restrict on update restrict
);

create table group_order (
  id                            integer not null,
  delivery_date_time            timestamp,
  status                        varchar(13),
  delivery_location_id          integer,
  constraint ck_group_order_status check ( status in ('created','paid','placed','delivering','completed','discount_used','canceled')),
  constraint pk_group_order primary key (id),
  foreign key (delivery_location_id) references location (id) on delete restrict on update restrict
);

create table location (
  id                            integer not null,
  street_number                 varchar(255),
  address                       varchar(255),
  city                          varchar(255),
  constraint pk_location primary key (id)
);

create table payment (
  id                            integer not null,
  amount                        double not null,
  time                          timestamp,
  constraint pk_payment primary key (id)
);

create table registered_user (
  id                            integer not null,
  name                          varchar(255),
  role                          varchar(8),
  current_order_id              integer,
  constraint ck_registered_user_role check ( role in ('student','employee','manager')),
  constraint uq_registered_user_current_order_id unique (current_order_id),
  constraint pk_registered_user primary key (id),
  foreign key (current_order_id) references sub_order (id) on delete restrict on update restrict
);

create table restaurant (
  id                            integer not null,
  name                          varchar(255),
  open                          time,
  close                         time,
  discount_strategy_id          integer,
  average_order_preparation_time integer not null,
  description                   varchar(255),
  constraint uq_restaurant_discount_strategy_id unique (discount_strategy_id),
  constraint pk_restaurant primary key (id),
  foreign key (discount_strategy_id) references discount_strategy (id) on delete restrict on update restrict
);

create table sub_order (
  order_type                    varchar(31) not null,
  id                            integer not null,
  price                         double not null,
  group_order_id                integer,
  restaurant_id                 integer,
  user_id                       integer,
  status                        varchar(13),
  placed_date                   timestamp,
  delivery_date                 timestamp,
  payment_id                    integer,
  delivery_location_id          integer,
  constraint ck_sub_order_status check ( status in ('created','paid','placed','delivering','completed','discount_used','canceled')),
  constraint uq_sub_order_payment_id unique (payment_id),
  constraint pk_sub_order primary key (id),
  foreign key (group_order_id) references group_order (id) on delete restrict on update restrict,
  foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict,
  foreign key (user_id) references registered_user (id) on delete restrict on update restrict,
  foreign key (payment_id) references payment (id) on delete restrict on update restrict,
  foreign key (delivery_location_id) references location (id) on delete restrict on update restrict
);

create table time_slot (
  id                            integer not null,
  start                         timestamp,
  production_capacity           integer not null,
  max_number_of_orders          integer not null,
  constraint pk_time_slot primary key (id),
  foreign key (id) references restaurant (id) on delete restrict on update restrict
);

