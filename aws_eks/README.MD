AWS Elastic Kubernetes Service

This example usases cloud formation template create AWS EKS control plane cluster, worker nodes and deploy a sample application 

# High Level Tasks 

- Step A. Create EKS CLuster
- Step B. Create EKS Worker Nodes
- Step C. Deploy an application 

# Implementation:
AWS Region: US West (Oregon)us-west-2

# Step A : Create EKS CLuster 

## Step 1: Create IAM role for EKS Cluster - AmazonEKSClusterPolicy

CloudFormation Template
Stack name: eksClusterRole
```
1_cloudFormation_eksClusterRole.yaml
```

Outputs:
RoleArn	=  Outputs value from stack  (e.g. arn:aws:iam::770239628917:role/eksClusterRole-eksClusterRole-1WP3CBHY331MN)

## Step 2: Create Dedicated VPC for the EKS Cluster
https://amazon-eks.s3.us-west-2.amazonaws.com/cloudformation/2020-08-12/amazon-eks-vpc-private-subnets.yaml

Stack name: eks-vpc
```
2_cloudFormation_amazon-eks-vpc-private-subnets.yaml
```
Outputs:
SecurityGroups	: 	Outputs value from stack (e.g. sg-02db2e8207e8143a3)	
SubnetIds 		: 	Outputs value from stack (e.g. subnet-0c64cd521f1b99fe4,subnet-0a55114cf202df088,subnet-00eca565a5ae026e1,subnet-0c12781233356d113)
VpcId			:	Outputs value from stack (e.g. vpc-01a43230808dac879)

## Step 3: Create EKS Cluster

CloudFormation Template
Stack name		: eks-cluster
Cluster name 	: eks-cluster

```
Update below cloudFormation template 
for RoleArn value from step 1
for SecurityGroupIds from step 2
for SubnetIds from step 2

Note: Cluster endpoint access
Default behavior for new Amazon EKS clusters.
Endpoint public access= Enabled and Endpoint private access= Disabled
```

