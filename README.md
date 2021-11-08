docker build -t pnaro/jenkins .

docker run \
-p 8080:8080 -e \
JENKINS_ADMIN='pnaro' -e \
JENKINS_PASSWORD='pnaro' \
JENKINS_INIT_JOB='seed' \
JENKINS_URL='http:\\localhost:8080'
-v /Users/pnaro/workspace/gitlab/jenkins/casc_configs:/var/jenkins_home/casc_configs \
-v/Users/pnaro/workspace/gitlab/jenkins/jobs:/usr/share/jenkins/ref/jobs \
pnaro/jenkins

