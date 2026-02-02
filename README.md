# Upbit Market App

업비트 Open API를 활용한 암호화폐 시세 조회 Android 앱입니다.

## 스크린샷

| 마켓 목록 | 마켓 상세 |
|:---:|:---:|
| KRW/BTC/USDT 탭별 시세 조회 | 선택한 코인 상세 정보 |

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Kotlin 2.1.0 |
| UI | Jetpack Compose, Material3 |
| Architecture | Clean Architecture, Multi-Module |
| DI | Hilt |
| Network | Retrofit, OkHttp, Kotlinx Serialization |
| Async | Coroutines, Flow |
| Navigation | Navigation Compose |
| Test | JUnit4, MockK, Turbine |
| Coverage | Kover |

## 모듈 구조

```
├── app                     # 앱 진입점, Navigation
├── core
│   ├── core-common        # 공통 유틸, Result 클래스
│   ├── core-network       # 네트워크 설정, API Client
│   ├── core-ui            # 공통 UI 컴포넌트, 테마
│   └── core-testing       # 테스트 유틸
├── domain
│   └── domain-market      # Market UseCase, Entity, Repository Interface
├── data
│   └── data-market        # Market Repository 구현, API, DTO
└── feature
    └── feature-market     # Market UI, ViewModel
```

## 아키텍처

```
┌─────────────────────────────────────────────────────────────┐
│                        Presentation                          │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │   Screen    │───▶│  ViewModel  │───▶│  UiState    │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                          Domain                              │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │   UseCase   │───▶│   Entity    │    │ Repository  │     │
│  └─────────────┘    └─────────────┘    │ (Interface) │     │
│                                         └─────────────┘     │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                           Data                               │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │ Repository  │───▶│     DTO     │───▶│     API     │     │
│  │   (Impl)    │    └─────────────┘    └─────────────┘     │
│  └─────────────┘                                            │
└─────────────────────────────────────────────────────────────┘
```

## 주요 기능

- **마켓 목록 조회**: KRW, BTC, USDT 마켓별 암호화폐 목록
- **실시간 시세**: 현재가, 변동률, 거래대금 표시
- **마켓 상세**: 고가/저가, 52주 최고/최저가, 호가창
- **자동 새로고침**: Flow 기반 주기적 데이터 갱신

## 빌드 및 실행

### 요구사항
- Android Studio Ladybug 이상
- JDK 11
- Android SDK 24+ (minSdk)

### 빌드
```bash
./gradlew assembleDebug
```

### 테스트
```bash
# 단위 테스트
./gradlew test

# E2E 테스트 (에뮬레이터/기기 필요)
./gradlew connectedAndroidTest

# 커버리지 리포트
./gradlew koverHtmlReport
```

## API

[Upbit Open API](https://docs.upbit.com/)를 사용합니다.

| Endpoint | 설명 |
|----------|------|
| `GET /v1/market/all` | 마켓 코드 조회 |
| `GET /v1/ticker` | 현재가 정보 |
| `GET /v1/orderbook` | 호가 정보 |

## 의존성 그래프

```
app
 ├── core-common
 ├── core-network
 ├── core-ui
 ├── domain-market
 ├── data-market
 └── feature-market

feature-market
 ├── core-common
 ├── core-ui
 └── domain-market

data-market
 ├── core-common
 ├── core-network
 └── domain-market

domain-market
 └── core-common
```

## 라이선스

MIT License
