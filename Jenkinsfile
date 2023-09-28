node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    def mvn = tool 'Default Maven';
    withSonarQubeEnv() {
      sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=cloud-infra-cr_dummy-project_AYrbEOdbPBi0XjJtoEVc -Dsonar.projectName='dummy-project'"
    }
  }
}