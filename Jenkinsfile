pipeline{
 agent any;

 stages{
    stage('build'){
        steps{
            checkout scm
            sh 'mvn clean install'
        }
    }
 }
}