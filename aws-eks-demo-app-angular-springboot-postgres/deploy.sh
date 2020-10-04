#!/bin/sh

# Step 1: package spring boot application 
./mvnw -DskipTests clean package

VERSION=v0.0.10
AWS_ACCOUNT_ID=XXXXXXXXXXXX

# Step 2: Create Docker Image
docker build -t $AWS_ACCOUNT_ID.dkr.ecr.us-west-2.amazonaws.com/spring-boot-postgres-poc:$VERSION .

# Step 3: Push Image to ECR repository
docker push  $AWS_ACCOUNT_ID.dkr.ecr.us-west-2.amazonaws.com/spring-boot-postgres-poc:$VERSION

# Step 4: Deploy application to EKS 
kubectl apply -f resources/spring-boot-app.yaml

# Step 5 : LoadBalancer (External URL)
POD=$(kubectl get svc -l app=spring-boot-postgres-poc | grep LoadBalancer | awk '{print $4}')
echo "${POD}"":8080"