node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    withSonarQubeEnv() {
      sh "mvn clean verify sonar:sonar -Dsonar.projectKey=cloud-infra-cr_dummy-project_AYraKtXh3P4UYTctu11N -Dsonar.projectName='dummy-project'"
    }
  }
}