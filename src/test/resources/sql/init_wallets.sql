DELETE FROM payment;
DELETE FROM wallet;

INSERT INTO wallet(user_id, balance, version) VALUES (1, 1000, 0);
INSERT INTO wallet(user_id, balance, version) VALUES (2, 50, 0);