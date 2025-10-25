# APIDemo - API Validation & Documentation Guide

Hướng dẫn chi tiết về API validation và sử dụng API documentation cho dự án APIDemo.

## 📋 Tổng quan

Dự án APIDemo cung cấp 2 nhóm API chính:
- **Actor API**: Quản lý diễn viên với validation cơ bản
- **Film API**: Quản lý phim với validation phức tạp hơn

## 🔧 Cài đặt và chạy dự án

### Yêu cầu hệ thống
- Java 17+
- Maven 3.6+
- MySQL 8.0+

## 🚀 Hướng dẫn cài đặt và chạy dự án

### 1. Cài đặt Database

#### Bước 1: Cài đặt MySQL
```bash
# Windows (sử dụng Chocolatey)
choco install mysql

# Hoặc tải từ: https://dev.mysql.com/downloads/mysql/
```

#### Bước 2: Tạo database và import dữ liệu
```sql
-- Kết nối MySQL và tạo database
CREATE DATABASE mydb;
USE mydb;

-- Import file DBScript.sql
-- Cách 1: Sử dụng MySQL Workbench
-- File -> Run SQL Script -> Chọn file DBScript.sql

-- Cách 2: Sử dụng command line
mysql -u root -p mydb < DBScript.sql
```

#### Bước 3: Cấu hình database connection
Chỉnh sửa file `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=your_password_here
```
### Chạy ứng dụng
```bash
# Build và chạy
mvn clean compile
mvn spring-boot:run

# Ứng dụng chạy tại: http://localhost:8081
```

## 📚 API Documentation

### Truy cập Swagger UI
1. Mở trình duyệt và vào: http://localhost:8081/swagger-ui/index.html
2. Swagger UI sẽ hiển thị tất cả API endpoints với documentation chi tiết

### Cấu trúc API Documentation
- **OpenAPI 3.1.0** specification
- **Interactive testing** trực tiếp trên Swagger UI
- **Request/Response examples** cho mỗi endpoint
- **Schema validation** được mô tả chi tiết

## 🎯 API Validation Testing

### 1. Actor API Validation

#### Validation Rules cho Actor
- `firstName`: Bắt buộc, không được trống, tối đa 255 ký tự
- `lastName`: Bắt buộc, không được trống, tối đa 255 ký tự

#### Test Cases cho Actor API

**Test 1: Tạo Actor thành công**
```bash
curl -X POST http://localhost:8081/api/actors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Tom",
    "lastName": "Hanks"
  }'
```

**Test 2: Validation lỗi - firstName trống**
```bash
curl -X POST http://localhost:8081/api/actors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "",
    "lastName": "Hanks"
  }'
```

**Test 3: Validation lỗi - lastName trống**
```bash
curl -X POST http://localhost:8081/api/actors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Tom",
    "lastName": ""
  }'
```

**Test 4: Validation lỗi - firstName quá dài**
```bash
curl -X POST http://localhost:8081/api/actors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "ThisIsAVeryLongFirstNameThatExceedsTheMaximumAllowedLengthOfTwoHundredAndFiftyFiveCharactersAndShouldTriggerValidationErrorBecauseItIsTooLongForTheDatabaseFieldAndTheValidationAnnotationShouldCatchThisAndReturnAnAppropriateErrorMessageToTheUser",
    "lastName": "Hanks"
  }'
```

**Test 5: Validation lỗi - thiếu trường bắt buộc**
```bash
curl -X POST http://localhost:8081/api/actors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Tom"
  }'
```

**Test 6: Validation lỗi - JSON không hợp lệ**
```bash
curl -X POST http://localhost:8081/api/actors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Tom",
    "lastName": "Hanks"
  '
```

### 2. Film API Validation

#### Validation Rules cho Film
- `title`: Bắt buộc, không được trống, tối đa 255 ký tự
- `releaseYear`: Phải > 0, tối đa 2100
- `rentalDuration`: Bắt buộc, phải > 0
- `rentalRate`: Bắt buộc, phải > 0
- `length`: Phải > 0 (nếu có)
- `replacementCost`: Bắt buộc, phải > 0

