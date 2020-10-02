# Deploy Angular SpringBoot Postgres on K8s

# Step 1: Deploy PostgreSQL cluster

## Step 1.1. Storage class - Alreday present in 1.17 version by default - NO NEED TO EXECUTE

name: gp2

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

name: postgres-pv-claim

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

name: postgres-config

```
Ref: 
https://kubernetes.io/docs/tasks/configure-pod-container/configure-pod-configmap/
```

```
kubectl create -f 3-configmap.yaml
```

## Step 1.4. Create StatefulSet

name: postgres-statefulset
 
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

name: postgres-service
type: ClusterIP   
port: 5432 

```
kubectl create -f 5-service.yaml
```

## Step 1.6. combined yaml

```
kubectl create -f postgres.yaml
kubectl get pods
``

## Step 1.7. Create a config map with the hostname of Postgres
This will be used as environment variable in springboot yaml file.
name : hostname-config
```
kubectl create configmap hostname-config --from-literal=postgres_host=$(kubectl get svc postgres -o jsonpath="{.spec.clusterIP}")
```

# Step 2: Deploy Spring Boot Application

## Step 2.1. Build Sample Java App and create Jar and create Docker Image 

```
mvn package
docker build -t <your Docker Hub account>/spring-boot-postgres-on-eks-k8s:v0.0.1 .
docker push <your Docker Hub account>/spring-boot-postgres-on-eks-k8s:v0.0.1
```

## 2.3. Deploy into k8s

```
Update yaml file with below
hostname-confige : hostname-config value from Step 1.7. (e.g. hostname-config)
postgres_host	 : postgres_host value from Step 1.7. (e.g. postgres_host)
```

```
kubectl create -f spring-boot-app.yml
kubectl expose deployment spring-boot-postgres-sample --type=LoadBalancer --port=8080
kubectl get svc spring-boot-postgres-sample
```

## 2.3 Test
```
http://<External IP Address>:8080
```

## 2.4 Scale
```
kubectl scale deployment spring-boot-postgres-sample --replicas=3
```

## 2.5. Updating your application
```
kubectl set image deployment/spring-boot-postgres-sample spring-boot-postgres-sample=<your Docker Hub account>/spring-boot-postgres-on-k8s:v2

```
## Next : 
## use horizontal pod scaler

## Step 3: Clean UP: 

```
kubectl delete -f specs/spring-boot-app.yml
kubectl delete svc spring-boot-postgres-sample
kubectl delete cm hostname-config

kubectl create -f postgres.yaml
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
```


# Rerefence:

## Setting up PostgreSQL DB on K8S : https://medium.com/@suyashmohan/setting-up-postgresql-database-on-kubernetes-24a2a192e962
## spring-boot-postgres-on-k8s: https://github.com/mkjelland/spring-boot-postgres-on-k8s-sample


## Deploying PostgreSQL through a Helm Chart: https://thenewstack.io/tutorial-deploy-postgresql-on-kubernetes-running-the-openebs-storage-engine/
## Where is the Postgres username/password being created in this Dockerfile? : https://stackoverflow.com/questions/40599116/where-is-the-postgres-username-password-being-created-in-this-dockerfile



http://consoledotblog.com/2016/02/packaging-spring-boot-applications-for-deployment-on-kubernetes/
https://learnk8s.io/spring-boot-kubernetes-guide
https://www.callicoder.com/deploy-spring-mysql-react-nginx-kubernetes-persistent-volume-secret/


https://github.com/mkjelland/spring-boot-postgres-on-k8s-sample
https://thenewstack.io/tutorial-deploy-postgresql-on-kubernetes-running-the-openebs-storage-engine/
https://portworx.com/postgres-kubernetes/
https://blog.mayadata.io/openebs/steps-to-deploy-angular-application-on-kubernetes
https://medium.com/@raghavendra.pes/deploying-angular-java-spring-boot-application-in-google-kubernetes-engine-gke-b7d96ce084b5
https://developer.okta.com/blog/2020/05/29/angular-deployment
https://dzone.com/articles/angular-docker-spring-boot-a-match-made-in-heaven
