-- The encrypted client_secret it `secret`
-- INSERT INTO oauth_client_details (client_id, client_secret, scope, authorized_grant_types, authorities, access_token_validity)
-- VALUES ('clientId', '{bcrypt}$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.', 'read,write', 'password,refresh_token,client_credentials', 'ROLE_CLIENT', 300);
  
  -- ------------------------- Client -------------------------------------------

INSERT INTO oauth_client_details
(id,client_id, resource_ids, client_secret, `scope`, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, app_name, fintech)
VALUES(1,'58a82ce582ce-865c-4054-865c-xyf12225', '1,2,3,4', '{bcrypt}$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.', NULL, 'authorization_code,password,refresh_token,client_credentials', 'https://www.of-dg.com/authenticate-success', 'ROLE_CLIENT', 300, NULL, NULL, NULL, 'Tawfeer', 'Digital World');

INSERT INTO oauth_client_details
(id,client_id, resource_ids, client_secret, `scope`, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, app_name, fintech)
VALUES(2,'6z222533-4xyf-4qw4-4ebf-56z82ce558a9', '1,2,3,4', '{bcrypt}$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.', NULL, 'authorization_code,password,refresh_token,client_credentials', 'https://www.la-dg.com/authenticate-success', 'ROLE_CLIENT', 300, NULL, NULL, NULL, 'Loan Calculator', 'Digital World');

INSERT INTO oauth_client_details
(id,client_id, resource_ids, client_secret, `scope`, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, app_name, fintech)
VALUES(3,'aa3ce24a-6cd5-4d51-891d-dc514af389c2', '1,2,3,4', '{bcrypt}$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.', NULL, 'authorization_code,password,refresh_token,client_credentials', 'https://www.info-tech.com/app-callback', 'ROLE_CLIENT', 300, NULL, NULL, NULL, 'Digital Payment', 'Info-Tech');

-- 5e112533-4ebf-4054-865c-41e82ce557c2
  

