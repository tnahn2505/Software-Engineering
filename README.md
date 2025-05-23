# Software-Engineering

## NPKSchool - Website Quản lý học sinh

NPKSchool là hệ thống quản lý học sinh được thiết kế theo kiến trúc microservices. Dự án phục vụ mục tiêu hiện đại hóa công tác quản lý học sinh cho các trường phổ thông, hỗ trợ các nghiệp vụ như: quản lý hồ sơ học sinh, phân lớp, nhập điểm, thống kê học tập, và phân quyền người dùng.

## 👨‍💻 Thành viên nhóm

- Phạm Thanh Nhân - 3121411154  
- Vũ Bình Phước - 3121411171  
- Nguyễn Văn Kiệt - 3121411117  

---

## 📌 Mục tiêu dự án

- Số hóa công tác quản lý học sinh trong nhà trường.
- Thiết kế hệ thống linh hoạt, dễ mở rộng.
- Phân tách hệ thống thành các microservice độc lập.
- Tăng tính bảo mật và hiệu quả vận hành.

---

## 🏗️ Kiến trúc hệ thống

- **Frontend**: ReactJS
- **Backend**: Java Spring Boot
- **Database**: MySQL
- **Triển khai**: Docker & Docker Compose
- **CI/CD**: GitHub Actions
- **Authentication**: Spring Security + JWT

---

## ⚙️ Các Microservices

| Service | Port | Mô tả |
|--------|------|--------|
| User Service | `8089` | Quản lý người dùng và xác thực |
| Student Service | `8083` | Quản lý học sinh |
| Teacher Service | `8084` | Quản lý giáo viên |
| Frontend | `3000` | Giao diện người dùng |

---

## 🚀 Tính năng chính

- Đăng nhập & phân quyền (Admin, Giáo viên, Học sinh)
- CRUD học sinh, giáo viên
- Nhập điểm và xem bảng điểm
- Phân lớp học sinh
- Thống kê học lực
- RESTful API chuẩn hóa

---


## Bảng phân công
- Vũ Bình Phước - Build Student Service và Frontend StudentManagement, HomeStudent, AddStudent, EditStudent, ViewScore.
- Phạm Thanh Nhân - Build Teacher Service và Frontend TeacherManagement, HomeTeacher, AddTeacher, EditTeacher, EditScore.
- Nguyễn Văn Kiệt - Build User Service và Frontend Login, ResetPassword, ChangePassword, InfoTeacher, InfoStudent.
