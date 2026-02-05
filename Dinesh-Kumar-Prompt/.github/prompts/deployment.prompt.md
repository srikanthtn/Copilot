---
agent: agent
# EKS Deployment Guide

## Quick Start

Generate a production-ready EKS deployment with:
- Multi-AZ infrastructure (VPC, EKS, ECR, RDS)
- Kubernetes manifests (Deployment, Service, Ingress, HPA, PDB)
- CI/CD pipelines (GitHub Actions)
- Monitoring & logging (Prometheus, Fluentd)
- Security policies (Network policies, RBAC, PSP)

---

## Project Structure

```
project/
├── infrastructure/terraform/     # IaC for VPC, EKS, ECR, RDS
├── kubernetes/                   # K8s manifests (base + overlays)
├── scripts/                      # Deployment automation
├── helm/                         # Helm charts
└── .github/workflows/            # CI/CD pipelines
```

---

## Core Components

### 1. Infrastructure (Terraform)

**VPC Module** - Multi-AZ with public/private subnets, NAT gateways, VPC endpoints

**EKS Module** - Managed cluster with:
- K8s 1.28+, IRSA enabled, encryption at rest
- Managed node groups with auto-scaling
- Cluster logging (API, audit, authenticator, controller, scheduler)

**ECR Module** - Container registry with:
- Image scanning on push
- Lifecycle policies (keep last 10 tagged, delete untagged after 7 days)
- KMS encryption

**RDS Module** (optional) - Multi-AZ database with backups, encryption, parameter groups

### 2. Kubernetes Manifests

**Deployment** - Rolling updates, health probes, resource limits, pod anti-affinity

**Service** - LoadBalancer (NLB) or ClusterIP

**Ingress** - ALB with SSL, health checks, cross-zone load balancing

**HPA** - Auto-scale 3-10 pods based on CPU (70%) and memory (80%)

**PDB** - Maintain minimum 2 available pods during disruptions

**ConfigMap/Secret** - Environment configuration and sensitive data

**ServiceAccount** - IRSA for AWS service access

### 3. CI/CD Pipeline

**Build Job:**
1. Checkout code
2. Configure AWS credentials
3. Login to ECR
4. Build & tag Docker image (`${ENV}-${SHA}-${TIMESTAMP}`)
5. Push to ECR
6. Scan for vulnerabilities

**Deploy Job:**
1. Update kubeconfig
2. Apply namespace, ConfigMap, Secret
3. Deploy with Kustomize overlays (dev/staging/production)
4. Update deployment image
5. Wait for rollout completion
6. Verify deployment & run health checks

### 4. Deployment Scripts

**Bash/PowerShell scripts** for:
- Prerequisites check (AWS CLI, kubectl, Docker)
- Build & push Docker images
- Deploy to EKS
- Verify deployment health

### 5. Monitoring & Logging

**Prometheus** - Scrape pod metrics via annotations

**Grafana** - Visualization dashboards

**Fluentd** - Aggregate logs to CloudWatch

**AlertManager** - Alert routing and notifications

### 6. Security

**Network Policies** - Default deny-all, explicit allow rules

**Pod Security Policies** - Non-root users, no privilege escalation, restricted capabilities

**RBAC** - Role-based access control for service accounts

**Secrets Management** - AWS Secrets Manager or Parameter Store integration

---

## Essential Manifests

### Deployment Template

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: app
  namespace: app-ns
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  template:
    spec:
      serviceAccountName: app-sa
      securityContext:
        runAsNonRoot: true
        runAsUser: 1000
      containers:
      - name: app
        image: ${ECR_REGISTRY}/app:${TAG}
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        envFrom:
        - configMapRef:
            name: app-config
        - secretRef:
            name: app-secrets
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 5
```

### Service Template

```yaml
apiVersion: v1
kind: Service
metadata:
  name: app-svc
  annotations:
    service.beta.kubernetes.io/aws-load-balancer-type: "nlb"
spec:
  type: LoadBalancer
  selector:
    app: app
  ports:
  - port: 80
    targetPort: 8080
