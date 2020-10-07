Create Amazon EKS clusters using eksctl

# Prerequisites
- Install the AWS CLI
- Install eksctl
- Install and configure kubectl

# Create your Amazon EKS cluster and compute
Latest Kubernetes version available in Amazon EKS is installed

There are two options
## Option 1. Create your Amazon EKS cluster and Linux nodes without a launch template
## Option 2. Create your Amazon EKS cluster and Amazon Linux nodes with a launch template
The launch template must already exist.
```
eksctl create cluster --config-file eksctl-resources/1-cluster-node-group-lt.yaml
```

We will use 1st option 1
## Step 1: Option 1 Create your Amazon EKS cluster and Linux nodes without a launch template

```
eksctl create cluster \
--name eks-cluster \
--version 1.17 \
--region us-west-2 \
--nodegroup-name linux-nodes \
--node-type t3.small \
--nodes 3 \
--nodes-min 1 \
--nodes-max 4 \
--ssh-access \
--ssh-public-key ravieksawskey \
--managed
```

Above create 2 separate CloudFormation stacks for cluster itself and the initial managed nodegroup
eksctl-resources/ksctl-eks-cluster-cluster.json
eksctl-resources/eksctl-eks-cluster-nodegroup-linux-nodes.json

## Step 2:  Check  
```
kubectl get svc
kubectl get nodes -o wide
```

## Step 3: clean up

3 ways
### Option 1: To delete an Amazon EKS cluster and nodes with eksctl 
### Option 2: To delete an Amazon EKS cluster with the AWS Management Console
### Option 3: To delete an Amazon EKS cluster with the AWS CLI 

### Option 1: To delete an Amazon EKS cluster and nodes with eksctl 

- List all services running in your cluster.
	```
	kubectl get svc --all-namespaces
	```

- Delete any services that have an associated EXTERNAL-IP value. These services are fronted by an Elastic Load Balancing load balancer, 
and you must delete them in Kubernetes to allow the load balancer and associated resources to be properly released.
	```
	kubectl delete svc <service-name>
	```

- Delete the cluster and its associated nodes with the following command, replacing <prod> with your cluster name.
	```
	eksctl delete cluster --name eks-cluster
	```


# important commands
```
aws --version
aws configure
eksctl version
kubectl version --short --client
```

# Reference 
1. https://docs.aws.amazon.com/eks/latest/userguide/getting-started-eksctl.html