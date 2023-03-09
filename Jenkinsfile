node {
    stage ("Checkout DataService"){
        git branch: 'main', url: 'https://github.com/herdegen-bah/bah-data-day6.git'
    }
    
    stage ("Gradle Build - DataService") {
	
        sh 'gradle clean build'

    }
    
    stage ("Gradle Bootjar-Package - DataService") {
        sh 'gradle bootjar'
    }
	
	stage ("Containerize the app-docker build - DataApi") {
        sh 'docker build --rm -t data-day7:v1.0 .'
    }
    
    stage ("Inspect the docker image - DataApi"){
        sh "docker images data-day7:v1.0"
        sh "docker inspect data-day7:v1.0"
    }
    
	stage ("Run Docker container instance - DataApi"){
        sh "docker run -d --rm --name data-day7 -p 8080:8080 data-day7:v1.0"
    }
    
	stage('User Acceptance Test - DataApi') {
	
	  def response= input message: 'Is this build good to go?',
	   parameters: [choice(choices: 'Yes\nNo', 
	   description: '', name: 'Pass')]
	
	  if(response=="Yes") {
	    stage('Deploy to Kubenetes cluster - DataApi') {
		  
	      sh "kubectl create deployment data-day7 --image=settlagekl/data-day7:v1.0"
		  sh "kubectl expose deployment data-day7 --type=LoadBalancer --port=8080"
	    }
	  }
    }
    stage ("Production Deployment View"){
    	sh "kubectl get deployments"
    	sh "kubectl get pods"
    	sh "kubectl get services"
    }
}
