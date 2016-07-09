FROM maven:3-jdk-8
VOLUME /tmp
ADD . /
RUN apt-get -y update
RUN apt-get install -y python-pip 
RUN pip install awscli 
RUN mvn package
CMD aws s3 cp s3://cnu-2016/prabh_simran/application.properties target/application.properties &&  java -jar target/Id1-1.0-SNAPSHOT.jar --spring.config.location=./target/application.properties
