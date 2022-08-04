# Build image with: 
#   `docker build -t spf .`
# Afterwards run container with: 
#   `docker run -it spf /bin/bash`
FROM ubuntu:16.04

RUN apt-get update -y
RUN apt-get install -y build-essential
RUN apt-get install -y git
RUN apt-get install -y ant

# Download java 8(!)
RUN apt-get install -y openjdk-8-jdk
RUN mkdir /usr/lib/LISSA

COPY . /usr/lib/LISSA
WORKDIR /usr/lib/LISSA

# Setup jpf env
RUN mkdir /root/.jpf
RUN echo 'jpf-core = /usr/lib/LISSA/jpf-core' >> /root/.jpf/site.properties
RUN echo 'jpf-symbc = /usr/lib/LISSA/jpf-symbc' >> /root/.jpf/site.properties
RUN echo 'extensions=${jpf-core},${jpf-symbc}' >> /root/.jpf/site.properties

# Build SymSolve
WORKDIR /usr/lib/LISSA/symsolve
RUN ./gradlew clean
RUN ./gradlew build
RUN cp build/libs/symsolve.jar ../jpf-symbc/lib

# Build jpf
WORKDIR /usr/lib/LISSA/jpf-core
RUN ant clean
RUN ant build
WORKDIR /usr/lib/LISSA/jpf-symbc
RUN ant clean
RUN ant build

# Setup path
ENV JPF_HOME=/usr/lib/LISSA
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
ENV LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JPF_HOME/jpf-symbc/lib

WORKDIR /usr/lib/LISSA/jpf-symbc