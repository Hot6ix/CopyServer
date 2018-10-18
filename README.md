# CopyServer
A copy server which allows only a single connection based on socket

Package explorer
- client
  - Sample client class for test server  
- data
  - Data classes  
- main
  - Main source of server and start class  
- utils
  - Util classes to create ini file and send to or receive from client  
  
1. Run server (Setting.ini file will be created in same directory of server)
2. Run TestClient
3. Type string in TestClient to send message
4. Server will receive message but if message contains character, server won't copy to clipboard (You can change the setting in ini file)
5. If you didn't change setting and send only numbers to server, server will copy to clipboard
6. Ctrl + V to paste

* Server will disconnect all other connections except first connection.  
* You can check system tray to see several options
  - 정보 (See information)
  - 설정 파일 열기 (Open setting.ini)
  - 연결 끊기 (Disconnect connected)
  - 종료 (Finish)
* This project used to use for copy numbers to auth.
