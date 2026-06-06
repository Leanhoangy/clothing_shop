-- =====================================================================
-- Clothing Shop - MySQL schema & seed data
-- Anh nam tai: src/main/resources/static/images/<loai>/NN.jpg (6 loai, moi loai 10 anh)
-- Chay:  mysql -u root < sql/clothing_shop.sql
-- =====================================================================

CREATE DATABASE IF NOT EXISTS clothing_shop
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE clothing_shop;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS product_images;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
SET FOREIGN_KEY_CHECKS = 1;

-- ---------- Bang danh muc -----------------------------------------------------
CREATE TABLE categories (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(100) NOT NULL,
  slug        VARCHAR(100) NOT NULL UNIQUE,
  created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ---------- Bang san pham -----------------------------------------------------
CREATE TABLE products (
  id           INT AUTO_INCREMENT PRIMARY KEY,
  category_id  INT NOT NULL,
  name         VARCHAR(150) NOT NULL,
  slug         VARCHAR(150) NOT NULL UNIQUE,
  description  TEXT,
  price        DECIMAL(12,2) NOT NULL,
  stock        INT NOT NULL DEFAULT 0,
  thumbnail    VARCHAR(255),
  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_product_category
    FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB;

-- ---------- Bang anh san pham (gallery) --------------------------------------
CREATE TABLE product_images (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  product_id  INT NOT NULL,
  image_path  VARCHAR(255) NOT NULL,
  sort_order  INT NOT NULL DEFAULT 0,
  CONSTRAINT fk_image_product
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
  CONSTRAINT uq_product_image UNIQUE (product_id, image_path)
) ENGINE=InnoDB;

-- ---------- Bang nguoi dung (khach hang + admin) ----------------------------
-- Dung CREATE ... IF NOT EXISTS va KHONG drop -> chay lai file khong xoa
-- mat khach hang da dang ky. Mat khau luu duoi dang BCrypt hash (app ma hoa).
CREATE TABLE IF NOT EXISTS users (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  full_name   VARCHAR(150) NOT NULL,
  email       VARCHAR(150) NOT NULL UNIQUE,
  password    VARCHAR(255) NOT NULL,
  phone       VARCHAR(20),
  role        VARCHAR(20)  NOT NULL DEFAULT 'CUSTOMER',
  enabled     BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ---------- Gio hang (moi user 1 gio, moi dong 1 san pham) -------------------
CREATE TABLE IF NOT EXISTS cart_items (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  user_id     INT NOT NULL,
  product_id  INT NOT NULL,
  quantity    INT NOT NULL DEFAULT 1,
  created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT uq_cart_user_product UNIQUE (user_id, product_id)
) ENGINE=InnoDB;

-- ---------- Don hang ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  user_id        INT NOT NULL,
  recipient_name VARCHAR(150) NOT NULL,
  phone          VARCHAR(20)  NOT NULL,
  address        VARCHAR(255) NOT NULL,
  total_amount   DECIMAL(12,2) NOT NULL,
  status         VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- ---------- Chi tiet don hang (snapshot ten + gia luc dat) -------------------
CREATE TABLE IF NOT EXISTS order_items (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  order_id      INT NOT NULL,
  product_id    INT NOT NULL,
  product_name  VARCHAR(150) NOT NULL,
  price         DECIMAL(12,2) NOT NULL,
  quantity      INT NOT NULL,
  CONSTRAINT fk_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================================
-- Du lieu mau
-- =====================================================================

INSERT INTO categories (id, name, slug) VALUES
  (1, 'Ao',   'ao'),
  (2, 'Quan', 'quan'),
  (3, 'Giay', 'giay');

INSERT INTO products (id, category_id, name, slug, description, price, stock, thumbnail) VALUES
  (1, 1, 'Ao so mi',     'ao-so-mi',     'Ao so mi vai cotton, kieu dang basic.',        320000, 50, 'images/shirt/01.jpg'),
  (2, 1, 'Ao thun',      'ao-thun',      'Ao thun tron co dien, thoang mat.',            180000, 80, 'images/t-shirt/01.jpg'),
  (3, 2, 'Quan dai',     'quan-dai',     'Quan dai kaki form regular.',                  390000, 40, 'images/pants/01.jpg'),
  (4, 2, 'Quan short',   'quan-short',   'Quan short nam nang dong mua he.',             220000, 60, 'images/shorts/01.jpg'),
  (5, 3, 'Giay da',      'giay-da',      'Giay da nam lich su, de mem.',                 850000, 25, 'images/shoes/01.jpg'),
  (6, 3, 'Giay sneaker', 'giay-sneaker', 'Giay sneaker the thao nang dong.',             690000, 35, 'images/sneakers/01.jpg');

INSERT INTO product_images (product_id, image_path, sort_order) VALUES
  -- Ao so mi (shirt)
  (1, 'images/shirt/01.jpg', 1), (1, 'images/shirt/02.jpg', 2), (1, 'images/shirt/03.jpg', 3),
  (1, 'images/shirt/04.jpg', 4), (1, 'images/shirt/05.jpg', 5), (1, 'images/shirt/06.jpg', 6),
  (1, 'images/shirt/07.jpg', 7), (1, 'images/shirt/08.jpg', 8), (1, 'images/shirt/09.jpg', 9),
  (1, 'images/shirt/10.jpg', 10),
  -- Ao thun (t-shirt)
  (2, 'images/t-shirt/01.jpg', 1), (2, 'images/t-shirt/02.jpg', 2), (2, 'images/t-shirt/03.jpg', 3),
  (2, 'images/t-shirt/04.jpg', 4), (2, 'images/t-shirt/05.jpg', 5), (2, 'images/t-shirt/06.jpg', 6),
  (2, 'images/t-shirt/07.jpg', 7), (2, 'images/t-shirt/08.jpg', 8), (2, 'images/t-shirt/09.jpg', 9),
  (2, 'images/t-shirt/10.jpg', 10),
  -- Quan dai (pants)
  (3, 'images/pants/01.jpg', 1), (3, 'images/pants/02.jpg', 2), (3, 'images/pants/03.jpg', 3),
  (3, 'images/pants/04.jpg', 4), (3, 'images/pants/05.jpg', 5), (3, 'images/pants/06.jpg', 6),
  (3, 'images/pants/07.jpg', 7), (3, 'images/pants/08.jpg', 8), (3, 'images/pants/09.jpg', 9),
  (3, 'images/pants/10.jpg', 10),
  -- Quan short (shorts)
  (4, 'images/shorts/01.jpg', 1), (4, 'images/shorts/02.jpg', 2), (4, 'images/shorts/03.jpg', 3),
  (4, 'images/shorts/04.jpg', 4), (4, 'images/shorts/05.jpg', 5), (4, 'images/shorts/06.jpg', 6),
  (4, 'images/shorts/07.jpg', 7), (4, 'images/shorts/08.jpg', 8), (4, 'images/shorts/09.jpg', 9),
  (4, 'images/shorts/10.jpg', 10),
  -- Giay da (shoes)
  (5, 'images/shoes/01.jpg', 1), (5, 'images/shoes/02.jpg', 2), (5, 'images/shoes/03.jpg', 3),
  (5, 'images/shoes/04.jpg', 4), (5, 'images/shoes/05.jpg', 5), (5, 'images/shoes/06.jpg', 6),
  (5, 'images/shoes/07.jpg', 7), (5, 'images/shoes/08.jpg', 8), (5, 'images/shoes/09.jpg', 9),
  (5, 'images/shoes/10.jpg', 10),
  -- Giay sneaker (sneakers)
  (6, 'images/sneakers/01.jpg', 1), (6, 'images/sneakers/02.jpg', 2), (6, 'images/sneakers/03.jpg', 3),
  (6, 'images/sneakers/04.jpg', 4), (6, 'images/sneakers/05.jpg', 5), (6, 'images/sneakers/06.jpg', 6),
  (6, 'images/sneakers/07.jpg', 7), (6, 'images/sneakers/08.jpg', 8), (6, 'images/sneakers/09.jpg', 9),
  (6, 'images/sneakers/10.jpg', 10);
