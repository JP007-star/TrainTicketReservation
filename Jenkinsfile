pipeline {
   agent any

   tools {
        maven "maven"
   }

     stages {
        stage('Initialize') {
             steps {
                echo "Hello World"
                echo " we are in build no -  ${BUILD_NUMBER}"
             }
        }

        stage('Run Test ') {
              steps {
                echo "Hello World"
                echo "${JOB_NAME}"
              }
        }
     }
}