ALTER TABLE `order_details` 
    MODIFY COLUMN `price` DECIMAL(10, 2),
    MODIFY COLUMN `number_of_products` INT DEFAULT 1,
    MODIFY COLUMN `total_money` DECIMAL(10, 2) DEFAULT 0;