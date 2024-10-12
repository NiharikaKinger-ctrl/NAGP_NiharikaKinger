pipeline {
    agent any
    stages {
        stage('Checkout'){
        steps{
            echo 'Cloning the repository'
            git branch: 'master', url: 'https://github.com/NiharikaKinger-ctrl/Niharika_NAGP_2024.git'
        }
    }
    stage('Test'){
        steps{
             echo 'Testing the code'
             bat 'mvn clean test'
        }
    }
    }
    post{
        always{
            echo 'this is always going to execute, in case of failure as well'
        }
        success{
            echo 'build success'
        }
        failure{
            echo 'build failed'
        }
    }
}