-- The encrypted password is `pass`
INSERT INTO users (id, username, password, enabled) VALUES (1, 'user', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', 1);
INSERT INTO users (id, username, password, enabled) VALUES (2, 'guest', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', 1);

INSERT INTO authorities (username, authority) VALUES ('user', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('guest', 'ROLE_GUEST');

-- ------------------------- account_type ----------------------------

INSERT INTO  account_type (id, code, name) VALUES (1,'SavingsAccounts','Savings Accounts');
INSERT INTO  account_type (id, code, name) VALUES (2,'CheckingAccounts','Checking Accounts');
INSERT INTO  account_type (id, code, name) VALUES (3,'MoneyMarketAccounts','Money Market Accounts');
INSERT INTO  account_type (id, code, name) VALUES (4,'CDs','Certificates of Deposit (CDs)');
INSERT INTO  account_type (id, code, name) VALUES (5,'RetirementAccounts','Retirement Accounts');

-- ------------------------- Permission ----------------------------
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(1, '2020-08-17 18:47:52', 'Ability to read basic account information', '2020-08-17 18:47:52', 'Read Accounts Basic', 'ReadAccountsBasic');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(2, '2020-08-17 18:47:52', 'Ability to read account identification details', '2020-08-17 18:47:52', 'Read Accounts Detail', 'ReadAccountsDetail');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(3, '2020-08-17 18:47:52', 'Ability to read all balance information', '2020-08-17 18:47:52', 'Read Balances', 'ReadBalances');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(4, '2020-08-17 18:47:52', 'Ability to read basic beneficiary details', '2020-08-17 18:47:52', 'Read Beneficiaries Basic', 'ReadBeneficiariesBasic');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(5, '2020-08-17 18:47:52', 'Ability to read account identification details for the beneficiary', '2020-08-17 18:47:52', 'Read Beneficiaries Detail', 'ReadBeneficiariesDetail');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(6, '2020-08-17 18:47:52', 'Ability to read all direct debit information', '2020-08-17 18:47:52', 'Read DirectDebits', 'ReadDirectDebits');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(7, '2020-08-17 18:47:52', '', '2020-08-17 18:47:52', 'Read Offers', 'ReadOffers');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(8, '2020-08-17 18:47:52', 'Request to access PAN in the clear across the available endpoints. BOI will return a masked PAN.', '2020-08-17 18:47:52', 'Read PAN', 'ReadPAN');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(9, '2020-08-17 18:47:52', '', '2020-08-17 18:47:52', 'Read Party', 'ReadParty');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(10, '2020-08-17 18:47:52', '', '2020-08-17 18:47:52', 'Read PartyPSU', 'ReadPartyPSU');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(11, '2020-08-17 18:47:52', 'Ability to read all product information relating to the account', '2020-08-17 18:47:52', 'Read Products', 'ReadProducts');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(12, '2020-08-17 18:47:52', 'Ability to read basic scheduled payments details', '2020-08-17 18:47:52', 'Read Scheduled Payments Basic', 'ReadScheduledPaymentsBasic');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(13, '2020-08-17 18:47:52', 'Ability to read additional elements about the scheduled payments', '2020-08-17 18:47:52', 'Read Scheduled Payments Detail', 'ReadScheduledPaymentsDetail');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(14, '2020-08-17 18:47:52', 'Ability to read basic standing order information', '2020-08-17 18:47:52', 'Read Standing Orders Basic', 'ReadStandingOrdersBasic');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(15, '2020-08-17 18:47:52', 'Ability to read account identification details for beneficiary of the standing order', '2020-08-17 18:47:52', 'Read Standing Orders Detail', 'ReadStandingOrdersDetail');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(16, '2020-08-17 18:47:52', 'Ability to read basic statement details', '2020-08-17 18:47:52', 'Read Statements Basic', 'ReadStatementsBasic');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(17, '2020-08-17 18:47:52', 'Ability to read statement data elements which may leak other information about the account', '2020-08-17 18:47:52', 'Read Statements Detail', 'ReadStatementsDetail');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(18, '2020-08-17 18:47:52', 'Ability to read basic transaction information', '2020-08-17 18:47:52', 'Read Transactions Basic', 'ReadTransactionsBasic');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(19, '2020-08-17 18:47:52', 'Ability to read only credit transactions', '2020-08-17 18:47:52', 'Read Transactions Credits', 'ReadTransactionsCredits');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(20, '2020-08-17 18:47:52', 'Ability to read only debit transactions', '2020-08-17 18:47:52', 'Read Transactions Debits', 'ReadTransactionsDebits');
INSERT INTO permission(id, created_at, description, last_updated_at, name, code) VALUES(21, '2020-08-17 18:47:52', 'Ability to read transaction data elements which may hold silent party details', '2020-08-17 18:47:52', 'Read Transactions Detail', 'ReadTransactionsDetail');

-- ------------------------- segment ----------------------------
INSERT INTO  segment (id, code, name) VALUES (1,'A','GOLDEN');
INSERT INTO  segment (id, code, name) VALUES (2,'B','SILVER');
INSERT INTO  segment (id, code, name) VALUES (3,'C','PLATINUM');

-- ------------------------- segment_permission ----------------------------

INSERT INTO segment_permission VALUES (1,1);
INSERT INTO segment_permission VALUES (2,1);
INSERT INTO segment_permission VALUES (3,1);
INSERT INTO segment_permission VALUES (1,2);
INSERT INTO segment_permission VALUES (2,2);
INSERT INTO segment_permission VALUES (1,3);

-- ------------------------- USER ----------------------------

-- password : pass
-- confirm : mobile number
INSERT INTO user
(id, created_at, description, last_updated_at, status, name, password, user_name, segment, first_name, last_name, mobile)
VALUES(1, '2020-08-17 18:47:52', '', '2020-08-17 18:47:52', '', 'Mohamed Mahmoud Abo Senna', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', '11112222', 1, 'Mohamed', 'Mahmoud', '9667000');

INSERT INTO user
(id, created_at, description, last_updated_at, status, name, password, user_name, segment, first_name, last_name, mobile)
VALUES(2, '2020-08-17 18:47:52', '', '2020-08-17 18:47:52', '', 'Ezzat Eissa', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', '33334444', 2, 'Ezzat', 'Eissa', '9668000');

INSERT INTO user
(id, created_at, description, last_updated_at, status, name, password, user_name, segment, first_name, last_name, mobile)
VALUES(3, '2020-08-17 18:47:52', '', '2020-08-17 18:47:52', '', 'Mohamed Abdallah', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', '55556666', 2, 'Mohamed', 'Abdallah', '9669000');
-- ------------------------- ACCOUNT ----------------------------
INSERT INTO account
(id, created_at, description, last_updated_at, status, account_number, user, account_type)
VALUES(1, '2020-08-17 18:47:52', '', '2020-08-17 18:47:52', 'ACTIVE', '9568254111', 1, 1);

INSERT INTO account
(id, created_at, description, last_updated_at, status, account_number, user, account_type)
VALUES(2, '2020-08-17 18:47:52', '', '2020-08-17 18:47:52', 'ACTIVE', '9567774111', 1, 2);

INSERT INTO account
(id, created_at, description, last_updated_at, status, account_number, user, account_type)
VALUES(3, '2020-08-17 18:47:52', '', '2020-08-17 18:47:52', 'NOT ACTIVE', '9657774111', 2, 3);

INSERT INTO account
(id, created_at, description, last_updated_at, status, account_number, user, account_type)
VALUES(4, '2020-08-17 18:47:52', '', '2020-08-17 18:47:52', 'ACTIVE', '9657774849', 2, 4);


INSERT INTO account
(id, created_at, description, last_updated_at, status, account_number, user, account_type)
VALUES(5, '2020-08-17 18:47:52', '', '2020-08-17 18:47:52', 'NOT ACTIVE', '9657774849', 3, 5);








-- ------------------------- Third Party ----------------------------

-- INSERT INTO app VALUES (1,'2020-08-17 18:47:52','',1,0,'2020-08-17 18:47:52','whatsapp','TP-01');
-- INSERT INTO app VALUES (2,'2020-08-17 18:47:52','',1,0,'2020-08-17 18:47:52','FB','TP-02');
-- ------------------------- Consent ----------------------------

INSERT INTO consent (id, created_at, description, last_updated_at, status, expiration_date_time, transaction_from_date_time, transaction_to_date_time, account, app)
VALUES (1,'2020-08-17 18:47:52','','2020-08-17 18:47:52','VALID','2020-09-17 18:47:52','2020-08-17 18:47:52','2020-09-17 18:47:52',1,1);
INSERT INTO consent (id, created_at, description, last_updated_at, status, expiration_date_time, transaction_from_date_time, transaction_to_date_time, account, app)
VALUES (2,'2020-08-17 18:47:52','','2020-08-17 18:47:52','NOT VALID','2020-09-17 18:47:52','2020-08-17 18:47:52','2020-09-17 18:47:52',3,2);
-- ------------------------- Consent_Permission ----------------------------

INSERT INTO consent_permission VALUES (1,1);
INSERT INTO consent_permission VALUES (2,1);
INSERT INTO consent_permission VALUES (1,2);
INSERT INTO consent_permission VALUES (2,2);
INSERT INTO consent_permission VALUES (1,3);
INSERT INTO consent_permission VALUES (2,3);

-- ------------------------- Account_ThirdParty ----------------------------

INSERT INTO account_app VALUES (1,1);
INSERT INTO account_app VALUES (3,2);

-- ------------------------- thirdParty_permission ----------------------------
INSERT INTO app_permission VALUES (1,1);
INSERT INTO app_permission VALUES (2,1);
INSERT INTO app_permission VALUES (3,1);
INSERT INTO app_permission VALUES (1,2);
INSERT INTO app_permission VALUES (2,2);
INSERT INTO app_permission VALUES (1,3);

