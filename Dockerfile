# 기본이미지
FROM 2juno/ptman-server-test:latest
# 워크 디렉토리
WORKDIR /home
# copy 아직 X
# 컨테이너 생성 전 실행할 명령어, mysql 서버 띄우기
#RUN service mysql start
# 컨테이너가 사용할 포트(컨테이너 내부)
EXPOSE 8080
# 컨테이너 띄우고 실행할 명령어
CMD service mysql start && java -jar ./web-ide-0.0.1-SNAPSHOT.jar
