# Clothing Shop

Ứng dụng web bán quần áo demo được xây dựng bằng Spring Boot + Thymeleaf + MySQL. Dự án cung cấp chức năng cửa hàng trực tuyến cơ bản gồm trang chủ, trang sản phẩm, giỏ hàng, thanh toán, quản lý đơn hàng và phân quyền ADMIN/CUSTOMER.

## Tổng quan
- Ngôn ngữ: Java 21
- Framework: Spring Boot 4
- Giao diện: Thymeleaf
- Cơ sở dữ liệu: MySQL
- CSS: Tailwind CSS cho phần admin
- Bảo mật: Spring Security + BCrypt

## Tính năng chính
- Trang chủ hiển thị các sản phẩm theo danh mục
- Trang chi tiết sản phẩm với ảnh, mô tả, giá và tùy chọn thêm vào giỏ
- Giỏ hàng: thêm sản phẩm, cập nhật số lượng, xóa sản phẩm
- Quy trình đặt hàng và lưu lịch sử đơn hàng
- Quản lý người dùng và phân quyền:
  - `CUSTOMER` xem mua hàng, giỏ hàng và đơn hàng của mình
  - `ADMIN` quản lý sản phẩm, người dùng, đơn hàng và báo cáo
- REST API cơ bản:
  - `GET /api/products`
  - `GET /api/products/{id}`

## Kiến trúc dự án
- `src/main/java`: mã nguồn Java
  - `controller`: xử lý request web và REST API
  - `service`: logic nghiệp vụ
  - `repository`: truy vấn dữ liệu với Spring Data JPA
  - `entity`: lớp thực thể dữ liệu
  - `dto`: đối tượng truyền dữ liệu giữa client và server
  - `security`: cấu hình bảo mật Spring Security
- `src/main/resources/templates`: trang HTML Thymeleaf
- `src/main/resources/static`: tệp CSS, JS, ảnh tĩnh
- `sql/clothing_shop.sql`: cấu trúc và dữ liệu khởi tạo MySQL

## Cấu hình kết nối MySQL
Tệp cấu hình: `src/main/resources/application.properties`

Giá trị mặc định sử dụng:
- URL: `jdbc:mysql://localhost:3306/clothing_shop?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&characterEncoding=UTF-8`
- Username: `root`
- Password: ` ` (trống)

Nếu cần thay đổi kết nối, thiết lập biến môi trường:
```bash
setx DB_URL "jdbc:mysql://localhost:3306/clothing_shop?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&characterEncoding=UTF-8"
setx DB_USERNAME root
setx DB_PASSWORD mypassword
```

> Lưu ý: `application.properties` đã dùng biến môi trường để giữ mật khẩu an toàn và không ghi trực tiếp vào file cấu hình.

## Cài đặt và chạy ứng dụng
### 1. Tạo database
1. Khởi động MySQL
2. Import file SQL:
```bash
mysql -u root < sql/clothing_shop.sql
```

### 2. Build CSS admin (nếu cần)
Dự án dùng Tailwind CSS cho thư mục admin. Chạy lệnh sau khi sửa CSS:
```bash
npm install
npm run build:css
```

### 3. Chạy ứng dụng Spring Boot
Trên Windows:
```bash
mvnw.cmd spring-boot:run
```

Trên macOS/Linux:
```bash
./mvnw spring-boot:run
```

Mở trình duyệt và truy cập:
```text
http://localhost:8080
```

## Tài khoản mẫu
| Vai trò | Email | Mật khẩu |
|---|---|---|
| Khách | `khach@huyrc.vn` | `123456` |
| Admin | `admin@huyrc.vn` | `admin123` |

## Mã nguồn và công cụ hỗ trợ
- `pom.xml`: cấu hình Maven và phụ thuộc Spring Boot
- `package.json`: cấu hình Tailwind CSS cho phần admin
- `mvnw`, `mvnw.cmd`: wrapper Maven để chạy mà không cần cài Maven toàn cục

## Kiểm thử
Dự án có thư mục kiểm thử:
- `src/test/java`: các lớp kiểm thử Spring Boot

Chạy kiểm thử bằng:
```bash
mvnw.cmd test
```

## Hướng phát triển
Các ý tưởng mở rộng có thể triển khai:
- tìm kiếm sản phẩm
- bộ lọc theo danh mục, giá, kích cỡ
- thông báo và email cho đơn hàng
- trang admin đầy đủ tính năng CRUD sản phẩm và báo cáo
- tích hợp thanh toán online

Xem thêm danh sách công việc trong `TODO.md`.

## Liên hệ
Dự án demo này phù hợp để tham khảo học tập, nâng cấp thành cửa hàng thương mại điện tử hoặc thử nghiệm chức năng quản lý sản phẩm và đơn hàng.
