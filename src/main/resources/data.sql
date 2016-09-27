insert into user (id, username, password, first_name, last_name, email) values (1, 'admin', 'admin', 'Admin', 'Noh', 'admin@domain.com');
insert into user (id, username, password, first_name, last_name, email) values (2, 'user', 'user', 'User', 'Noh', 'user@domain.com');

insert into role (id, name, description) values (1, 'ROLE_ADMIN', 'Administrator');
insert into role (id, name, description) values (2, 'ROLE_USER', 'User');

insert into user_role (user_id, group_id) values (1, 1);
insert into user_role (user_id, group_id) values (1, 2);
insert into user_role (user_id, group_id) values (2, 2);