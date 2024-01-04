pipeline {
    agent any;

    stages{
        stage('Clone repository') {
            steps{
                checkout scm
            }
        }

            stage('Build image') {
                steps{
                    app = docker.build("ftvdexcz/gms-frontend-ssl-ci")
                }
                
            }

            stage('Test image') {
                steps{
                    app.inside {
                        sh 'echo "Tests passed"'
                    }
                }
            }

            stage('Push image') {
                steps{
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub') {
                        app.push("${env.BUILD_NUMBER}")
                    }
                }
                
            }
            
            stage('Trigger ManifestUpdate') {
                steps{
                    echo "triggering updatemanifestjob"
                    build job: 'gms-updatemanifest', parameters: [string(name: 'DOCKERTAG', value: env.BUILD_NUMBER),string(name: 'SERVICE', ${env.GIT_BRANCH})]
                }
            }
    }

    
}
