--
-- db_category
--
CREATE TABLE db_category (
  id        varchar(9)  NOT NULL,
  name      varchar(30) NOT NULL,
  parent_id varchar(9)  default NULL,
  PRIMARY KEY  (id),
  CONSTRAINT db_category_parent_fk FOREIGN KEY (parent_id) REFERENCES db_category (id)
);
CREATE INDEX db_cat_parent_fki ON db_category (parent_id);
--
-- db_product
--
CREATE TABLE db_product (
  ean_code     varchar(13) NOT NULL,
  name         varchar(30) NOT NULL,
  category_id  varchar(9)  NOT NULL,
  price        decimal(8,2)  NOT NULL,
  notes        varchar(256),
  description  varchar(4000),
  image        blob,
  PRIMARY KEY  (ean_code),
  CONSTRAINT db_product_category_fk FOREIGN KEY (category_id) REFERENCES db_category (id)
);
CREATE INDEX db_product_category_fki ON db_product (category_id);
--
-- db_role
--
CREATE TABLE db_role (
  name varchar(16) NOT NULL,
  PRIMARY KEY  (name)
);
--
-- db_user
--
CREATE TABLE db_user (
  id       int         NOT NULL,
  name     varchar(30) NOT NULL,
  email    varchar(50) NOT NULL,
  "password" varchar(16) NOT NULL,
  role_id  varchar(16) NOT NULL,
  "active"   smallint    default 1 NOT NULL,
  PRIMARY KEY  (id),
  CONSTRAINT db_user_role_fk FOREIGN KEY (role_id) REFERENCES db_role (name),
  constraint active_flag check ("active" in (0,1))
);
CREATE INDEX db_user_role_fki on db_user (role_id);
--
-- db_customer
--
CREATE TABLE db_customer (
  id         int          NOT NULL,
  category   char(1)      NOT NULL,
  salutation varchar(10),
  first_name varchar(30)  NOT NULL,
  last_name  varchar(30)  NOT NULL,
  birth_date date,
  PRIMARY KEY  (id),
  CONSTRAINT db_customer_user_fk FOREIGN KEY (id) REFERENCES db_user (id)
);
--
-- db_order
--
CREATE TABLE db_order (
  id          int NOT NULL,
  customer_id int NOT NULL,
  total_price     decimal(8,2)  NOT NULL,
  created_at  timestamp  NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT db_order_customer_fk FOREIGN KEY (customer_id) REFERENCES db_customer (id)
);
CREATE INDEX db_order_customer_fki on db_order (customer_id);
--
-- db_order_item
--
CREATE TABLE db_order_item (
  id              int   NOT NULL,
  order_id        int   NOT NULL,
  number_of_items int   default 1 NOT NULL,
  product_ean_code      varchar(13) NOT NULL,
  total_price     decimal(8,2)  NOT NULL,
  PRIMARY KEY  (id),
  CONSTRAINT db_order_item_order_fk FOREIGN KEY (order_id) REFERENCES db_order (id),
  CONSTRAINT db_order_item_product_fk FOREIGN KEY (product_ean_code) REFERENCES db_product (ean_code)
);
CREATE INDEX db_order_item_order_fki ON db_order_item (order_id);
CREATE INDEX db_order_item_product_fki ON db_order_item (product_ean_code);
CREATE GENERATOR seq_id_gen;