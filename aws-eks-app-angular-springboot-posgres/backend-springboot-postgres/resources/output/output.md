
#1. 
```
$ kubectl get storageclass
NAME            PROVISIONER             RECLAIMPOLICY   VOLUMEBINDINGMODE      ALLOWVOLUMEEXPANSION   AGE
gp2 (default)   kubernetes.io/aws-ebs   Delete          WaitForFirstConsumer   false                  22m

$ kubectl create -f 2-pv-claim.yaml
persistentvolumeclaim/postgres-pv-claim created

$ kubectl get pvc
NAME                STATUS    VOLUME   CAPACITY   ACCESS MODES   STORAGECLASS   AGE
postgres-pv-claim   Pending                                      gp2            18s

$ kubectl describe pvc postgres-pv-claim
Name:          postgres-pv-claim
Namespace:     default
StorageClass:  gp2
Status:        Pending
Volume:
Labels:        app=postgres
Annotations:   <none>
Finalizers:    [kubernetes.io/pvc-protection]
Capacity:
Access Modes:
VolumeMode:    Filesystem
Mounted By:    <none>
Events:
  Type    Reason                Age                   From                         Message
  ----    ------                ----                  ----                         -------
  Normal  WaitForFirstConsumer  15s (x12 over 2m52s)  persistentvolume-controller  waiting for first consumer to be created before binding


$ kubectl create -f 3-configmap.yaml
configmap/postgres-config created

$ kubectl get cm
NAME              DATA   AGE
postgres-config   3      13s

$ kubectl create -f 4-statefulset.yaml
statefulset.apps/postgres-statefulset created

$ kubectl get statefulsets
NAME                   READY   AGE
postgres-statefulset   0/1     56s

$ kubectl describe  statefulsets postgres-statefulset
Name:               postgres-statefulset
Namespace:          default
CreationTimestamp:  Fri, 02 Oct 2020 15:23:44 +0200
Selector:           app=postgres
Labels:             app=postgres
Annotations:        <none>
Replicas:           1 desired | 1 total
Update Strategy:    RollingUpdate
  Partition:        824643529064
Pods Status:        1 Running / 0 Waiting / 0 Succeeded / 0 Failed
Pod Template:
  Labels:  app=postgres
  Containers:
   postgres:
    Image:      postgres:13
    Port:       5432/TCP
    Host Port:  0/TCP
    Environment Variables from:
      postgres-config  ConfigMap  Optional: false
    Environment:       <none>
    Mounts:
      /var/lib/postgresql/data from pv-data (rw)
  Volumes:
   pv-data:
    Type:       PersistentVolumeClaim (a reference to a PersistentVolumeClaim in the same namespace)
    ClaimName:  postgres-pv-claim
    ReadOnly:   false
Volume Claims:  <none>
Events:
  Type    Reason            Age   From                    Message
  ----    ------            ----  ----                    -------
  Normal  SuccessfulCreate  82s   statefulset-controller  create Pod postgres-statefulset-0 in StatefulSet postgres-statefulset successful


$ kubectl get pods
NAME                     READY   STATUS             RESTARTS   AGE
postgres-statefulset-0   0/1     CrashLoopBackOff   6          7m31s


$ kubectl logs postgres-statefulset-0
The files belonging to this database system will be owned by user "postgres".
This user must also own the server process.

initdb: error: directory "/var/lib/postgresql/data" exists but is not empty
It contains a lost+found directory, perhaps due to it being a mount point.
Using a mount point directly as the data directory is not recommended.
Create a subdirectory under the mount point.
The database cluster will be initialized with locale "en_US.utf8".
The default database encoding has accordingly been set to "UTF8".
The default text search configuration will be set to "english".

Data page checksums are disabled.

``
FIX : 
How to mount a postgresql volume using Aws EBS in Kubernete: https://stackoverflow.com/questions/51168558/how-to-mount-a-postgresql-volume-using-aws-ebs-in-kubernete
Using subPath: https://kubernetes.io/docs/concepts/storage/volumes/#using-subpath


$ kubectl get statefulsets
NAME                   READY   AGE
postgres-statefulset   1/1     9s

$ kubectl create -f 5-service.yaml
service/postgres created

$ kubectl get svc
NAME         TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)    AGE
kubernetes   ClusterIP   10.100.0.1      <none>        443/TCP    3h23m
postgres     ClusterIP   10.100.237.57   <none>        5432/TCP   23s

```

# 2. Spring boot
```
$ kubectl create configmap hostname-config --from-literal=postgres_host=$(kubectl get svc postgres -o jsonpath="{.spec.clusterIP}")
configmap/hostname-config created

$ kubectl get cm
NAME              DATA   AGE
hostname-config   1      44s
postgres-config   3      4m5s

$ kubectl get svc spring-boot-postgres-poc
NAME                       TYPE           CLUSTER-IP      EXTERNAL-IP                                                              PORT(S)          AGE
spring-boot-postgres-poc   LoadBalancer   10.100.97.127   a91e1a89bd5f74a91ab8f3d0b7a3feac-469189436.us-west-2.elb.amazonaws.com   8000:32061/TCP   22s


```
