Creating a new Kubernetes cluster with nodes in Amazon EKS

# Getting started with the AWS Management Console
# Prerequisites

- 1. Install the AWS CLI
  A command line tools for working with AWS services, including Amazon EKS.
  https://awscli.amazonaws.com/AWSCLIV2.msi
```
aws --version
aws-cli/2.0.52 Python/3.7.7 Windows/8.1 exe/AMD64
```

- 2. Install and configure kubectl
  A command line tool for working with Kubernetes clusters.
  Kubernetes uses the kubectl command-line utility for communicating with the cluster API server
```
curl -o kubectl.exe https://amazon-eks.s3.us-west-2.amazonaws.com/1.17.9/2020-08-04/bin/windows/amd64/kubectl.exe

kubectl version --short --client
Client Version: v1.17.9-eks-4c6976
```

- 3. Create your Amazon EKS cluster IAM role 
A role allows Kubernetes clusters managed by Amazon EKS to make calls to other AWS services on your behalf to manage the resources that you use with the service

Using IAM console 
IAM - > Roles - > Create role ->  EKS (Services) - >  EKS - Cluster -> Permission (AmazonEKSClusterPolicy) - Review
Role Name: eksClusterRole

OR

Using CloudFormation
CloudFormation ->  Create stack - > Upload a template file - > NAME = eksClusterRole -> CREATE
```
CloudFormation_eksClusterRole.yaml
```
out:
RoleArn
arn:aws:iam::770239628917:role/eksClusterRole-eksClusterRole-16O2Q5R6V8M5N

https://s3.us-east-2.amazonaws.com/cf-templates-qklvv2s3hls2-us-east-2/2020271bWx-CloudFormation_eksClusterRole.yaml


- 4. Create Amazon EKS cluster VPC

Amazon EKS requires subnets in at least two Availability Zones
AWS recommend a VPC with public and private subnets so that Kubernetes can create public load balancers in the public subnets that load balance traffic to pods running on nodes that are in private subnets

VPC with public and private subnets

Region: US East (Ohio)us-east-2

1.1 VPC 					-> AWS::EC2::VPC
1.2 InternetGateway 		-> AWS::EC2::InternetGateway
1.3 VPCGatewayAttachment	-> AWS::EC2::VPCGatewayAttachment

1.4 PublicRouteTable		-> AWS::EC2::RouteTable

1.5 PrivateRouteTable01		-> AWS::EC2::RouteTable
1.6 PrivateRouteTable02		-> AWS::EC2::RouteTable

1.7. PublicRoute 			-> AWS::EC2::Route
DestinationCidrBlock	GatewayId					RouteTableId
0.0.0.0/0				InternetGateway(1.2)		PublicRouteTable ID(1.4)

1.8. PrivateRoute01			-> AWS::EC2::Route
DestinationCidrBlock	NatGatewayId					RouteTableId			NatGatewayId
0.0.0.0/0				NatGateway01(??)			PrivateRouteTable01(1.5)	NatGateway01
1.9. PrivateRoute01			-> AWS::EC2::Route
DestinationCidrBlock	NatGatewayId					RouteTableId
0.0.0.0/0				NatGateway02(??)			PrivateRouteTable02(1.6)	NatGateway02

1.10 NatGateway01			-> AWS::EC2::NatGateway
1.11 NatGateway02			-> AWS::EC2::NatGateway

1.12 NatGatewayEIP1			->AWS::EC2::EIP
1.13 NatGatewayEIP2			->AWS::EC2::EIP

1.14 PublicSubnet01->AWS::EC2::Subnet
1.15 PublicSubnet02->AWS::EC2::Subnet

1.16 PrivateSubnet01->AWS::EC2::Subnet
1.17 PrivateSubnet02->AWS::EC2::Subnet

1.18 PublicSubnet01RouteTableAssociation->AWS::EC2::SubnetRouteTableAssociation
SubnetId			RouteTableId
PublicSubnet01		PublicRouteTable
1.19 PublicSubnet02RouteTableAssociation->AWS::EC2::SubnetRouteTableAssociation
SubnetId			RouteTableId
PublicSubnet02		PublicRouteTable

