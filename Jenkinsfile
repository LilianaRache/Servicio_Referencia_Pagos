pipeline {
    agent any

    environment {
        IMAGE_NAME = "referenced-payments-api"
        COMPOSE_FILE = "ci/docker-compose.ci.yml"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: 'github-token',
                    url: 'https://github.com/LilianaRache/Servicio_Referencia_Pagos.git'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test jacocoTestReport'
            }
            post {
                always {
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Upload coverage to Codecov') {
            steps {
                withCredentials([string(credentialsId: 'CODECOV_TOKEN', variable: 'CODECOV_TOKEN')]) {
                    sh '''
                        curl -Os https://uploader.codecov.io/latest/linux/codecov
                        chmod +x codecov
                        ./codecov -f build/reports/jacoco/test/jacocoTestReport.xml -t $CODECOV_TOKEN
                    '''
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Run Docker Compose') {
            steps {
                sh "docker-compose -f ${COMPOSE_FILE} down || true"
                sh "docker-compose -f ${COMPOSE_FILE} up -d --remove-orphans"
            }
        }

        stage('Smoke Tests') {
            steps {
                sh '''
                echo "Esperando 5s para que el API arranque..."
                sleep 5
                curl -f http://referenced-payments-api:8080/v1/health || curl -f http://localhost:8080/v1/health
                '''
            }
        }
    }

    post {
        success {
            echo "Build y deploy OK"
        }
        failure {
            echo "Fallo en pipeline"
        }
    }
}