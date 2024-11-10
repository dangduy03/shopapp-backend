-- Kiểm tra xem cột is_mobile đã tồn tại chưa
SET @columnCount = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'tokens'
      AND TABLE_SCHEMA = 'ShopApp'
      AND COLUMN_NAME = 'is_mobile'
);


