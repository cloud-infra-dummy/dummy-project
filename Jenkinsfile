node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    def mvn = tool 'maven';
    withSonarQubeEnv() {
      sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=cloud-infra-cr_dummy-project_AYrf1XTHrpQqqYUpD-oP -Dsonar.projectName='dummy-project'"
    }
  }
}