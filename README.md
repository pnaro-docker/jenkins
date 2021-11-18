#Jenkins base image
https://hub.docker.com/r/jenkins/jenkins

#Commands
docker build -t pnaro/jenkins .

docker run \
-p 8080:8080 -e \
JENKINS_ADMIN='pnaro' -e \
JENKINS_PASSWORD='pnaro' \
JENKINS_INIT_JOB='seed' \
JENKINS_URL='http:\\localhost:8080'
-v casc_configs:/var/jenkins_home/casc_configs \
-v jobs:/usr/share/jenkins/ref/jobs \
pnaro/jenkins

