FROM spohnan/jenkins-ecs

USER jenkins

COPY config.xml /var/jenkins_home
COPY hudson.tasks.Maven.xml /var/jenkins_home
COPY jenkins.mvn.GlobalMavenConfig.xml /var/jenkins_home
# COPY job-raptor-devops.xml /var/jenkins_home/jobs/raptor-devops/config.xml