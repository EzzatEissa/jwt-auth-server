CREATE TABLE IF NOT EXISTS oauth_client_details (
  id INT AUTO_INCREMENT PRIMARY KEY,
  client_id VARCHAR(256),
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256) NOT NULL,
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4000),
  autoapprove VARCHAR(256),
  status VARCHAR(256),
  description VARCHAR(256),
  created_at datetime DEFAULT NULL,
  last_updated_at datetime DEFAULT NULL,
  app_name VARCHAR(256),
  fintech VARCHAR(256),
  UNIQUE KEY unique_client_id(client_id)
);


CREATE TABLE IF NOT EXISTS oauth_client_token (
  token_id VARCHAR(256),
  token BLOB,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS oauth_access_token (
  token_id VARCHAR(256),
  token BLOB,
  authentication_id VARCHAR(256),
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication BLOB,
  refresh_token VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
  token_id VARCHAR(256),
  token BLOB,
  authentication BLOB
);

CREATE TABLE IF NOT EXISTS oauth_code (
  code VARCHAR(256), authentication BLOB
);

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(256) NOT NULL,
  password VARCHAR(256) NOT NULL,
  enabled TINYINT(1),
  UNIQUE KEY unique_username(username)
);

CREATE TABLE IF NOT EXISTS authorities (
  username VARCHAR(256) NOT NULL,
  authority VARCHAR(256) NOT NULL,
  PRIMARY KEY(username, authority)
);

CREATE TABLE IF NOT EXISTS permission (
  id bigint(20) NOT NULL auto_increment,
  created_at datetime DEFAULT NULL,
  description text,
  status VARCHAR(256),
  last_updated_at datetime DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  code varchar(255) DEFAULT NULL,
  UNIQUE KEY unique_code(code),
  UNIQUE KEY unique_name(name),
  PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS segment (
  id bigint(20) NOT NULL auto_increment,
  created_at datetime DEFAULT NULL,
  description text,
  status VARCHAR(256),
  last_updated_at datetime DEFAULT NULL,
  code varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS account_type (
  id bigint(20) NOT NULL auto_increment,
  created_at datetime DEFAULT NULL,
  description text,
  status VARCHAR(256),
  last_updated_at datetime DEFAULT NULL,
  code varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS segment_permission (
  segment_id bigint(20) NOT NULL,
  permission_id bigint(20) NOT NULL,
  PRIMARY KEY (segment_id,permission_id),
  FOREIGN KEY (permission_id) REFERENCES permission (id),
  FOREIGN KEY (segment_id) REFERENCES segment (id)
);


CREATE TABLE IF NOT EXISTS app_permission (
  app_id bigint(20) NOT NULL,
  permission_id bigint(20) NOT NULL,
  PRIMARY KEY (app_id,permission_id),
  FOREIGN KEY (permission_id) REFERENCES permission (id),
  FOREIGN KEY (app_id) REFERENCES oauth_client_details (id)
);

CREATE TABLE IF NOT EXISTS user (
  id bigint(20) NOT NULL auto_increment,
  created_at datetime DEFAULT NULL,
  description text,
  status VARCHAR(256),
  last_updated_at datetime DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  user_name varchar(255) DEFAULT NULL,
  segment bigint(20) DEFAULT NULL,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  mobile varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (segment) REFERENCES segment (id)
);

CREATE TABLE IF NOT EXISTS account (
  id bigint(20) NOT NULL auto_increment,
  created_at datetime DEFAULT NULL,
  description text,
  status varchar(255) DEFAULT NULL,
  last_updated_at datetime DEFAULT NULL,
  account_number varchar(255) DEFAULT NULL,
  user bigint(20) DEFAULT NULL,
  account_type bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user) REFERENCES user (id)
);

CREATE TABLE IF NOT EXISTS consent (
  id bigint(20) NOT NULL auto_increment,
  created_at datetime DEFAULT NULL,
  description text,
  last_updated_at datetime DEFAULT NULL,
  expiration_date_time datetime DEFAULT NULL,
  status varchar(255) DEFAULT NULL,
  transaction_from_date_time datetime DEFAULT NULL,
  transaction_to_date_time datetime DEFAULT NULL,
  account bigint(20) DEFAULT NULL,
  app bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (app) REFERENCES oauth_client_details (id),
  FOREIGN KEY (account) REFERENCES account (id),
  UNIQUE KEY app_account_constains(account, app)
);

CREATE TABLE IF NOT EXISTS consent_permission (
  consent_id bigint(20) NOT NULL,
  permission_id bigint(20) NOT NULL,
  PRIMARY KEY (consent_id,permission_id),
  FOREIGN KEY (permission_id) REFERENCES permission (id),
  FOREIGN KEY (consent_id) REFERENCES consent (id)
);

CREATE TABLE IF NOT EXISTS account_app (
  account_id bigint(20) NOT NULL,
  app_id bigint(20) NOT NULL,
  PRIMARY KEY (account_id,app_id),
  FOREIGN KEY (account_id) REFERENCES account (id),
  FOREIGN KEY (app_id) REFERENCES oauth_client_details (id)
);
