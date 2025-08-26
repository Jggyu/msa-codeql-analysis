# MSA Service Call Analysis with CodeQL

이 프로젝트는 CodeQL을 사용하여 마이크로서비스 간 호출 관계를 분석하는 방법을 테스트하기 위한 샘플 프로젝트입니다.

## 프로젝트 구조

```
├── user-service/          # 사용자 관리 서비스 (포트: 8080)
├── order-service/         # 주문 관리 서비스 (포트: 8081)
├── codeql-queries/        # 사용자 정의 CodeQL 쿼리
└── .github/workflows/     # CodeQL 분석 워크플로우
```

## 서비스 간 의존성

**Order Service → User Service**
- `OrderService.getOrderWithUserInfo()`: User Service 호출하여 사용자 정보 조회
- `OrderService.validateOrderUser()`: User Service 호출하여 사용자 유효성 검증
- `UserServiceClient`: WebClient를 사용한 HTTP 통신

## CodeQL 분석 방법

### 1. GitHub Actions를 통한 자동 분석

이 리포지토리는 GitHub Actions를 통해 자동으로 CodeQL 분석을 실행합니다:

- **트리거**: Push, Pull Request, 매주 월요일 12:30
- **분석 결과**: Security 탭에서 확인 가능

### 2. 사용자 정의 쿼리

#### `msa-service-calls.ql`
- 마이크로서비스 간 HTTP 호출 감지
- WebClient, RestTemplate 호출 패턴 분석

#### `service-dependency-mapping.ql`
- 서비스 간 의존성 매핑
- 호출 대상 URL과 메서드 정보 추출

### 3. 로컬에서 CodeQL 실행

```bash
# CodeQL CLI 설치 후
codeql database create java-database --language=java

# 사용자 정의 쿼리 실행
codeql query run codeql-queries/msa-service-calls.ql --database=java-database

# 분석 결과를 SARIF 형식으로 출력
codeql database analyze java-database codeql-queries/ --format=sarif-latest --output=results.sarif
```

## 서비스 실행 방법

### User Service 실행
```bash
cd user-service
mvn spring-boot:run
```
- 접근 URL: http://localhost:8080
- API 엔드포인트:
  - `GET /api/users` - 모든 사용자 조회
  - `GET /api/users/{id}` - 특정 사용자 조회
  - `POST /api/users` - 사용자 생성

### Order Service 실행
```bash
cd order-service
mvn spring-boot:run
```
- 접근 URL: http://localhost:8081
- API 엔드포인트:
  - `GET /api/orders` - 모든 주문 조회
  - `GET /api/orders/{id}` - 특정 주문 조회
  - `GET /api/orders/{id}/with-user` - 사용자 정보 포함 주문 조회 (User Service 호출)
  - `GET /api/orders/{id}/validate-user` - 주문 사용자 유효성 검증 (User Service 호출)

## 테스트 시나리오

1. **User Service 실행** (포트 8080)
2. **Order Service 실행** (포트 8081)
3. **서비스 간 호출 테스트**:
   ```bash
   # 사용자 정보가 포함된 주문 조회 (Order Service → User Service 호출)
   curl http://localhost:8081/api/orders/1/with-user
   
   # 주문 사용자 검증 (Order Service → User Service 호출)
   curl http://localhost:8081/api/orders/1/validate-user
   ```

## CodeQL 분석 결과 확인

1. **GitHub Security 탭** - 자동 분석 결과 확인
2. **Actions 탭** - 워크플로우 실행 상태 확인
3. **로컬 분석** - SARIF 파일로 상세 결과 확인

## 주요 분석 포인트

- **HTTP 클라이언트 호출 패턴**: WebClient 사용
- **URL 하드코딩 감지**: application.yml의 설정값 사용
- **서비스 의존성 맵**: Order Service → User Service
- **API 엔드포인트 분석**: REST 컨트롤러 매핑

## 확장 방법

1. **새로운 서비스 추가**
2. **다양한 통신 방식 테스트** (Feign, RestTemplate 등)
3. **더 복잡한 CodeQL 쿼리 작성**
4. **CircuitBreaker, 로드밸런싱 등 MSA 패턴 추가**

---

이 프로젝트는 MSA 환경에서 CodeQL을 활용한 서비스 간 의존성 분석의 기본적인 예제를 제공합니다.