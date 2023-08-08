# MapBook_Batch

> MapBook Service를 위한 Batch Server  <br><br>
>(https://mapbook.pro/)



### Batch_Process
1. 대출 횟수 최신화
- Target Data : 전국 도서관 1460개. 1억 2천만건. 용량 12GB
- Batch Interval : month

![image](https://github.com/TeamScaling/MapBook-Batch/assets/107255371/dfb61e3b-d3a6-472b-a4d3-715aed02488e)

2. 도서 상세 정보 업데이트
- Target Data : 도서관 소장 도서 목록에서 산출된 업데이트가 필요한 도서 데이터
- Batch Interval : day. 3만건 씩
- Kakao 도서 API를 통해 데이터를 확보한 뒤 Update
![image](https://github.com/TeamScaling/MapBook-Batch/assets/107255371/a1dda7ec-e8e2-41cf-b44a-464b6dd546d2)


### Project Duration

- Duration: 2023.07.28 ~

### Backend Technology
- Java 17
- Spring Boot 2.7.14
- Spring JPA
- Spring Batch

### Infrastructure
- AWS EC2
- AWS RDS (Mysql 8.0)

### Open API

- Kakao Book API