#### Test Cases cho Film API

**Test 1: Tạo Film thành công**
```bash
curl -X POST http://localhost:8081/api/films \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Forrest Gump",
    "description": "Life is like a box of chocolates",
    "releaseYear": 1994,
    "rentalDuration": 7,
    "rentalRate": 2.99,
    "length": 142,
    "replacementCost": 19.99,
    "rating": "PG-13"
  }'
```

**Test 2: Validation lỗi - title trống**
```bash
curl -X POST http://localhost:8081/api/films \
  -H "Content-Type: application/json" \
  -d '{
    "title": "",
    "rentalDuration": 7,
    "rentalRate": 2.99,
    "replacementCost": 19.99
  }'
```

**Test 3: Validation lỗi - releaseYear không hợp lệ**
```bash
curl -X POST http://localhost:8081/api/films \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Movie",
    "releaseYear": -1994,
    "rentalDuration": 7,
    "rentalRate": 2.99,
    "replacementCost": 19.99
  }'
```

**Test 4: Validation lỗi - releaseYear quá lớn**
```bash
curl -X POST http://localhost:8081/api/films \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Movie",
    "releaseYear": 2500,
    "rentalDuration": 7,
    "rentalRate": 2.99,
    "replacementCost": 19.99
  }'
```

**Test 5: Validation lỗi - rentalDuration âm**
```bash
curl -X POST http://localhost:8081/api/films \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Movie",
    "rentalDuration": -7,
    "rentalRate": 2.99,
    "replacementCost": 19.99
  }'
```

**Test 6: Validation lỗi - rentalRate âm**
```bash
curl -X POST http://localhost:8081/api/films \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Movie",
    "rentalDuration": 7,
    "rentalRate": -2.99,
    "replacementCost": 19.99
  }'
```

**Test 7: Validation lỗi - replacementCost âm**
```bash
curl -X POST http://localhost:8081/api/films \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Movie",
    "rentalDuration": 7,
    "rentalRate": 2.99,
    "replacementCost": -19.99
  }'
```

**Test 8: Validation lỗi - thiếu trường bắt buộc**
```bash
curl -X POST http://localhost:8081/api/films \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Movie",
    "rentalDuration": 7
  }'
```

## 🔍 Sử dụng Swagger UI để Test API

### Bước 1: Truy cập Swagger UI
1. Mở trình duyệt và vào: http://localhost:8081/swagger-ui/index.html
2. Đợi Swagger UI load xong

### Bước 2: Khám phá API Documentation
1. **Xem danh sách API**: Scroll xuống để xem tất cả endpoints
2. **Actor APIs**: Nhóm API quản lý diễn viên
3. **Film APIs**: Nhóm API quản lý phim

### Bước 3: Test API trực tiếp trên Swagger UI

#### Test Actor API
1. **Mở Actor POST endpoint** (`/api/actors`)
2. Click **"Try it out"**
3. **Nhập dữ liệu test**:
   ```json
   {
     "firstName": "John",
     "lastName": "Doe"
   }
   ```
4. Click **"Execute"**
5. **Xem kết quả**: Status code, Response body, Headers

#### Test Film API
1. **Mở Film POST endpoint** (`/api/films`)
2. Click **"Try it out"**
3. **Nhập dữ liệu test**:
   ```json
   {
     "title": "Test Movie",
     "description": "A test movie",
     "releaseYear": 2024,
     "rentalDuration": 7,
     "rentalRate": 2.99,
     "length": 120,
     "replacementCost": 19.99,
     "rating": "PG-13"
   }
   ```
4. Click **"Execute"**
5. **Xem kết quả**: Status code, Response body, Headers

### Bước 4: Test Validation Errors

#### Test Actor Validation
1. **Test với firstName trống**:
   ```json
   {
     "firstName": "",
     "lastName": "Doe"
   }
   ```

