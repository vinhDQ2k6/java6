create table
  users (
    username varchar(50) not null,
    password varchar(100) not null,
    enabled boolean not null,
    primary key (username)
  );

create table
  roles (
    role_id char(10) not null,
    role_name varchar(50) not null,
    primary key (role_id)
  );

create table
  user_roles (
    username varchar(50) not null,
    roles char(10) not null,
    primary key (username, roles),
    foreign key (username) references users (username),
    foreign key (roles) references roles (role_id)
  );

insert into
  users (username, password, enabled)
values
  ('user@example.com', '{noop}123', 1),
  ('admin@example.com', '{noop}123', 1),
  ('both@example.com', '{noop}123', 1);

insert into
  roles (role_id, role_name)
values
  ('ROLE_USER', 'Nhân viên'),
  ('ROLE_ADMIN', 'Quản lý');

insert into
  user_roles (username, roles)
values
  ('user@example.com', 'ROLE_USER'),
  ('admin@example.com', 'ROLE_ADMIN'),
  ('both@example.com', 'ROLE_USER'),
  ('both@example.com', 'ROLE_ADMIN');