# TODO — Những thứ cần cải thiện / phát triển tiếp

> Ghi chú định hướng phát triển cho project HuyRC (Spring Boot + Thymeleaf + MySQL).
> Cập nhật: 2026-06-06

## 🐞 Lỗi đã biết (cần sửa)

- [ ] **Biểu đồ doanh thu admin đang hiển thị số liệu GIẢ (demo)**
  Trang `/admin/dashboard` — biểu đồ "Doanh thu theo tháng" không vẽ số thật từ DB.
  - Nguyên nhân: khối `<script>` chứa `ADMIN_MONTHLY` trong [dashboard.html](src/main/resources/templates/admin/dashboard.html)
    nằm **ngoài** `<div th:fragment="content">`, mà [layout.html](src/main/resources/templates/admin/layout.html) chỉ chèn fragment `content`
    → script bị bỏ qua → [admin.js](src/main/resources/static/admin/js/admin.js) dùng mảng demo mặc định.
  - Cách sửa gợi ý: đưa dữ liệu `monthly` vào **trong** fragment qua data-attribute trên thẻ canvas
    (vd `th:attr="data-monthly=${monthly}"`), rồi cho admin.js đọc & `JSON.parse`.

## 🎨 UI / Tính năng cho khách hàng

- [x] **Lọc sản phẩm theo danh mục** ✅ (xong 2026-06-07)
  Bấm pill danh mục → `GET /?categoryId=...` lọc đúng sản phẩm; pill đang chọn được highlight.
  - Đã cấu trúc lại DB thành **6 danh mục theo loại** (Áo sơ mi, Áo thun, Quần dài, Quần short, Giày da, Giày sneaker),
    mỗi danh mục **10 sản phẩm** (mỗi ảnh = 1 sản phẩm) → tổng 60 sản phẩm. Xem [clothing_shop.sql](sql/clothing_shop.sql).

- [x] **Thông báo "đã thêm vào giỏ" thay vì chuyển thẳng tới trang giỏ hàng** ✅ (xong 2026-06-07)
  THÊM VÀO GIỎ → ở lại trang sản phẩm + hiện toast "Đã thêm sản phẩm vào giỏ hàng"; MUA NGAY → sang giỏ.
  - `POST /cart/add` nhận thêm `action` (add/buy); add → `redirect:/products/{id}` + flash `added`, buy → `redirect:/cart`.
  - Header 🛒 tự cập nhật vì trang tải lại sau redirect.

- [x] **Chức năng tìm kiếm sản phẩm** ✅ (xong 2026-06-07)
  Bấm 🔍 → hiện thanh tìm kiếm; `GET /search?q=...` → [SearchController](src/main/java/com/javgr/clothingshop/controller/SearchController.java) + `ProductRepository.searchByName` → trang [search.html](src/main/resources/templates/search.html).

- [x] **Menu danh mục dạng hamburger (☰)** ✅ (xong 2026-06-07)
  Nút ☰ bên trái logo mở drawer trượt từ trái: "Tất cả sản phẩm" + 8 nhóm (bấm nhóm xổ danh mục con). Bỏ nav cũ (Trang phục/Áo/Quần/Giày/Sale). `groups` được cấp ở mọi trang qua [GlobalModelAttributes](src/main/java/com/javgr/clothingshop/controller/GlobalModelAttributes.java).

## 💡 Ý tưởng mở rộng (chưa ưu tiên)

- [ ] Trang admin: quản lý sản phẩm (thêm/sửa/xoá), duyệt đơn hàng (đổi trạng thái PENDING → CONFIRMED → SHIPPED).
- [ ] Header hiển thị **họ tên** thay vì email người dùng.
- [ ] Giá khuyến mãi thật (thêm cột `sale_price` vào `products`) — hiện giá gạch & `-23%` chỉ là trang trí.
- [ ] Phân trang khi nhiều sản phẩm.
- [ ] Thanh toán online / chọn phương thức thanh toán.