1.20 PrivateSubnet01RouteTableAssociation->AWS::EC2::SubnetRouteTableAssociation
SubnetId			RouteTableId
PrivateSubnet01		PrivateRouteTable01
1.21 PrivateSubnet02RouteTableAssociation->AWS::EC2::SubnetRouteTableAssociation
SubnetId			RouteTableId
PrivateSubnet02		PrivateRouteTable02


1.22 ControlPlaneSecurityGroup 			-> AWS::EC2::SecurityGroup
attach to vpc

CloudFormation ->  Create stack - > Upload a template file - > Stack name = eks-vpc -> CREATE

```
2_cloudFormation_amazon-eks-vpc-private-subnets.yaml
```

When  stack is created, select it in the console, choose Output and Record 
SecurityGroups 		sg-073cd3302446d9b80	
SubnetIds 			subnet-0b7dc30b5ed003ffd,subnet-09c5e7164f1f2cd07,subnet-0e33b02e9ece3ec68,subnet-0e6a8c3b88815939a
VpcId 				vpc-05c959835ae719f93	

# Step 1: Create your Amazon EKS cluster
Name : eksCluster

CloudFormation ->  Create stack - > Upload a template file - > NAME = eksCluster -> CREATE
```
3_cloudFormation_ekscluster.yaml
```
custername: dev


# Step 2: Create a kubeconfig file   ????????
 kubeconfig file for your cluster with the AWS CLI update-kubeconfig command.
```
aws eks --region us-east-2 update-kubeconfig --name dev
out: Added new context arn:aws:eks:us-east-2:770239628917:cluster/dev to C:\Users\ravi_kumar27\.kube\config

kubectl get svc
```

# Step 3: Create compute
Managed nodes – Linux
## 3.1 Create Amazon EKS node role using CloudFormation

CloudFormation ->  Create stack - > Upload a template file - > NAME = eks-node-group-instance-role -> CREATE

NAME : eks-node-group-instance-role
```
4_cloudFormation_amazon-eks-nodegroup-role.yaml
```

record below once stack is created.
NodeInstanceRole  = arn:aws:iam::770239628917:role/eks-node-group-instance-role-NodeInstanceRole-MLIV0BLP98AC

## 3.2  Create  managed node group using the AWS Management Console

Select EKS cluster name->Compute tab ->  Add Node Group
Name: eksNodeGroup
Node IAM role name: From step 3.1

CloudFormation ->  Create stack - > Upload a template file - > NAME = eksNodeGroup -> CREATE
```
5_cloudFormation_eksnodegroup.yaml
```

Watch the status of your nodes and wait for them to reach the Ready status.
```
kubectl get nodes --watch
```


# Next steps
Cluster Autoscaler 
Deploy a sample Linux application 
Deploy a Windows sample application
Cluster management




# useful commands

aws eks list-clusters
aws eks --region us-east-2 update-kubeconfig --name dev
kubectl get svc
kubectl get nodes --watch


# Note: 
Available Amazon EKS Kubernetes versions
https://docs.aws.amazon.com/eks/latest/userguide/kubernetes-versions.html
Kubernetes 1.17

kubectl latest version
curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt
v1.19.2

# Reference 

HELM Chart
https://stackoverflow.com/questions/61878907/is-it-possible-to-create-a-cloudfromation-template-to-deploy-to-aws-eks

##  1. Getting started with Amazon EKS
https://docs.aws.amazon.com/eks/latest/userguide/getting-started-console.html
https://docs.aws.amazon.com/eks/latest/userguide/getting-started.html

## 2. CloudFormation Ref & GetAtt cheatsheet
https://theburningmonk.com/cloudformation-ref-and-getatt-cheatsheet/
https://github.com/awsdocs/aws-cloudformation-user-guide/blob/master/doc_source/intrinsic-function-reference-getatt.md
## 3. AWS CloudFormation resource and property types reference
https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-template-resource-type-ref.html

