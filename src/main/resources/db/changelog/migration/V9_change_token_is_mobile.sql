-- Nếu cột không tồn tại, thêm cột is_mobile
SET @alterStatement = IF(@columnCount = 0,
    'ALTER TABLE tokens ADD COLUMN is_mobile TINYINT(1) DEFAULT 0;',
    'SELECT "Column already exists";'
);

