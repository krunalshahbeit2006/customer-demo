create table CUSTOMER
(
    ID               integer          not null,
    UNIQUE_ID        varchar(10 char) not null,
    NAME             varchar(50 char) not null,
    EMAIL            varchar(50 char) not null,
    TELEPHONE        varchar(15 char) not null,
    INSERT_TIMESTAMP timestamp,
    UPDATE_TIMESTAMP timestamp
);

alter table CUSTOMER
    add constraint CUSTOMER_pk
        primary key (ID);

alter table CUSTOMER
    add constraint CUSTOMER_uk
        unique (UNIQUE_ID);

create table CUSTOMER_REV
(
    REV      integer      not null,
    REVTSTMP bigint,
    USERNAME varchar(255) not null,
    primary key (REV)
);

create table CUSTOMER_AUD
(
    ID               integer          not null,
    REV              integer          not null,
    REVTYPE          smallint,
    UNIQUE_ID        varchar(10 char) not null,
    NAME             varchar(50 char) not null,
    EMAIL            varchar(50 char) not null,
    TELEPHONE        varchar(15 char) not null,
    INSERT_TIMESTAMP timestamp,
    UPDATE_TIMESTAMP timestamp,
    primary key (ID, REV)
);

alter table CUSTOMER_AUD
    add constraint CUSTOMER_AUD_REV_fk
        foreign key (REV)
            references CUSTOMER_REV;


create sequence CUSTOMER_ID_SEQ nocache;
create sequence CUSTOMER_UNIQUE_ID_SEQ nocache;
