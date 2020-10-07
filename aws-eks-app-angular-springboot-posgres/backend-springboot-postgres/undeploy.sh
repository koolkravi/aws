#!/bin/sh

# Step 1: Delete Spring boot app service and deployment 
kubectl delete -f resources/spring-boot-app.yaml

# Step 2: Delete hostname-config 
kubectl delete cm hostname-config

# Step 3: Delete postgres service and deployment 
kubectl delete -f resources/postgres.yaml

# Step 4: Delete ECR 
aws ecr delete-repository \
    --repository-name  spring-boot-postgres-poc \
    --force