* Deploy Angular SpringBoot Postgres on K8s

# Step 1: Deploy PostgreSQL cluster

## Step 1.1. Storage class - Alreday present in 1.17 version by default - NO NEED TO EXECUTE

Name: gp2

```
Ref: 
For Read: 
https://docs.aws.amazon.com/eks/latest/userguide/storage-classes.html
https://kubernetes.io/docs/concepts/storage/storage-classes/
cloud-native storage solution Portworx as a daemon set on EKS: https://dzone.com/articles/how-to-run-ha-postgresql-on-amazon-eks
```

```
kubectl get storageclass

kubectl create -f 1-storageclass.yaml
kubectl patch storageclass <gp2> -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'

kubectl get storageclass
```

## Step 1.2. Create a Persistent Volume Claim (PVC) based on the Storage Class

Name: postgres-pv-claim

Ref: 
```
For Read: 
https://kubernetes.io/docs/concepts/storage/dynamic-provisioning/
https://kubernetes.io/docs/concepts/storage/persistent-volumes/
```

```
Update yaml file with below
storageClassName: storageClassName value from step 1 (e.g. gp2)
```

```
kubectl get storageclass

kubectl create -f 2-pv-claim.yaml

kubectl get pvc
```

## Step 1.3. Create ConfigMap for postgres username and password

Name: postgres-config

```
Ref: 
https://kubernetes.io/docs/tasks/configure-pod-container/configure-pod-configmap/
```

```
kubectl create -f 3-configmap.yaml
```

## Step 1.4. Create StatefulSet

Name: postgres-statefulset
 
```
Ref :
https://kubernetes.io/docs/concepts/workloads/controllers/statefulset/
Official posgres: https://hub.docker.com/_/postgres/
ERROR: 
How to mount a postgresql volume using Aws EBS in Kubernete: https://stackoverflow.com/questions/51168558/how-to-mount-a-postgresql-volume-using-aws-ebs-in-kubernete
Using subPath: https://kubernetes.io/docs/concepts/storage/volumes/#using-subpath
```

```
Update yaml file with below
persistentVolumeClaim.claimName : pv-claim name from Step 1.2. (e.g. postgres-pv-claim)
configMapRef.Name				: ConfigMap name from Step 1.3. (e.g. postgres-config)
```

```
kubectl create -f 4-statefulset.yaml
```

## Step 1.5. Create service resource to expose database

Name: postgres-service
type: ClusterIP   
port: 5432 

```
kubectl create -f 5-service.yaml
```

## Step 1.6. combined yaml

```
kubectl create -f resources/postgres.yaml
kubectl get pods
```

## Step 1.7. Create a config map with the hostname of Postgres

This will be used as environment variable in springboot yaml file.
Name : hostname-config

```
kubectl create configmap hostname-config --from-literal=postgres_host=$(kubectl get svc postgres -o jsonpath="{.spec.clusterIP}")
```

# Step 2: Deploy Spring Boot Application

## Step 2.1. Build Sample Java App and create Jar

Artifact : eks-demo-0.0.1-SNAPSHOT.jar
```
start.spring.io 
Dependencies
	Spring Web
	Thymeleaf 
	PostgreSQL Driver
	Spring Data JPA	
```


```
./mvnw -DskipTests package
```

## Step 2.2. Create ECR Repository and authenticate Docker

### a. Create ECR Registory
```
aws ecr create-repository \
     --repository-name artifact-poc \
     --region us-west-2
```

### b. Authenticate Docker to an Amazon ECR registry with get-login-password 
Repository name : artifact-poc
URI : XXXXXXXXX.dkr.ecr.us-west-2.amazonaws.com/artifact-poc

```
aws ecr get-login-password \
     --region us-west-2 | docker login \
     --username AWS \
     --password-stdin aws_account_id.dkr.ecr.region.amazonaws.com/artifact-poc
```

## Step 2.3. Build and Push Docker Image to ECR 

Dockerfile
```
docker build -t XXXXXXXXX.dkr.ecr.us-west-2.amazonaws.com/artifact-poc/spring-boot-postgres-on-k8s:v0.0.1 .
docker push XXXXXXXXX.dkr.ecr.us-west-2.amazonaws.com/artifact-poc/spring-boot-postgres-on-k8s:v0.0.1
```

## Step 2.4. Deploy into k8s

