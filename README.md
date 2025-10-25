# APIDemo - Spring Boot Application

Dự án Spring Boot API Demo với tích hợp Elasticsearch và Kibana để theo dõi logs.

## 📋 Yêu cầu hệ thống

- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Elasticsearch 8.x
- Kibana 8.x
- Filebeat 8.x

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

### 2. Cài đặt Elasticsearch và Kibana

#### Cách 1: Sử dụng Docker (Khuyến nghị)
```bash
# Tạo file docker-compose.yml
version: '3.8'
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.11.0
    container_name: elasticsearch
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ports:
      - "9200:9200"
    volumes:
      - elasticsearch_data:/usr/share/elasticsearch/data

  kibana:
    image: docker.elastic.co/kibana/kibana:8.11.0
    container_name: kibana
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch

volumes:
  elasticsearch_data:

# Chạy Elasticsearch và Kibana
docker-compose up -d
```

#### Cách 2: Cài đặt trực tiếp
```bash
# Tải và cài đặt Elasticsearch
# Windows: https://www.elastic.co/downloads/elasticsearch
# Giải nén và chạy: bin/elasticsearch.bat

# Tải và cài đặt Kibana
# Windows: https://www.elastic.co/downloads/kibana
# Giải nén và chạy: bin/kibana.bat
```

### 3. Cài đặt Filebeat

#### Bước 1: Tải và cài đặt Filebeat
```bash
# Windows: Tải từ https://www.elastic.co/downloads/beats/filebeat
# Giải nén vào thư mục C:\filebeat
```

#### Bước 2: Cấu hình Filebeat
Chỉnh sửa file `filebeat.yml` trong thư mục dự án:
```yaml
filebeat.inputs:
  - type: filestream
    enabled: true
    paths:
      - C:\Users\XANH\Downloads\WEB_22KTPM1\logs\server.log  # Đường dẫn tuyệt đối đến file log
    json.keys_under_root: true
    json.add_error_key: true

output.elasticsearch:
  hosts: ["http://localhost:9200"]
  username: "elastic"
  password: "yourpassword"

setup.kibana:
  host: "http://localhost:5601"
```

#### Bước 3: Chạy Filebeat
```bash
# Di chuyển đến thư mục filebeat
cd C:\filebeat

# Chạy filebeat với config từ dự án
.\filebeat.exe -e -c C:\Users\XANH\Downloads\WEB_22KTPM1\filebeat.yml
```

### 4. Chạy ứng dụng Spring Boot

#### Bước 1: Build dự án
```bash
# Di chuyển đến thư mục dự án
cd C:\Users\XANH\Downloads\WEB_22KTPM1

# Build dự án
mvn clean compile
```

#### Bước 2: Chạy ứng dụng
```bash
# Cách 1: Sử dụng Maven
mvn spring-boot:run

# Cách 2: Build JAR và chạy
mvn clean package
java -jar target/APIDemo-0.0.1-SNAPSHOT.jar
```

#### Bước 3: Kiểm tra ứng dụng
- Ứng dụng chạy tại: http://localhost:8081

## 🔍 Kiểm tra và Test Server

### 1. Test API Endpoints

#### Test Actor API - API thành công
```bash
# Lấy danh sách actors
curl -X GET http://localhost:8081/api/actors

# Tạo actor mới
curl -X POST http://localhost:8081/api/actors \
  -H "Content-Type: application/json" \
  -d '{"firstName": "John", "lastName": "Doe"}'

# Lấy actor theo ID
curl -X GET http://localhost:8081/api/actors/1

# Cập nhật actor
curl -X PUT http://localhost:8081/api/actors/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName": "Jane", "lastName": "Smith"}'

# Xóa actor
curl -X DELETE http://localhost:8081/api/actors/1
```

#### Test Film API - API thành công
```bash
# Lấy danh sách films
curl -X GET http://localhost:8081/api/films

# Tạo film mới
curl -X POST http://localhost:8081/api/films \
  -H "Content-Type: application/json" \
  -d '{"title": "Test Movie", "description": "A test movie", "releaseYear": 2024}'
```

### 2. Test API với lỗi để tạo logs

#### Gửi API lỗi để tạo error logs
```bash
# Test 1: Gửi request với dữ liệu không hợp lệ
curl -X POST http://localhost:8081/api/actors \
  -H "Content-Type: application/json" \
  -d '{"firstName": "", "lastName": ""}'

# Test 2: Gửi request với JSON không đúng format
curl -X POST http://localhost:8081/api/actors \
  -H "Content-Type: application/json" \
  -d '{"firstName": "John"'

# Test 3: Gửi request đến endpoint không tồn tại
curl -X GET http://localhost:8081/api/actors/99999

# Test 4: Gửi request với method không đúng
curl -X PATCH http://localhost:8081/api/actors/1

# Test 5: Gửi request với Content-Type sai
curl -X POST http://localhost:8081/api/actors \
  -H "Content-Type: text/plain" \
  -d "invalid data"
```

