FROM rtfpessoa/ubuntu-jdk8

RUN apt-get update && \
    apt-get install maven -y


WORKDIR /src
ADD . /src

RUN mvn install
RUN mvn package

EXPOSE 1234

WORKDIR /src/target

CMD ["java", "-jar", "words-0.0.1-SNAPSHOT.jar"]

