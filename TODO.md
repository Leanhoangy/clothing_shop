# TODO — Những thứ cần cải thiện / phát triển tiếp

> Ghi chú định hướng phát triển cho project HuyRC (Spring Boot + Thymeleaf + MySQL).
> Cập nhật: 2026-06-06

## 🎨 UI / Tính năng cho khách hàng

- [ ] **Lọc sản phẩm theo danh mục**
  Trên trang chủ, bấm vào mục danh mục (ví dụ "ÁO") thì chỉ hiện các sản phẩm thuộc danh mục đó.
  - Hiện tại các pill danh mục (`TẤT CẢ / ÁO / QUẦN / GIÀY`) trong [index.html](src/main/resources/templates/index.html) chỉ là trang trí, chưa hoạt động.
  - Gợi ý: thêm endpoint `GET /?category=ao` (hoặc `/category/{slug}`) trong [ShopController](src/main/java/com/javgr/clothingshop/controller/ShopController.java),
    thêm query lọc theo category trong [ProductRepository](src/main/java/com/javgr/clothingshop/repository/ProductRepository.java),
    cho pill thành link và highlight pill đang chọn.

- [ ] **Thông báo "đã thêm vào giỏ" thay vì chuyển thẳng tới trang giỏ hàng**
  Khi bấm "THÊM VÀO GIỎ" ở [product.html](src/main/resources/templates/product.html), hiện báo thành công và **ở lại** trang sản phẩm
  (chỉ "MUA NGAY" mới chuyển sang giỏ).
  - Hiện tại `POST /cart/add` trong [CartController](src/main/java/com/javgr/clothingshop/controller/CartController.java) luôn `redirect:/cart`.
  - Gợi ý: redirect về lại `/products/{id}?added` + hiện toast/alert; hoặc dùng AJAX (fetch) để thêm giỏ không tải lại trang, cập nhật số 🛒 trên header.

- [ ] **Chức năng tìm kiếm sản phẩm**
  Ô tìm kiếm 🔍 trên header cho gõ từ khoá → hiện sản phẩm khớp tên.
  - Hiện icon 🔍 trong [fragments/layout.html](src/main/resources/templates/fragments/layout.html) chưa làm gì.
  - Gợi ý: form `GET /search?q=...`, controller + `ProductRepository.findByNameContainingIgnoreCase(...)`, trang kết quả (tái dùng lưới sản phẩm của index).

## 💡 Ý tưởng mở rộng (chưa ưu tiên)

- [ ] Trang admin: quản lý sản phẩm (thêm/sửa/xoá), duyệt đơn hàng (đổi trạng thái PENDING → CONFIRMED → SHIPPED).
- [ ] Header hiển thị **họ tên** thay vì email người dùng.
- [ ] Giá khuyến mãi thật (thêm cột `sale_price` vào `products`) — hiện giá gạch & `-23%` chỉ là trang trí.
- [ ] Phân trang khi nhiều sản phẩm.
- [ ] Thanh toán online / chọn phương thức thanh toán.
