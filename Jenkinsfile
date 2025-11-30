pipeline {
    agent any

    environment {
        IMAGE_NAME = "referenced-payments-api"
        COMPOSE_FILE = "docker-compose-backend.yml"
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

        stage('Test') {
            steps {
                sh './gradlew test'
            }
            post {
                always {
                    junit '**/build/test-results/test/*.xml'
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
                // forzar recreate para aplicar nueva imagen
                sh "docker-compose -f ${COMPOSE_FILE} up -d --remove-orphans --build"
            }
        }

        stage('Smoke Tests') {
            steps {
                // prueba rápida para validar que el API responde
                sh '''
                echo "Esperando 5s para que el API arranque..."
                sleep 5
                curl -f http://referenced-payments-api:8080/actuator/health || curl -f http://localhost:8080/actuator/health
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