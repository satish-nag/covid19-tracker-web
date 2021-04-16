pipeline{
 agent any;
 checkout scm

 stages{
    stage('build'){
        steps{
            sh 'mvn clean install'
        }
    }
 }
}