```

### Ingress Template (ALB)

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: app-ingress
  annotations:
    kubernetes.io/ingress.class: alb
    alb.ingress.kubernetes.io/scheme: internet-facing
    alb.ingress.kubernetes.io/target-type: ip
    alb.ingress.kubernetes.io/healthcheck-path: /actuator/health
    alb.ingress.kubernetes.io/listen-ports: '[{"HTTP": 80}, {"HTTPS": 443}]'
    alb.ingress.kubernetes.io/ssl-redirect: '443'
    alb.ingress.kubernetes.io/certificate-arn: ${ACM_CERT_ARN}
spec:
  rules:
  - host: api.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: app-svc
            port:
              number: 80
```

### HPA Template

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: app-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: app
  minReplicas: 3
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
```

### PDB Template

```yaml
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: app-pdb
spec:
  minAvailable: 2
  selector:
    matchLabels:
      app: app
```

---

## Deployment Workflow

### 1. Infrastructure Setup

```bash
cd infrastructure/terraform/environments/production
terraform init
terraform plan
terraform apply
```

### 2. Configure kubectl

```bash
aws eks update-kubeconfig --region us-east-1 --name my-cluster
```

### 3. Deploy Application

```bash
# Build & push image
docker build -t ${ECR_REGISTRY}/app:${TAG} .
docker push ${ECR_REGISTRY}/app:${TAG}

# Deploy to K8s
kubectl apply -f kubernetes/base/namespace.yaml
kubectl apply -k kubernetes/overlays/production
kubectl set image deployment/app app=${ECR_REGISTRY}/app:${TAG} -n app-ns
kubectl rollout status deployment/app -n app-ns
```

### 4. Verify Deployment

```bash
kubectl get pods -n app-ns
kubectl get svc -n app-ns
kubectl get ingress -n app-ns
curl -f http://${ENDPOINT}/actuator/health
```

---

## Security Best Practices

1. **IAM** - Use IRSA for pod-level AWS permissions
2. **Secrets** - Store in AWS Secrets Manager, mount as volumes
3. **Network** - Implement network policies (default deny-all)
4. **Images** - Scan with ECR, use minimal base images
5. **RBAC** - Least privilege for service accounts
6. **Encryption** - Enable at rest (EBS, RDS, Secrets) and in transit (TLS)

---

## Cost Optimization

1. Use Spot instances for non-critical workloads
2. Enable cluster autoscaler
3. Right-size resource requests/limits
4. Use Fargate for burst workloads
5. Implement ECR lifecycle policies
6. Enable VPC endpoints (avoid NAT charges)
7. Use Reserved Instances for predictable workloads

---

## Validation Checklist

- [ ] Infrastructure provisioned (VPC, EKS, ECR)
- [ ] kubectl access to cluster
- [ ] Images pushed to ECR
- [ ] Pods running and healthy
- [ ] Services exposing correctly
- [ ] Ingress/ALB configured with SSL
- [ ] HPA scaling pods
- [ ] Monitoring operational
- [ ] Secrets not hardcoded
- [ ] Network policies enforced
- [ ] CI/CD pipeline functional
- [ ] Health checks passing
- [ ] Documentation complete

---

## Documentation Requirements

**DEPLOYMENT.md** - Prerequisites, steps, rollback, troubleshooting

**INFRASTRUCTURE.md** - Architecture, components, cost, DR plan

**MONITORING.md** - Metrics, alerts, dashboards, logs

**RUNBOOK.md** - Operations, incidents, maintenance, contacts

---

## Quick Reference

### Common Commands

```bash
# Update kubeconfig
aws eks update-kubeconfig --region ${REGION} --name ${CLUSTER}

# Deploy with Kustomize
kubectl apply -k kubernetes/overlays/${ENV}

# Update image
kubectl set image deployment/app app=${IMAGE} -n ${NS}

# Check rollout
kubectl rollout status deployment/app -n ${NS}

# Rollback
kubectl rollout undo deployment/app -n ${NS}

# Scale manually
kubectl scale deployment/app --replicas=5 -n ${NS}

# View logs
kubectl logs -f deployment/app -n ${NS}

# Port forward
kubectl port-forward svc/app-svc 8080:80 -n ${NS}
```

### Environment Variables

```bash
AWS_REGION=us-east-1
EKS_CLUSTER_NAME=my-cluster
ECR_REPOSITORY=my-app
NAMESPACE=app-ns
ENVIRONMENT=production
```

---

## Conclusion

This guide provides a complete EKS deployment structure. Adapt to your requirements and ensure all validation steps pass before production deployment.


---
Define the task to achieve, including specific requirements, constraints, and success criteria.