FROM centos:7

WORKDIR /root

RUN yum update && yum install -y java-1.8.0-openjdk

ADD target/highloadcup2017-1.0.jar .

EXPOSE 80

CMD java -Xms3g -Dproduction-load=true -jar highloadcup2017-1.0.jar