2. **Test với lastName trống**:
   ```json
   {
     "firstName": "John",
     "lastName": ""
   }
   ```

3. **Test với JSON không hợp lệ**:
   ```json
   {
     "firstName": "John"
   ```

#### Test Film Validation
1. **Test với title trống**:
   ```json
   {
     "title": "",
     "rentalDuration": 7,
     "rentalRate": 2.99,
     "replacementCost": 19.99
   }
   ```

2. **Test với releaseYear âm**:
   ```json
   {
     "title": "Test Movie",
     "releaseYear": -2024,
     "rentalDuration": 7,
     "rentalRate": 2.99,
     "replacementCost": 19.99
   }
   ```

3. **Test với rentalRate âm**:
   ```json
   {
     "title": "Test Movie",
     "rentalDuration": 7,
     "rentalRate": -2.99,
     "replacementCost": 19.99
   }
   ```

## 📊 Response Format

### Success Response
```json
{
  "id": 1,
  "firstName": "Tom",
  "lastName": "Hanks"
}
```

### Error Response
```json
{
  "status": 400,
  "message": "Invalid request"
}
```

### Validation Error Response
Khi validation fail, Spring Boot sẽ trả về response với format:
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": [
    {
      "field": "firstName",
      "message": "firstName không được để trống"
    },
    {
      "field": "rentalRate",
      "message": "rentalRate phải > 0"
    }
  ]
}
```

## 🧪 Advanced Testing

### Test với Postman
1. **Import OpenAPI spec**: File `src/main/resources/static/api-docs.yaml`
2. **Tạo collection** với tất cả endpoints
3. **Thiết lập environment variables**:
   - `base_url`: http://localhost:8081
   - `api_prefix`: /api

### Test với curl scripts
Tạo file `test-api.sh`:
```bash
#!/bin/bash

BASE_URL="http://localhost:8081/api"

echo "Testing Actor API..."
curl -X GET $BASE_URL/actors
echo -e "\n"

echo "Testing Film API..."
curl -X GET $BASE_URL/films
echo -e "\n"

echo "Testing Actor validation..."
curl -X POST $BASE_URL/actors \
  -H "Content-Type: application/json" \
  -d '{"firstName": "", "lastName": "Test"}'
echo -e "\n"
```

## 📝 Validation Annotations Reference

### ActorRequest Validation
```java
@NotBlank(message = "firstName không được để trống")
@Size(max = 255, message = "firstName tối đa 255 ký tự")
private String firstName;

@NotBlank(message = "lastName không được để trống")
@Size(max = 255, message = "lastName tối đa 255 ký tự")
private String lastName;
```

### FilmRequest Validation
```java
@NotBlank(message = "title không được để trống")
@Size(max = 255, message = "title tối đa 255 ký tự")
private String title;

@Positive(message = "releaseYear phải > 0")
@Max(value = 2100, message = "releaseYear không hợp lệ")
private Integer releaseYear;

@NotNull(message = "rentalDuration bắt buộc")
@Positive(message = "rentalDuration phải > 0")
private Integer rentalDuration;

@NotNull(message = "rentalRate bắt buộc")
@DecimalMin(value = "0.0", inclusive = false, message = "rentalRate phải > 0")
private BigDecimal rentalRate;
```

## 🔧 Troubleshooting

### Lỗi thường gặp

1. **Validation không hoạt động**:
   - Kiểm tra `@Valid` annotation trong controller
   - Đảm bảo `spring-boot-starter-validation` trong pom.xml

2. **Swagger UI không load**:
   - Kiểm tra `springdoc-openapi-starter-webmvc-ui` dependency
   - Kiểm tra file `api-docs.yaml` có tồn tại

3. **API trả về 500 error**:
   - Kiểm tra database connection
   - Xem logs trong console để debug

### Debug Validation
Thêm vào `application.properties`:
```properties
# Bật debug logging cho validation
logging.level.org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor=DEBUG
logging.level.org.springframework.validation=DEBUG
```
