FROM jenkins/jenkins:lts-jdk17
USER root

# Instalar Docker CLI
RUN apt-get update && apt-get install -y \
    docker.io \
    curl \
    && apt-get clean

# Instalar Docker Compose v2 (comprobado)
RUN curl -SL https://github.com/docker/compose/releases/download/v2.24.5/docker-compose-linux-x86_64 \
    -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose

USER jenkins
