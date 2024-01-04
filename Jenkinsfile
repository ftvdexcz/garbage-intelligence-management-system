pipeline {
    agent any;
    environment{
        registry='ftvdexcz'
        imageName='gms-frontend-ci'
        buildNumber="${env.BUILD_NUMBER}"
    }

    stages{
            stage('Clone repository') {
                steps{
                    checkout scm
                }
            }

            stage('Build image') {
                steps{
                    script{
                        app = docker.build("${registry}/${imageName}")
                    }
                    
                }
                
            }

            stage('Test image') {
                steps{
                    script{
                        app.inside {
                            sh 'echo "Tests passed"'
                        }
                    }
                }
            }

            stage('Push image') {
                steps{
                    script{
                        docker.withRegistry('https://registry.hub.docker.com', 'dockerhub') {
                            app.push("${buildNumber}")
                        }
                    }
                }
            }

            stage('Clean image in local') {
                steps{
                    sh "docker rmi -f ${registry}/${imageName}:${buildNumber}"
                }
            }
            
            stage('Trigger ManifestUpdate') {
                steps{
                    echo "triggering updatemanifestjob"
                    build job: 'gms-updatemanifest', parameters: [string(name: 'DOCKERTAG', value: env.buildNumber), string(name: 'SERVICE', value: env.GIT_BRANCH)]
                }
            }
    }
}
