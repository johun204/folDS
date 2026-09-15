# folDS

갤럭시 폴드용으로 커스터마이즈한 개인용 닌텐도 DS 에뮬레이터.
[rafaelvcaetano/melonDS-android](https://github.com/rafaelvcaetano/melonDS-android)의 포크이며, 에뮬레이션 코어는 [melonDS](https://melonds.kuribo64.net/)입니다.

## 폴드 전용으로 바꾼 것

- **펼침(내부 화면)**: 화면 전체에 실제 DS Lite 모양 스킨을 깔고, 스킨 속 두 화면 위치에 게임을 렌더링합니다. 스킨 위의 십자키·X/Y/A/B·START/SELECT를 누르면 실제 입력이 들어가고 눌린 부분이 어두워집니다.
- **접힘(커버 화면)**: 같은 컨셉을 세로로 재구성해, 좁고 긴 화면에서도 게임 화면이 작아지지 않도록 두 화면을 거의 전체 폭으로 배치합니다.
- 자동 퀵세이브(설정 > Save Files), 백그라운드·접힘 시 저장
- 시작 시 ROM 캐시 정리, 자세별 화면 회전 고정

스킨 좌표는 `app/src/main/java/me/magnum/melonds/impl/layout/DsSkin.kt`에 스킨 PNG(`app/src/main/res/drawable-nodpi/`) 픽셀 기준으로 들어 있습니다. 스킨 이미지를 바꾸면 좌표도 함께 고쳐야 합니다.

## 빌드

로컬에 JDK 21 / NDK / SDK가 없어 GitHub Actions로 빌드합니다.

```
gh workflow run build-fold8-release.yml --ref main
```

APK는 워크플로 아티팩트와 Releases에 올라갑니다. CI가 매번 새 서명 키를 만들기 때문에 기존 설치본 위에 덮어 설치되지 않습니다(재설치 필요).

## 라이선스

원본과 동일하게 GPLv3입니다. [LICENSE](LICENSE) 참고.
