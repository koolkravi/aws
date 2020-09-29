Command Output 

# 1. 
```
$ kubectl get svc
NAME         TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)   AGE
kubernetes   ClusterIP   10.100.0.1   <none>        443/TCP   33m
```

# 2. 
```
$ kubectl get nodes --watch
NAME                                            STATUS   ROLES    AGE   VERSION
ip-192-168-145-219.us-west-2.compute.internal   Ready    <none>   14m   v1.17.11-eks-cfdc40
ip-192-168-205-137.us-west-2.compute.internal   Ready    <none>   14m   v1.17.11-eks-cfdc40

$ kubectl get nodes -o wide
NAME                                            STATUS   ROLES    AGE     VERSION               INTERNAL-IP       EXTERNAL-IP   OS-IMAGE         KERNEL-VERSION                  CONTAINER-RUNTIME
ip-192-168-145-219.us-west-2.compute.internal   Ready    <none>   6m40s   v1.17.11-eks-cfdc40   192.168.145.219   <none>        Amazon Linux 2   4.14.193-149.317.amzn2.x86_64   docker://19.3.6
ip-192-168-205-137.us-west-2.compute.internal   Ready    <none>   6m48s   v1.17.11-eks-cfdc40   192.168.205.137   <none>        Amazon Linux 2   4.14.193-149.317.amzn2.x86_64   docker://19.3.6
```

# 3. 




# Others
```
--------------------------------
$ kubectl get pods -o wide
NAME                     READY   STATUS    RESTARTS   AGE     IP                NODE                                           NOMINATED NODE   READINESS GATES
knote-7c6bf55569-jnz6l   1/1     Running   0          82s     192.168.28.244    ip-192-168-46-219.us-west-2.compute.internal   <none>           <none>
knote-7c6bf55569-kphth   1/1     Running   0          82s     192.168.195.169   ip-192-168-222-60.us-west-2.compute.internal   <none>           <none>
mongo-5544db8bc9-fjkff   1/1     Running   0          2m14s   192.168.213.241   ip-192-168-222-60.us-west-2.compute.internal   <none>           <none>
mongo-5544db8bc9-qjbfh   1/1     Running   0          2m14s   192.168.9.101     ip-192-168-46-219.us-west-2.compute.internal   <none>           <none>


$ kubectl get deploy
NAME    READY   UP-TO-DATE   AVAILABLE   AGE
knote   2/2     2            2           57s
mongo   2/2     2            2           109s


$ kubectl get svc
NAME         TYPE           CLUSTER-IP       EXTERNAL-IP                                                              PORT(S)        AGE
knote        LoadBalancer   10.100.227.150   a9b8f5e3839244aa194072cd8f6da53a-778786078.us-west-2.elb.amazonaws.com   80:30000/TCP   45s
kubernetes   ClusterIP      10.100.0.1       <none>                                                                   443/TCP        66m
mongo        ClusterIP      10.100.112.59    <none>                                                                   27017/TCP      97s


$ nslookup a9b8f5e3839244aa194072cd8f6da53a-778786078.us-west-2.elb.amazonaws.com
Non-authoritative answer:
Server:  speedport.ip
Address:  fe80::1

Name:    a9b8f5e3839244aa194072cd8f6da53a-778786078.us-west-2.elb.amazonaws.com
Addresses:  35.162.57.90
          44.238.242.92

curl a9b8f5e3839244aa194072cd8f6da53a-778786078.us-west-2.elb.amazonaws.com

run from browser
a9b8f5e3839244aa194072cd8f6da53a-778786078.us-west-2.elb.amazonaws.com




aws --version
aws-cli/2.0.52 Python/3.7.7 Windows/10 exe/AMD64
aws iam list-users
aws-iam-authenticator help
kubectl get svc
aws  eks --region us-west-2 update-kubeconfig --name eks-cluster



kubectl get nodes --watch

$ kubectl get nodes -o wide
NAME                                           STATUS   ROLES    AGE    VERSION              INTERNAL-IP      EXTERNAL-IP    OS-IMAGE         KERNEL-VERSION                  CONTAINER-RUNTIME
ip-192-168-222-60.us-west-2.compute.internal   Ready    <none>   4m8s   v1.17.9-eks-4c6976   192.168.222.60   <none>         Amazon Linux 2   4.14.193-149.317.amzn2.x86_64   docker://19.3.6
ip-192-168-46-219.us-west-2.compute.internal   Ready    <none>   4m7s   v1.17.9-eks-4c6976   192.168.46.219   18.237.87.89   Amazon Linux 2   4.14.193-149.317.amzn2.x86_64   docker://19.3.6
```