```
Update yaml file with below
hostname-confige : hostname-config value from Step 1.7. (e.g. hostname-config, postgres_host )
postgres_host	 : postgres_host value from Step 1.7. (e.g. postgres_host, POSTGRES_USER, POSTGRES_PASSWORD)
XXXXXXXXX 		 :  with value from Step 2.2 b e.g.(XXXXXXXXX.dkr.ecr.us-west-2.amazonaws.com/artifact-poc)
```

```
kubectl apply -f spring-boot-app.yml

#kubectl expose deployment spring-boot-postgres-poc --type=LoadBalancer --port=8080

```

## Step 2.5 Test
```
kubectl get svc spring-boot-postgres-poc
http://<External IP Address>:8080
```

## Step 2.6 Scale
```
kubectl scale deployment spring-boot-postgres-poc --replicas=3
```

## Step 2.7. Updating your application
```
kubectl set image deployment/spring-boot-postgres-poc spring-boot-postgres-poc=<your Docker Hub account>/spring-boot-postgres-on-k8s:v2

```
## Next : 
## use horizontal pod scaler

## Step 3: Clean UP: 

```
kubectl delete -f specs/spring-boot-app.yml
kubectl delete svc spring-boot-postgres-poc
kubectl delete cm hostname-config

kubectl delete -f resources/postgres.yaml

aws ecr delete-repository \
    --repository-name artifact-poc \
    --force
	
```


# Some commnads
```
echo -n "STRING" | base64
echo U1RSSU5H | base64 --decode
kubectl logs <podname>

kubectl delete service postgres
kubectl delete statefulsets postgres-statefulset
kubectl delete cm postgres-config
kubectl delete pvc postgres-pv-claim

kubectl get pvc
kubectl get cm postgres-config
kubectl get statefulsets postgres-statefulset
kubectl get service postgres
kubectl get pods

aws ecr describe-repositories
aws ecr describe-images --repository-name artifact-test

```


# Rerefence:

## Postgres
### Setting up PostgreSQL DB on K8S : https://medium.com/@suyashmohan/setting-up-postgresql-database-on-kubernetes-24a2a192e962
### spring-boot-postgres-on-k8s: https://github.com/mkjelland/spring-boot-postgres-on-k8s-sample

## Spring Boot
### Deploying Angular + Spring Boot + MangoDB Application in Microsoft Azure Cloud
PART 1 : Spring Boot and Angular web application (https://medium.com/@raghavendra.pes/building-web-application-with-spring-boot-and-angular-853aed3ecfea)
https://medium.com/@raghavendra.pes/kubernetes-deploying-angular-spring-boot-application-in-microsoft-azure-cloud-dd8fb63419c5

## Build a Microservice Architecture with Spring Boot and Kubernetes
https://developer.okta.com/blog/2019/04/01/spring-boot-microservices-with-kubernetes

## Helm Chart
### Deploying PostgreSQL through a Helm Chart: https://thenewstack.io/tutorial-deploy-postgresql-on-kubernetes-running-the-openebs-storage-engine/

## Where is the Postgres username/password being created in this Dockerfile? : https://stackoverflow.com/questions/40599116/where-is-the-postgres-username-password-being-created-in-this-dockerfile


## Developing and deploying Spring Boot microservices on Kubernetes (Freemarker+Spring boot+MongoDB)
https://learnk8s.io/spring-boot-kubernetes-guide

## Deploying Angular + Spring Boot + MangoDB  Application in Google Kubernetes Engine (GKE)
https://medium.com/@raghavendra.pes/deploying-angular-java-spring-boot-application-in-google-kubernetes-engine-gke-b7d96ce084b5


## Steps to Deploy Angular application on Kubernetes
https://blog.mayadata.io/openebs/steps-to-deploy-angular-application-on-kubernetes
https://developer.okta.com/blog/2020/05/29/angular-deployment

## Build a CRUD App with Angular 9 and Spring Boot 2.2
https://dzone.com/articles/angular-docker-spring-boot-a-match-made-in-heaven
https://developer.okta.com/blog/2020/01/06/crud-angular-9-spring-boot-2	// start.spring.io 

## Deploying a full-stack Spring boot, Mysql, and React app on Kubernetes with Persistent Volumes and Secrets
https://www.callicoder.com/deploy-spring-mysql-react-nginx-kubernetes-persistent-volume-secret/

## Spring Log:  https://mkyong.com/spring-boot/spring-boot-slf4j-logging-example/

## on GKS : https://medium.com/javarevisited/kubernetes-step-by-step-with-spring-boot-docker-gke-35e9481f6d5f

## Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-template-engines)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)

## Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

