# 프로젝트 구조
 - 서비스 단위의 패키지 구조
 - 자체적 디자인 시스템 구성
 - 화면 구조
   - MainScreen
     - SearchResultScreen
       - SearchKeyInScreen
     - FavoriteScreen
       - SearchKeyInScreen
   - BookDetailScreen

# 주요 구현 포인트
- Data Class(LazyItem) 기반으로 가변적으로 다양한 컴포저블을 리스트에 생성
- MVI 패턴의 구조