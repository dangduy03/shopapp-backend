ALTER TABLE tokens 
ADD COLUMN expiration_date DATETIME DEFAULT NULL AFTER `expired`;
