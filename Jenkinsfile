node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    def mvn = tool 'maven';
    withSonarQubeEnv() {
      sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=cloud-infra-dummy_dummy-project_AYruskg4rpQqqYUpD-oi -Dsonar.projectName='dummy-project'"
    }
  }
}