```
3_cloudFormation_ekscluster.yaml
```
Outputs:
Arn							: Outputs value from stack (e.g. arn:aws:eks:us-west-2:770239628917:cluster/eks-cluster	)	
CertificateAuthorityData	: Outputs value from stack (e.g. LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUN5RENDQWJDZ0F3SUJBZ0lCQURBTkJna3Foa2lHOXcwQkFRc0ZBREFWTVJNd0VRWURWUVFERXdwcmRXSmwKY201bGRHVnpNQjRYRFRJd01Ea3lPVEV3TXpnMU1Gb1hEVE13TURreU56RXdNemcxTUZvd0ZURVRNQkVHQTFVRQpBeE1LYTNWaVpYSnVaWFJsY3pDQ0FTSXdEUVlKS29aSWh2Y05BUUVCQlFBRGdnRVBBRENDQVFvQ2dnRUJBTFR6CjlyNWhuYU9TNGNDRXY5VmRyQXFRMnd5TnNkU3VKR0NTS2EvamJ5UERwOTl0QnNyWlBjczdBcmQwYW5RWjVrcTcKb3ZzV2dJR1FBQm4zeTBVaThrajMrSGdOeVJiWHdoRllHVnBrclhIdm1IaG45NjBkaTZrbm5ZWFNwN0V2eFZ5bwo2T3Z4eUp4alN5alNZQ0xmbTI2YTM1a1hidkRSRlFPc2ZzR0RXdHpzQ29HblRUenIweHlLR1dqMXNGdFBUeUtiClJ2ZTZpYkM1Sm1VOHpEYmE3ZDFrOGNkcDNRUUQvL0MrM0FvRHFTaVF3ZmtydlY5TytMUnBxTVBDN0U4U09xM2IKcWlFRFJVanA2YmpoTGJFb0d4S3h4a1RBTzJPU0pUQzBQYVF6Tm1WdDdzL1RXUjJsRnZJbzBhek1POVp1cjByRAowTmgzVFR4R0w3MCtlMHRQZ2pVQ0F3RUFBYU1qTUNFd0RnWURWUjBQQVFIL0JBUURBZ0trTUE4R0ExVWRFd0VCCi93UUZNQU1CQWY4d0RRWUpLb1pJaHZjTkFRRUxCUUFEZ2dFQkFIZmVwNEdxb3hQS1Jqb3A1ZW5wTkp4N05wMDkKTmswRWFDaEVrTnFnM1RzZXBhNXVuS0NERDZZNlZBaWhtcExad1Y1bld1VE5uSnozeGhQc3JMUTRiWE1KV1lLRQorOVAxTWxpb09tMmU5aVZqVU9JZHJGTGNSN1VJaE1vSG82Q3pqNlJWeVJhSDdzUkFjVmlUb21KTks0ZTdzTDM2ClF0dW1xWko2c2x1Q0h6Wk1QSXV5UzdCUFplS2tiNldNSFNOWDJGN245NHlhMXViOW4yRi9Fdk5NczVuOE4xNEUKc2h0amR4YTdrRGRiYWU0YzZNRm52dnBNenJiYXlFQitrUDN4NUhibVJmNG0vWFhiK1pNdTF4RHRtOGJRQnVpOApuNHgrOUNldTVXbHQ3RXd6eXhraU9mZkRRQ21UQWY2V1M0cmhSMlIyM3YzMUowck9HVnJpYk5pamNlZz0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=	)	
ClusterSecurityGroupId		: Outputs value from stack (e.g. sg-0ce012c49e4097d99	)	
Endpoint					: Outputs value from stack (e.g. https://5AE0ABA6DFABE554800244AE2FB44541.sk1.us-west-2.eks.amazonaws.com)	
eksCluster					: Outputs value from stack (e.g. eks-cluster)


## Step 4: Install & Setup IAM Authenticator and Kubectl Utility
1. install aws cli
2. install kubectl
3. install iam authenticator (not needed)

```
aws  eks --region us-west-2 update-kubeconfig --name eks-cluster
kubectl get svc
```

# Step B. Create EKS Worker Nodes

## Step 5: Create IAM Role for EKS Worker Nodes
CloudFormation Template
Stack name		: eksWorkerNodeGroupRole

AmazonEKS_CNI_Policy
AmazonEKSWorkerNodePolicy
AmazonEC2ContainerRegistryReadOnly


```
4_cloudFormation_amazon-eks-nodegroup-role.yaml
```

Outputs:
NodeInstanceRole	=  Outputs value from stack  (e.g. arn:aws:iam::770239628917:role/eksWorkerNodeGroupRole-NodeInstanceRole-XRWMXWZT5275)


## Step 6: Create Worker nodes

CloudFormation Template
Stack name		: eks-worker-node-group

Update below cloudFormation template 
for ClusterName= eks-cluster from step 3
for NodeGroupRoleArn with value of NodeInstanceRole from step 5
for SubnetIds withe values form step 2
and upadate Ec2SshKey value with your ssh key

```
5_cloudFormation_eksnodegroup.yaml
```

Outputs:
Arn:arn:aws		:Outputs value from stack (e.g.eks:us-west-2:770239628917:nodegroup/eks-cluster/EKSNodegroup-4VAQ5tzAckMJ/7aba6cde-304f-3637-a6f1-2c2d15f0e096)
ClusterName		:Outputs value from stack (e.g.eks-cluster)
NodegroupName	:Outputs value from stack (e.g.EKSNodegroup-4VAQ5tzAckMJ)
```
kubectl get nodes -0 wide
kubectl get nodes --watch
```

# Step C. Deploy application 

## Step 7: Deploying Demo Application

### 7.1 Run Frontend:
```
kubectl apply -f knote.yaml
```

### 7.2 Run Backend:
```
kubectl apply -f mongo.yaml
```

```
$ kubectl get pods -o wide
$ kubectl get deploy
$ kubectl get svc
$ nslookup a9b8f5e3839244aa194072cd8f6da53a-778786078.us-west-2.elb.amazonaws.com
```

### 7.2 Test 
```
curl External IP (e.g. a9b8f5e3839244aa194072cd8f6da53a-778786078.us-west-2.elb.amazonaws.com)

run from browser
External IP (e.g. a9b8f5e3839244aa194072cd8f6da53a-778786078.us-west-2.elb.amazonaws.com)
```

# Step D. Clean Up

## Step 8: Clean update-kubeconfig

Delete Stack eks-worker-node-group
Delete Stack eksWorkerNodeGroupRole

Delete Stack eks-cluster
Delete Stack eksClusterRole

Delete Stack  eks-vpc

# Some Commands
-------------------------------
```
aws  eks --region us-west-2 update-kubeconfig --name eks-cluster
kubectl get svc

kubectl get nodes -0 wide
kubectl get nodes --watch

kubectl apply -f knote.yaml
kubectl apply -f mongo.yaml

$ kubectl get pods -o wide
$ kubectl get deploy
$ kubectl get svc

$ nslookup External IP (a9b8f5e3839244aa194072cd8f6da53a-778786078.us-west-2.elb.amazonaws.com)

aws --version //aws-cli/2.0.52 Python/3.7.7 Windows/10 exe/AMD64
aws iam list-users
aws-iam-authenticator help
kubectl version
```

# Ref: 
- 1. https://docs.aws.amazon.com/eks/latest/userguide/getting-started-console.html
- 2. CLoud formation reference: https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-template-resource-type-ref.html
- 3. Others
	---------------------------------------
	https://www.youtube.com/watch?v=aZd0UolVwD4
	https://github.com/learnitguide/kubernetes-knote
	https://github.com/aws-quickstart/quickstart-amazon-eks
	https://github.com/aws-quickstart/quickstart-amazon-eks-cluster-resource-provider
	https://docs.aws.amazon.com/eks/latest/userguide/create-public-private-vpc.html
	https://docs.aws.amazon.com/eks/latest/userguide/cluster-autoscaler.html
	-------------------------------------------

- 4. Next : sample
	```
	deploy guestbook Using aws console  : https://medium.com/avmconsulting-blog/deploying-a-kubernetes-cluster-with-amazon-eks-9455e7e7828
	deploy wordpress Using eksctl  	: https://levelup.gitconnected.com/create-a-kubernetes-cluster-and-deploy-an-app-on-amazon-eks-using-aws-cli-31fc5e296b3d

	```
- 5. Deploy guestbook  public sample application
	```
	kubectl apply -f https://raw.githubusercontent.com/kubernetes/kubernetes/v1.10.3/examples/guestbook-go/redis-master-controller.json
	kubectl apply -f https://raw.githubusercontent.com/kubernetes/kubernetes/v1.10.3/examples/guestbook-go/redis-master-service.json
	kubectl apply -f https://raw.githubusercontent.com/kubernetes/kubernetes/v1.10.3/examples/guestbook-go/redis-slave-controller.json
	kubectl apply -f https://raw.githubusercontent.com/kubernetes/kubernetes/v1.10.3/examples/guestbook-go/redis-slave-service.json
	kubectl apply -f https://raw.githubusercontent.com/kubernetes/kubernetes/v1.10.3/examples/guestbook-go/guestbook-controller.json
	kubectl apply -f https://raw.githubusercontent.com/kubernetes/kubernetes/v1.10.3/examples/guestbook-go/guestbook-service.json
	kubectl get services -o wide
	```
	
- 6.  Deploy the Cluster Autoscaler
	  https://docs.aws.amazon.com/eks/latest/userguide/cluster-autoscaler.html
	````
	#Pre-requisite
		##Installing Chocolatey.
		choco	
		##insall eksctl
		chocolatey install -y eksctl 
		eksctl version

	#Deploy the Cluster Autoscaler
		##Node group IAM policy
			6_cloudFormation_amazon-eks-nodegroup-role_with_cluster_autoscaler_policy.yaml

		kubectl apply -f https://raw.githubusercontent.com/kubernetes/autoscaler/master/cluster-autoscaler/cloudprovider/aws/examples/cluster-autoscaler-autodiscover.yaml
		kubectl -n kube-system annotate deployment.apps/cluster-autoscaler cluster-autoscaler.kubernetes.io/safe-to-evict="false"

		kubectl -n kube-system logs -f deployment.apps/cluster-autoscaler
	```
	
	
	TODO
	https://aws.amazon.com/blogs/infrastructure-and-automation/using-aws-cloudformation-to-deploy-software-into-amazon-eks-clusters/
	Getting started with eksctl : https://docs.aws.amazon.com/eks/latest/userguide/getting-started-eksctl.html