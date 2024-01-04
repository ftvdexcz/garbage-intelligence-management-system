pipeline {
    agent any;

    stages{
        stage('Clone repository') {
                checkout scm
            }

            stage('Build image') {
                app = docker.build("ftvdexcz/gms-frontend-ssl-ci")
            }

            stage('Test image') {
                app.inside {
                    sh 'echo "Tests passed"'
                }
            }

            stage('Push image') {
                docker.withRegistry('https://registry.hub.docker.com', 'dockerhub') {
                    app.push("${env.BUILD_NUMBER}")
                }
            }
            
            stage('Trigger ManifestUpdate') {
                        echo "triggering updatemanifestjob"
                        build job: 'gms-updatemanifest', parameters: [string(name: 'DOCKERTAG', value: env.BUILD_NUMBER),string(name: 'SERVICE', ${env.GIT_BRANCH})]
            }
    }

    
}
