# Minecraft Authentication Plugin

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21-brightgreen.svg)](https://www.minecraft.net/)
[![Java](https://img.shields.io/badge/Java-21+-orange.svg)](https://www.oracle.com/java/)

마인크래프트 서버에 접속할 때 인증 코드를 요구하는 플러그인입니다.

## 📋 기능

- ✅ 서버 접속 시 자동 인증 시스템
- ✅ 채팅 기반 코드 입력
- ✅ 설정 파일로 인증 코드 관리
- ✅ 인증 전까지 플레이어 이동 제한
- ✅ 실시간 설정 리로드
- ✅ 클릭 가능한 Exit 버튼

## 🚀 설치 방법

1. [Releases](../../releases) 페이지에서 최신 버전의 `.jar` 파일 다운로드
2. 마인크래프트 서버의 `plugins` 폴더에 파일 복사
3. 서버 재시작
4. `plugins/AuthenticationPlugin/settings.yml` 파일에서 인증 코드 설정

## ⚙️ 설정

### settings.yml

```yaml
auth-code: "1234"  # 원하는 인증 코드로 변경
```

설정 변경 후 `/authreload` 명령어로 즉시 적용 가능합니다.

## 🎮 사용 방법

### 플레이어
1. 서버에 접속
2. 채팅창에 표시되는 인증 다이얼로그 확인
3. 채팅창에 인증 코드 입력
4. 인증 성공 시 서버 플레이 가능

### 관리자
- `/authreload` - 설정 파일 리로드 (권한: `auth.reload`)

## 🛠️ 빌드 방법

### 요구사항
- Java 21 이상
- Gradle 8.0 이상

### 빌드 명령어

```bash
git clone https://github.com/neramc/Authentication.git
cd Authentication
./gradlew build
```

빌드된 파일은 `build/libs/Authentication-1.0-SNAPSHOT.jar`에 생성됩니다.

## 📦 의존성

- Spigot/Paper API 1.21+
- Java 17+

## 📝 라이센스

이 프로젝트는 [Apache License 2.0](LICENSE) 라이센스 하에 배포됩니다.

## 🤝 기여

이슈와 Pull Request는 언제나 환영합니다!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📧 문의

프로젝트 링크: [https://github.com/neramc/Authentication](https://github.com/neramc/Authentication)

## 🙏 감사의 말

- [Spigot](https://www.spigotmc.org/) - API 제공
- [Paper](https://papermc.io/) - 최적화된 서버 플랫폼
