node {
    def mvnHome
    stage('Preparation') {
        git 'https://github.com/ICFI/raptor-devops.git'
        mvnHome = tool 'mvn3.x'
    }
    stage('Build') {
        sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
    }
    stage('Results') {
        junit '**/target/surefire-reports/TEST-*.xml'
    }
}