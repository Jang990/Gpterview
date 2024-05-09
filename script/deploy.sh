#!/bin/bash

REPOSITORY=/home/ec2-user/app/project
PROJECT_NAME=Gpterview

echo "> Build 파일복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(ps -ef | grep java | awk '{print $2}')
echo "연재 구동중인 애플리케이션 pid: $CURRENT_PID"

if [ -z $CURRENT_PID]; then
        echo "> 현재 구동중인 애플리케이션 없음.";
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)
echo "> JAR Name: $JAR_NAME"

echo "JAR 실행 권한 부여"
chmod +x $REPOSITORY/*.jar

echo "$JAR_NAME 실행"
nohup java -jar -Dspring.config.location=classpath:/application-interview.yml,/home/ec2-user/app/application-prod.yml,/home/ec2-user/app/application-openai.yml,/home/ec2-user/app/application-oauth.yml -Dspring.profiles.active=prod $REPOSITORY/$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &