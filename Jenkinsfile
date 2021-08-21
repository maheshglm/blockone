/*
   I could not able to execute the pipeline due to infrastructure issues,
   but I came up with Jenkinsfile such a way that with minimum updates,
   we can build the pipeline in Jenkins.
*/

def repo_url = "git@github.com:maheshglm/blockone.git"

pipeline {

    //Assume we have an agent configured with Java11, Maven3 and Docker installed
    agent {
        label 'java11'
    }

    environment {
        DOCKER_HUB_LOGIN = credentials("DOCKER_HUB_CREDS")
        SONAR_LOGIN = credentials("SONARQUBE_CREDS")
    }

    options {
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    triggers {
        pollSCM 'H H 1 12 *'
    }

    stages {

        stage ('Checkout source code') {
            steps {
                gitCheckout url: "${repo_url}"
            }
        }

        stage ('Build app') {
            steps {
                dir ('app/student-api') {
                    sh 'mvn clean package -DskipTests=true'
                }
            }
        }

        stage ('Run unit tests') {
            steps {
                dir ('app/student-api') {
                    sh "mvn test"
                }
            }
        }

        stage ('Run Sonar scan') {
            steps {
                dir ('app/student-api'){
                    script {
                        withSonarQubeEnv {
                            sh "mvn sonar:sonar -Dsonar.login=${SONAR_LOGIN_USR} -Dsonar.password=${SONAR_LOGIN_PSW}"
                        }
                    }
                    timeout(time: 2, unit: 'MINUTES') {
                        retry(3) {
                            script {
                                def qg = waitForQualityGate()
                                if (qg.status != 'OK') {
                                    error "Pipeline aborted due to quality gate failure: ${qg.status}"
                                }
                            }
                        }
                    }
                }
            }
        }

        stage ('Build Docker images') {
            steps {
                dir ('app/student-api') {
                    sh """
                        docker build -t student-api .
                        docker build -t student-mysql-db mysql/
                    """
                }
            }
        }

        stage ('Run Functional tests') {
            steps {
                dir ('app') {
                    sh """
                        echo "Bring up the stack"
                        cd student-api
                        docker-compose up -d

                        echo "Execute cucumber tests"
                        cd ../student-api-tests
                        mvn clean install
                    """
                }
               cucumber buildStatus: null, fileIncludePattern: '**/*.json', jsonReportDirectory: 'testout/reports'
            }
            post {
                always {
                    script {
                        dir ('app/student-api') {
                            sh 'docker-compose down --volumes'
                        }
                    }
                }
            }
        }

        //It can be any docker registry
        stage ('Push images to Docker hub') {
            when {
                branch 'master'
            }
            steps {
                dir ('app/student-api') {
                    sh """
                        docker login --username=$DOCKER_HUB_LOGIN_USR --password=$DOCKER_HUB_LOGIN_PSW

                        docker tag student-api mahesh1985/student-api:0.0.1
                        docker tag student-mysql-db mahesh1985/student-mysql-db:0.0.1

                        docker tag student-api mahesh1985/student-api:latest
                        docker tag student-mysql-db mahesh1985/student-mysql-db:latest

                        docker push mahesh1985/student-api:latest
                        docker push mahesh1985/student-mysql-db:latest

                        docker push mahesh1985/student-api:0.0.1
                        docker push mahesh1985/student-mysql-db:0.0.1
                    """
                }
            }
        }

        stage('Deploy to EC2') {
            when {
                branch 'master'
            }
            steps {
                //1. I can possibly use Terraform or CloudFormation template to spin up an EC2 instance
                //and deploy docker images
                //2. If I have K8 cluster, I can deploy the pods onto it.
                //please find the k8 deployment files at /k8/
            }
        }
    }


    post {
        always {
            emptyDir()
        }

        failure {
            mail()
        }
    }
}


