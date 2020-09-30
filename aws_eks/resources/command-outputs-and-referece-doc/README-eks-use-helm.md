# Using Helm with Amazon EKS
Helm package manager for Kubernetes helps to install and manage applications on Kubernetes cluster

# Pre-requisite
- Create EKS Cluster and node group and Configure kubectl to work for Amazon EKS 
	```
	Refer: 
	https://github.com/koolkravi/aws/blob/master/aws_eks/README.MD or
	https://github.com/koolkravi/aws/blob/master/aws_eks/resources/command-outputs-and-referece-doc/REAMME-using-eksctl.txt
	```
	```
	kubectl get svc
	```

# Step 1: Install the Helm binaries on local system
```
choco install kubernetes-helm
```
Test
```
helm help
```

# Step 2: Install an example chart

```
helm repo add stable https://kubernetes-charts.storage.googleapis.com/
helm repo update
helm search repo stable
helm install stable/mysql --generate-name

helm show chart stable/mysql
helm show all stable/mysql

helm ls
helm uninstall mysql-1601477868 
helm status mysql-1601477868 
```

# Step 3: Create an example chart and push it to Amazon ECR
Steps to push a Helm chart to an Amazon ECR repository

## 3.1: Install the Helm client version 3
## 3.2: Enable OCI support in the Helm 3 client
```
export HELM_EXPERIMENTAL_OCI=1
```
## 3.3: Create a repository to store your Helm chart
```
aws ecr create-repository \
     --repository-name artifact-test \
     --region us-west-2
```
## 3.4: Authenticate Helm client to the Amazon ECR registry to which you intend to push your Helm chart. 
Authentication tokens must be obtained for each registry used, and the tokens are valid for 12 hours

```
aws ecr get-login-password \
     --region us-west-2 | helm registry login \
     --username AWS \
     --password-stdin aws_account_id.dkr.ecr.region.amazonaws.com/artifact-test

```
## 3.5:Use the following steps to create a test Helm chart
- a. Create a directory named helm-tutorial to work in.
```
mkdir helm-tutorial
cd helm-tutorial
```
- b. Create a Helm chart named mychart and clear the contents of the templates directory.
```
helm create mychart
rm -rf ./mychart/templates/*
```
- c. Create a ConfigMap in the templates folder.
```
cd mychart/templates
cat <<EOF > configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: mychart-configmap
data:
  myvalue: "Hello World"
EOF
```

## 3.6: Save the chart locally and create an alias for the chart with your registry URI.

```
cd ..
helm chart save . mychart
helm chart save . aws_account_id.dkr.ecr.us-west-2.amazonaws.com/artifact-test:mychart


```
## 3.7: Identify the Helm chart to push. Run the helm chart list command to list the Helm charts on your system.
```
helm chart list
```

## 3.8: Push the Helm chart using the helm chart push command:
```
helm chart push aws_account_id.dkr.ecr.region.amazonaws.com/artifact-test:mychart
```

## 3.9: Describe your Helm chart.
```
aws ecr describe-images \
     --repository-name artifact-test \
     --region us-west-2
```

# 4. Install an Amazon ECR hosted Helm chart to an Amazon EKS cluster
Steps
ref : https://docs.aws.amazon.com/AmazonECR/latest/userguide/ECR_on_EKS.html
```
aws ecr describe-repositories
aws ecr describe-images --repository-name artifact-test
```
## 4.1: Pull your Helm chart to your local cache.
```
helm chart pull aws_account_id.dkr.ecr.region.amazonaws.com/repository-name:mychart

```
## 4.2:  Export the chart to a local directory. In this example, we use a directory named charts.
```
helm chart export aws_account_id.dkr.ecr.region.amazonaws.com/repository-name:mychart --destination ./charts

helm chart export 770239628917.dkr.ecr.us-west-2.amazonaws.com/artifact-test:mychart --destination ./charts
```

## 4.3: Install the chart.
```
cd ..
helm install ecr-chart-demo ./mychart
```

## 4.4: Verify the chart installation. The output will be a YAML representation of the Kubernetes resources deployed by the chart.
```
helm get manifest ecr-chart-demo
```

## 4.5: (Optional) See your Helm chart running in your Amazon EKS pod.
```
kubectl get pods --all-namespaces
```
## 4.6 : When you are finished, you can remove the chart release from your cluster.

```
helm uninstall ecr-chart-demo
```

# 5. Install an Amazon EKS chart from the eks-charts GitHub repo or from Helm Hub
```
https://github.com/aws/eks-charts
https://hub.helm.sh/charts?q=eks
```

# 6. clean up

```
aws ecr delete-repository \
    --repository-name artifact-test \
    --force
	
eksctl delete cluster --name eks-cluster
```



# some commands
```
helm help
helm version
helm get -h

```

# Reference
## 1.https://docs.aws.amazon.com/eks/latest/userguide/helm.html
## 2.https://helm.sh/docs/intro/quickstart/#install-an-example-chart
## 3.Pushing a Helm chart (https://docs.aws.amazon.com/AmazonECR/latest/userguide/push-oci-artifact.html)
## 4.Creating a repository (https://docs.aws.amazon.com/AmazonECR/latest/userguide/repository-create.html)
## 5.Registry authentication(https://docs.aws.amazon.com/AmazonECR/latest/userguide/Registries.html#registry_auth)

# NEXT
- Using Amazon ECR Images with Amazon EKS (https://docs.aws.amazon.com/AmazonECR/latest/userguide/ECR_on_EKS.html) 
