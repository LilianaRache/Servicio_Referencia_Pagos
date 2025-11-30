pipeline {
    agent any

    environment {
        IMAGE_NAME = "referenced-payments-api"
        COMPOSE_FILE = "docker-compose.yml"
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
                // usa gradle wrapper
                sh './gradlew clean build -x test'
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
                // construye la imagen usando el docker del host (por el socket montado)
                sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Run Docker Compose') {
            steps {
                sh "docker-compose -f ci/docker-compose.ci.yml down || true"
                sh "docker-compose -f ci/docker-compose.ci.yml up -d --remove-orphans"
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

        stage('Upload coverage to Codecov') {
            steps {
                sh '''
                    curl -Os https://uploader.codecov.io/latest/linux/codecov
                    chmod +x codecov
                    ./codecov -t $CODECOV_TOKEN
                '''
            }
        }
    }

    post {
        success {
            echo "Build y deploy OK"
            // puedes añadir notificaciones aquí (email, slack, etc)
        }
        failure {
            echo "Fallo en pipeline"
            // notificaciones de fallo
        }
    }
}