### 3. Kiểm tra Logs

#### Xem logs trong file
```bash
# Windows
type logs\server.log

# Hoặc sử dụng PowerShell
Get-Content logs\server.log -Tail 50
```

#### Xem logs trong Kibana

**Bước 1: Truy cập Kibana**
1. Mở trình duyệt và vào: http://localhost:5601
2. Đợi Kibana load xong

**Bước 2: Tạo Index Pattern**
1. Vào **Stack Management** (biểu tượng bánh răng ở góc trái)
2. Chọn **Index Patterns**
3. Click **Create index pattern**
4. Nhập `filebeat-*` vào ô Index pattern name
5. Click **Next step**
6. Chọn `@timestamp` làm Time field
7. Click **Create index pattern**

**Bước 3: Xem logs trong Discover**
1. Vào **Discover** (biểu tượng kính lúp ở menu trái)
2. Chọn index pattern `filebeat-*` nếu chưa chọn
3. Bạn sẽ thấy tất cả logs được gửi từ Filebeat

**Bước 4: Tìm kiếm và lọc logs**
1. **Tìm kiếm theo từ khóa:**
   - Gõ `ERROR` vào ô search để tìm error logs
   - Gõ `WARN` để tìm warning logs
   - Gõ `INFO` để tìm info logs

2. **Lọc theo thời gian:**
   - Click vào biểu tượng đồng hồ ở góc phải trên
   - Chọn khoảng thời gian muốn xem (ví dụ: Last 15 minutes)

3. **Lọc theo field:**
   - Click vào field `level` để xem các level log khác nhau
   - Click vào field `message` để xem nội dung log
   - Click vào field `logger` để xem logger nào tạo ra log

**Bước 5: Xem chi tiết log**
1. Click vào một log entry để xem chi tiết
2. Trong phần **Available fields**, bạn có thể:
   - Xem `timestamp`: Thời gian tạo log
   - Xem `level`: Mức độ log (ERROR, WARN, INFO, DEBUG)
   - Xem `message`: Nội dung log
   - Xem `logger`: Class/package tạo ra log
   - Xem `thread`: Thread name

**Bước 6: Tạo Dashboard (Tùy chọn)**
1. Vào **Dashboard** (biểu tượng dashboard ở menu trái)
2. Click **Create dashboard**
3. Click **Add** để thêm visualization
4. Chọn **Data table** hoặc **Line chart**
5. Chọn index pattern `filebeat-*`
6. Cấu hình visualization theo ý muốn

### 4. Ví dụ Trace Log cụ thể

Sau khi chạy các API test ở trên, bạn sẽ thấy trong Kibana:

1. **Logs thành công:**
   ```json
   {
     "timestamp": "2024-01-15T10:30:00.000Z",
     "level": "INFO",
     "message": "Started ApiDemoApplication in 2.5 seconds",
     "logger": "com.example.APIDemo.ApiDemoApplication"
   }
   ```

2. **Error logs:**
   ```json
   {
     "timestamp": "2024-01-15T10:31:00.000Z",
     "level": "ERROR",
     "message": "Validation failed for argument",
     "logger": "org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor"
   }
   ```

3. **SQL logs:**
   ```json
   {
     "timestamp": "2024-01-15T10:32:00.000Z",
     "level": "DEBUG",
     "message": "select actor0_.actor_id as actor_id1_0_, actor0_.first_name as first_na2_0_, actor0_.last_name as last_nam3_0_, actor0_.last_update as last_upd4_0_ from actor actor0_",
     "logger": "org.hibernate.SQL"
   }
   ```

## 📊 Cấu trúc dự án

```
WEB_22KTPM1/
├── src/main/java/com/example/APIDemo/
│   ├── controller/          # REST Controllers
│   ├── service/            # Business Logic
│   ├── repository/         # Data Access Layer
│   ├── model/              # Entity Models
│   ├── dto/                # Data Transfer Objects
│   ├── config/             # Configuration
│   └── security/           # Security Configuration
├── src/main/resources/
│   ├── application.properties
│   └── logback-spring.xml
├── logs/                   # Log files
├── DBScript.sql           # Database script
├── filebeat.yml           # Filebeat configuration
└── pom.xml               # Maven dependencies
```
