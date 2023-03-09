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
	
	stage("Delete Previous Env - DataApi"){
		sh 'kubectl delete deployment data-day7 || true'
		sh 'kubectl delete service data-day7 || true'
		sh 'docker rm data-day7 || true'
		sh 'docker rmi data-day7 || true'
	}
	
	stage ("Build the Docker image - DataApi"){
		sh "docker build --rm -t settlagekl/data-day7:v1.0 ."
	}
	stage ("Inspect the docker image - DataApi"){
		sh "docker images data-day7:v1.0"
		sh "docker inspect data-day7:v1.0"
	}
    
	stage('User Acceptance Test - DataApi') {
	
	  def response= input message: 'Is this build good to go?',
	   parameters: [choice(choices: 'Yes\nNo', 
	   description: '', name: 'Pass')]
	
	  if(response=="Yes") {
	    stage('Deploy to Kubernetes cluster - DataApi') {
		  
	      sh "kubectl create deployment data-day7 --image=data-day7:v1.0"
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
