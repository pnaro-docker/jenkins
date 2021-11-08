FROM jenkins/jenkins:2.303.3-lts
USER ${user}
ENV CASC_JENKINS_CONFIG /var/jenkins_home/casc_configs
COPY src/main/groovy/ /usr/share/jenkins/ref/init.groovy.d/
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
