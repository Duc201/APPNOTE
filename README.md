APP NOTES
 - Sử dụng Kotlin , mô hình MVVM , ROOM , Firebase, Sharereference, Corountine, ViewPager 2, 
 - Chức năng chính:
 + Tạo tài khoản , đăng nhập , quên mật khẩu sử dụng Firebase firebase authentication
 + Thêm, sửa, xóa Note sử dụng firebase realtime và Room để lưu dữ liệu. Khi thiết bị Offline sẽ lưu trong Room. Khi thiết bị Online sẽ lưu vào cả Room và FireBase. Mỗi khi mở app và có kết nối Internet sẽ đồng bộ với FireBase
 + Khi thoát đăng nhập dữ liệu trong Room, Sharereference sẽ bị xóa hết 
 + Khi đăng nhập trên thiết bị mới sẽ đồng bộ dữ liệu từ firebase về và lưu trong Room
![image](https://github.com/Duc201/APPNOTE/assets/128071699/34a9e291-2c8b-48f3-a0ce-32baf265ba1b)
![image](https://github.com/Duc201/APPNOTE/assets/128071699/ce18dc90-93c5-430d-9100-3c27f9a75bb4)
![image](https://github.com/Duc201/APPNOTE/assets/128071699/c1602cc3-1209-4aac-b7cf-7583b7654d46)
![image](https://github.com/Duc201/APPNOTE/assets/128071699/d4c5bb7f-a5b8-4e8c-b040-64d4f28c18e3)

