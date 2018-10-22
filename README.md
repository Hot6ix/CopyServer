# CopyServer
A copy server which allows only a single connection based on socket

스마트폰 -> PC 인증번호 전송용 서버

- 사용 방법
  - 프로그램 실행 시 시스템 트레이에서 아이콘을 확인할 수 있습니다.
  - 전용 앱을 통해 연결합니다.
  - 스마트폰에서 인증번호 SMS 수신 시 인증번호가 자동으로 복사됩니다.
  
- setting.ini 수정 방법
    - password는 앱에서 서버 연결 시 필요한 비밀번호로 기본적으로 빈칸으로 되어 비밀번호를 요구하지 않습니다.
    - port(default: 31331)는 프로그램과 앱이 연결하기 위한 통로로 특별한 경우를 제외하고 바꾸지 않을 것을 권장합니다.
    - allowCharacter(default: false)는 받은 데이터 안에 문자가 포함되어 있을 경우 클립보드에 복사하지 않습니다. true로 변경 시 문자가 포함되어 있어도 클립보드에 복사합니다.
  
- 주의 사항
  - 첫 실행 시 같은 폴더 내 setting.ini를 생성합니다.
