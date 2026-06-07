# MoPo — Clothing Shop

Website bán quần áo demo, xây bằng **Spring Boot + Thymeleaf + MySQL**.
Giao diện tham khảo phong cách yame.vn: trang chủ, chi tiết sản phẩm, giỏ hàng, đặt hàng, đăng nhập/đăng ký.

## Công nghệ
- Java 21, Spring Boot 4 (Web MVC, Data JPA, Security, Thymeleaf, Validation)
- MySQL 8/9
- Lombok, BCrypt

## Tính năng
- Trang chủ + chi tiết sản phẩm (ảnh gallery), render bằng Thymeleaf
- REST API `GET /api/products`, `GET /api/products/{id}` (trả JSON)
- Đăng ký / đăng nhập / đăng xuất (Spring Security, mật khẩu mã hoá BCrypt)
- Giỏ hàng: thêm / sửa số lượng / xoá
- Đặt hàng → tạo đơn, xem lịch sử & chi tiết đơn
- Phân quyền: khách (CUSTOMER) và quản trị (ADMIN)

## Cấu trúc dữ liệu (MySQL)
`categories`, `products`, `product_images`, `users`, `cart_items`, `orders`, `order_items`
— xem chi tiết trong [sql/clothing_shop.sql](sql/clothing_shop.sql).

## Chạy thử

### 1. Tạo database
Đảm bảo MySQL đang chạy, rồi import:
```bash
mysql -u root < sql/clothing_shop.sql
```

### 2. Cấu hình kết nối (nếu MySQL có mật khẩu)
Mặc định dùng user `root`, không mật khẩu. Nếu khác, đặt biến môi trường
(không sửa thẳng vào file để tránh commit nhầm mật khẩu):
```bash
export DB_USERNAME=root
export DB_PASSWORD=matkhau_cua_ban
```

### 3. Chạy app
```bash
./mvnw spring-boot:run
```
Mở http://localhost:8080

## Tài khoản demo (tự tạo khi khởi động)
| Vai trò | Email | Mật khẩu |
|---|---|---|
| Khách | `khach@huyrc.vn` | `123456` |
| Admin | `admin@huyrc.vn` | `admin123` |

## Định hướng phát triển
Xem [TODO.md](TODO.md) — lọc theo danh mục, thông báo thêm giỏ, tìm kiếm sản phẩm, trang admin...