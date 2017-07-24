#
# Official image plus some plugins
#
FROM jenkins/jenkins:lts

USER root
RUN apt-get update \
    && apt-get install -y apt-transport-https sudo python-pip \
    && apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D \
    && echo "deb https://apt.dockerproject.org/repo debian-stretch main" >> /etc/apt/sources.list.d/docker.list \
    && apt-get update -y \
    && apt-get install -y docker-engine \
    && pip install awscli \
    && echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers
USER jenkins

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

VOLUME /var/run/docker.sock