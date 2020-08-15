# seomse-api

# 개발환경
-   open jdk 1.8

# 구성
api 
 - java class 구현에 따른 기능 제공 

역 제어 api(reverse control api)
 - 클라이언트가 서버가 되어 나를 제어할 수 있는 api 기능 제공

push, receive
 - 대량 메시지 전송을 위한 방식, 데이터 전송 단위 크기를 정할 수 있고 메시지 종료 이벤트를 받을 수 있음.
 
#문서자료
구성 부분에 있는 기능들의 상세한 사용 방법은 지원 하려 합니다.
 - 아직 문서화는 진행 되지 않았습니다.

https://seomse.com/ 
주소에 업데이트 될 예정 입니다.

#gradle
implementation 'com.seomse.api:seomse-api:1.0.0'