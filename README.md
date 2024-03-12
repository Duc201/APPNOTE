APP NOTES
 - Sử dụng Kotlin , mô hình MVVM , ROOM , Firebase, Sharereference, Corountine, ViewPager 2, 
 - Chức năng chính:
 + Tạo tài khoản , đăng nhập , quên mật khẩu sử dụng Firebase firebase authentication
 + Thêm, sửa, xóa Note sử dụng firebase realtime và Room để lưu dữ liệu. Khi thiết bị Offline sẽ lưu trong Room. Khi thiết bị Online sẽ lưu vào cả Room và FireBase. Mỗi khi mở app và có kết nối Internet sẽ đồng bộ với FireBase
 + Khi thoát đăng nhập dữ liệu trong Room, Sharereference sẽ bị xóa hết 
 + Khi đăng nhập trên thiết bị mới sẽ đồng bộ dữ liệu từ firebase về và lưu trong Room