## 4.Creates an Amazon EKS control plane.
https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-eks-cluster.html

## 5.Using AWS CloudFormation to deploy software into Amazon EKS clusters
https://aws.amazon.com/blogs/infrastructure-and-automation/using-aws-cloudformation-to-deploy-software-into-amazon-eks-clusters/

## 6. Deploy your AWS EKS cluster with Terraform
https://www.padok.fr/en/blog/aws-eks-cluster-terraform




## Using AWS CloudFormation to deploy software into Amazon EKS clusters
https://aws.amazon.com/blogs/infrastructure-and-automation/using-aws-cloudformation-to-deploy-software-into-amazon-eks-clusters/

## Modular and Scalable Amazon EKS Architecture
https://docs.aws.amazon.com/quickstart/latest/amazon-eks-architecture/welcome.html

## Creating Kubernetes Auto Scaling Groups for Multiple Availability Zones

https://aws.amazon.com/blogs/containers/amazon-eks-cluster-multi-zone-auto-scaling-groups/

## What is Amazon EKS?
https://docs.aws.amazon.com/eks/latest/userguide/what-is-eks.html

## What is AWS CloudFormation?
https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/Welcome.html

https://youtu.be/qKljYgi2lQ0

## video eks cluster
https://youtu.be/X2ljiOx6BMQ

## Deploying and Scaling Spring Boot Microservices to Amazon EKS - AWS User Group Singapore
https://youtu.be/hZyUOvP7qv0
https://github.com/learnk8s/spring-boot-k8s-hpa

## Deploy Kubernetes Application With AWS EKS
https://www.sndkcorp.com/blog/deploy-kubernetes-application-with-aws-eks/

## Reference 

## How to deploy a production-grade Kubernetes cluster on AWS
https://gruntwork.io/guides/kubernetes/how-to-deploy-production-grade-kubernetes-cluster-aws/#configure-the-control-plane

## https://aws.amazon.com/

## Getting started with Amazon EKS
https://docs.aws.amazon.com/eks/latest/userguide/getting-started.html

## Part 2 - Hands-on: Java Applikation and CloudFormation Templates

https://www.qualysoft.com/en/blog/teil-2-hands-java-applikation-und-cloudformation-templates

## Deploying a Kubernetes Cluster With Amazon EKS
https://dzone.com/articles/deploying-a-kubernetes-cluster-with-amazon-eks

## https://docs.bitnami.com/tutorials/deploy-java-application-kubernetes-helm/

## Deploy your AWS EKS cluster with Terraform
https://www.padok.fr/en/blog/aws-eks-cluster-terraform

## deploy eks cluster using cloudformation

## CloudFormation template for creating EKS clusters
https://gitlab.com/gitlab-org/gitlab/-/merge_requests/17036

## Deploy a Kubernetes Cluster Using Amazon EKS with New Quick Start
https://aws.amazon.com/about-aws/whats-new/2019/02/deploy-a-kubernetes-cluster-using-amazon-eks-with-new-quick-start/

## Deploying a Kubernetes Cluster with Amazon EKS
https://logz.io/blog/amazon-eks-cluster/
https://dzone.com/articles/deploying-a-kubernetes-cluster-with-amazon-eks

## Quickly spin up an AWS EKS Kubernetes cluster using CloudFormation
https://hackernoon.com/quickly-spin-up-an-aws-eks-kubernetes-cluster-using-cloudformation-3d59c56b292e

## AWS EKS: Managed setup with CloudFormation
https://medium.com/@dhammond0083/aws-eks-managed-setup-with-cloudformation-97461300e952

## AWS Kubernetes EKS | Part 1 — Create using CloudFormation with Hardened Bastion
https://medium.com/@susanto.bn/aws-kubernetes-eks-part-1-create-using-cloudformation-with-hardened-bastion-4e459250ffd0


## Fn::Sub
https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference-sub.html

##  AWS::CloudFormation::Interface:   ParameterGroups
https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-cloudformation-interface.html

## key pair
https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html
