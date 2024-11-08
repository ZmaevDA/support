pipeline {
    agent any

    environment {
        USERNAME = credentials('github-username')
        TOKEN = credentials('github-token')
        SONAR_TOKEN = credentials('sonar-token')
        SONAR_URL = credentials('sonar-url')
    }

    stages {
        stage('Checkout new branch') {
            steps {
                git url: 'ssh://git@git.jetbrains.space/rcmentorship/zmaev-daniil/support.git',
                branch: env.BRANCH_NAME
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
            }
        }

        stage('Unit Tests') {
            steps {
                sh './gradlew test --tests "ru.zmaev.unit.*"'
            }
        }

        stage('SonarQube') {
            when {
                expression {
                    return !env.BRANCH_NAME.equalsIgnoreCase('main')
                }
            }
            steps {
                sh './gradlew sonar'
            }
        }

        stage('Deploy') {
            when {
                expression {
                    return env.BRANCH_NAME.equalsIgnoreCase('main')
                }
            }
            steps {
                sh """
                echo "Запуск Spring Boot приложения с профилем dev"
                nohup java -jar build/libs/support-deploy-1.0-SNAPSHOT.jar --spring.profiles.active=dev > ~/app.log 2>&1 &
                """
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
            cleanWs()
        }
    }
}
