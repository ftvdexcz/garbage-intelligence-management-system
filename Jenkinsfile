pipeline {
    agent any;
    environment{
        registry='ftvdexcz'
        imageName="gms-${env.SERVICE}-ci"
        tag="${env.DOCKERTAG}"
        deployFile="${env.SERVICE}-deployment.yml"
    }

    stages{
            stage('Clone repository') {
                steps{
                    checkout scm
                }
            } 

            stage('Update manifest') {
                steps{
                    script {
                        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                        withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                            //def encodedPassword = URLEncoder.encode("$GIT_PASSWORD",'UTF-8')
                            sh "git config user.email ftvdexc95@gmail.com"
                            sh "git config user.name longdq"
                            //sh "git switch master"
                            sh "cd ./service-cd"
                            sh "ls"
                            sh "cat ${deployFile}"
                            sh "sed -i 's+${registry}/${imageName}.*+${registry}/${imageName}:${tag}+g' ${deployFile}"
                            sh "cat ${deployFile}"
                            sh "git add ."
                            sh "git commit -m 'Done by Jenkins Job changemanifest ${deployFile}: ${tag}'"
                            sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/${GIT_USERNAME}/garbage-intelligence-management-system.git HEAD:k8s"
                            }   
                        }
                    }
                }                
            }
    }

    
}
