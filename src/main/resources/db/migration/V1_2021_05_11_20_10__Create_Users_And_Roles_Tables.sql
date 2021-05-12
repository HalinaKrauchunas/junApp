CREATE TABLE jun_db.users
(
    id              bigint AUTO_INCREMENT,
    username        varchar(80),
    password        varchar(80),
    email           varchar(80),
    locale          varchar(80),
    message_for_user varchar(256),
    last_visit      datetime,
    active          tinyint(1),
    activation_code text(256),
    PRIMARY KEY (id)
);

CREATE TABLE jun_db.roles
(
    id   bigint AUTO_INCREMENT,
    name varchar(15),
    PRIMARY KEY (id)
);

CREATE TABLE jun_db.users_roles
(
    user_id bigint,
    role_id bigint,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES jun_db.users (id),
    FOREIGN KEY (role_id) REFERENCES jun_db.roles (id)
);

insert into jun_db.roles (id, name)
values (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

insert into jun_db.users (username, password, email, locale, message_for_user, last_visit, active, activation_code)
values ('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i',
        'user@gmail.com', 'Belarus', null,
        '2008-10-23 10:37:22', 1, null);

insert into jun_db.users_roles (user_id, role_id)
values (1, 1),
       (1, 2);