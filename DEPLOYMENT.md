# Finance Backend - Deployment Guide

## 🚀 Deployment Options

### **Option 1: Docker & Docker Compose (Recommended)**

#### Prerequisites
- Docker Desktop installed
- Docker Compose
- Access to Oracle database (external or container)

#### Steps

**1. Build Docker Image**
```bash
cd c:\workspace\nareshproject\finance-backend
docker build -t finance-backend:latest .
```

**2. Run with Docker Compose**
```bash
docker-compose up -d
```

**3. Verify Deployment**
```bash
docker logs finance-backend-app
# Should show: "Started FinanceBackendApplication in XX.XXX seconds"
```

**4. Test Health Endpoint**
```bash
curl http://localhost:8081/api/auth/health
```

**5. Stop Application**
```bash
docker-compose down
```

---

### **Option 2: Cloud Platforms**

#### **A. Heroku (Free/Paid)**

**Prerequisites**: Heroku CLI, GitHub account

**1. Create Heroku App**
```bash
heroku login
heroku create finance-backend-kunal
```

**2. Set Environment Variables**
```bash
heroku config:set APP_JWT_SECRET="your-secret-key"
heroku config:set SPRING_DATASOURCE_URL="jdbc:oracle:thin:@host:1521:ORCLPDB"
heroku config:set SPRING_DATASOURCE_USERNAME="C##kunal"
heroku config:set SPRING_DATASOURCE_PASSWORD="kunal123"
```

**3. Deploy from GitHub**
```bash
heroku git:remote -a finance-backend-kunal
git push heroku main
```

**4. Monitor Logs**
```bash
heroku logs --tail -a finance-backend-kunal
```

**5. Access Application**
```
https://finance-backend-kunal.herokuapp.com/api/auth/health
```

---

#### **B. AWS (Elastic Beanstalk)**

**Prerequisites**: AWS CLI, AWS account

**1. Initialize Elastic Beanstalk**
```bash
eb init -p "Java 17 running on 64bit Amazon Linux 2" finance-backend
```

**2. Create Environment**
```bash
eb create finance-backend-env
```

**3. Configure Environment Variables**
```bash
eb setenv APP_JWT_SECRET="your-secret-key"
eb setenv SPRING_DATASOURCE_URL="jdbc:oracle:thin:@your-rds:1521:ORCLPDB"
```

**4. Deploy**
```bash
eb deploy
```

**5. Monitor**
```bash
eb status
eb logs
```

**6. Access Application**
```
http://finance-backend-env.elasticbeanstalk.com/api/auth/health
```

---

#### **C. Azure App Service**

**Prerequisites**: Azure CLI, Azure account

**1. Create Resource Group**
```bash
az group create --name finance-rg --location eastus
```

**2. Create App Service Plan**
```bash
az appservice plan create --name finance-plan --resource-group finance-rg --sku B1 --is-linux
```

**3. Create Web App**
```bash
az webapp create --resource-group finance-rg --plan finance-plan --name finance-backend-app --runtime "JAVA|17"
```

**4. Build and Deploy**
```bash
./mvnw clean package
az webapp deployment source config-zip --resource-group finance-rg --name finance-backend-app --src ./target/finance-backend-*.jar
```

**5. Configure Settings**
```bash
az webapp config appsettings set --resource-group finance-rg --name finance-backend-app \
  --settings APP_JWT_SECRET="your-secret-key" \
  SPRING_DATASOURCE_URL="jdbc:oracle:thin:@host:1521:ORCLPDB"
```

**6. Access Application**
```
https://finance-backend-app.azurewebsites.net/api/auth/health
```

---

#### **D. Google Cloud Run (Serverless)**

**Prerequisites**: Google Cloud SDK, Google Cloud account

**1. Build Container Image**
```bash
gcloud builds submit --tag gcr.io/YOUR_PROJECT_ID/finance-backend
```

**2. Deploy to Cloud Run**
```bash
gcloud run deploy finance-backend \
  --image gcr.io/YOUR_PROJECT_ID/finance-backend \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars APP_JWT_SECRET="your-secret-key",SPRING_DATASOURCE_URL="jdbc:oracle:thin:@host:1521:ORCLPDB"
```

**3. Get Service URL**
```bash
gcloud run services describe finance-backend --region us-central1
```

**4. Test Endpoint**
```bash
curl https://finance-backend-xxxxx.a.run.app/api/auth/health
```

---

### **Option 3: Traditional Deployment (Linux Server)**

#### **Prerequisites**
- Linux Server (Ubuntu 20.04+)
- Java 17 installed
- Maven 3.9+
- Oracle Database access

#### **1. SSH into Server**
```bash
ssh user@your-server-ip
```

#### **2. Clone Repository**
```bash
cd /opt/apps
git clone https://github.com/kunaldadar007/finance-backend.git
cd finance-backend
```

#### **3. Build Application**
```bash
./mvnw clean package -DskipTests
```

#### **4. Create Systemd Service File**
```bash
sudo nano /etc/systemd/system/finance-backend.service
```

**Add:**
```ini
[Unit]
Description=Finance Backend Application
After=network.target

[Service]
Type=simple
User=finance
WorkingDirectory=/opt/apps/finance-backend
ExecStart=/usr/bin/java -jar target/finance-backend-*.jar
Restart=on-failure
RestartSec=10
Environment=APP_JWT_SECRET=your-secret-key
Environment=SPRING_DATASOURCE_URL=jdbc:oracle:thin:@localhost:1521:ORCLPDB
Environment=SPRING_DATASOURCE_USERNAME=C##kunal
Environment=SPRING_DATASOURCE_PASSWORD=kunal123

[Install]
WantedBy=multi-user.target
```

#### **5. Enable and Start Service**
```bash
sudo systemctl daemon-reload
sudo systemctl enable finance-backend
sudo systemctl start finance-backend
sudo systemctl status finance-backend
```

#### **6. Check Logs**
```bash
sudo journalctl -u finance-backend -f
```

#### **7. Setup Nginx Reverse Proxy**
```bash
sudo nano /etc/nginx/sites-available/finance-backend
```

**Add:**
```nginx
server {
    listen 80;
    server_name finance.yourdomain.com;

    location / {
        proxy_pass http://localhost:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

#### **8. Enable Nginx Site**
```bash
sudo ln -s /etc/nginx/sites-available/finance-backend /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

#### **9. Setup SSL Certificate (Let's Encrypt)**
```bash
sudo apt install certbot python3-certbot-nginx
sudo certbot --nginx -d finance.yourdomain.com
```

---

### **Option 4: Kubernetes (Advanced)**

#### **Prerequisites**: Kubernetes cluster, kubectl, Docker

#### **1. Create Kubernetes Docker Secret**
```bash
kubectl create secret docker-registry regcred --docker-server=gcr.io \
  --docker-username=_json_key --docker-password="$(cat ~/key.json)"
```

#### **2. Create Deployment YAML**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: finance-backend
spec:
  replicas: 2
  selector:
    matchLabels:
      app: finance-backend
  template:
    metadata:
      labels:
        app: finance-backend
    spec:
      containers:
      - name: finance-backend
        image: gcr.io/your-project/finance-backend:latest
        ports:
        - containerPort: 8081
        env:
        - name: APP_JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: finance-secret
              key: jwt-secret
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:oracle:thin:@oracle-db:1521:ORCLPDB"
        livenessProbe:
          httpGet:
            path: /api/auth/health
            port: 8081
          initialDelaySeconds: 30
          periodSeconds: 10

---
apiVersion: v1
kind: Service
metadata:
  name: finance-backend-service
spec:
  type: LoadBalancer
  ports:
  - port: 80
    targetPort: 8081
  selector:
    app: finance-backend
```

#### **3. Deploy**
```bash
kubectl apply -f deployment.yaml
kubectl get pods
kubectl get svc
```

---

## 📊 Deployment Comparison

| Option | Cost | Complexity | Scalability | Maintenance |
|--------|------|-----------|------------|-------------|
| Docker Local | $0 | ⭐ | ⭐ | ⭐ |
| Heroku | $$ | ⭐⭐ | ⭐⭐ | ⭐⭐ |
| AWS EB | $$$ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐ |
| Azure App Service | $$$ | ⭐⭐ | ⭐⭐⭐ | ⭐⭐ |
| Google Cloud Run | $ | ⭐⭐ | ⭐⭐⭐⭐ | ⭐ |
| Linux Server | $ | ⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐ |
| Kubernetes | $$$ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ |

---

## 🔐 Security Checklist Before Deployment

- [ ] Change default JWT secret to strong random string
- [ ] Use environment variables for sensitive data
- [ ] Enable HTTPS/SSL certificate
- [ ] Configure firewall rules
- [ ] Enable database connection pooling
- [ ] Set up monitoring and logging
- [ ] Enable rate limiting
- [ ] Configure CORS properly
- [ ] Use Bearer token authentication
- [ ] Implement request validation

---

## 🚨 Troubleshooting

### **Application won't start**
```bash
# Check logs
docker logs finance-backend-app

# Verify database connection
mvn clean test
```

### **Database connection fails**
```bash
# Test Oracle connection
sqlplus C##kunal/kunal123@//localhost:1521/ORCLPDB

# Update connection string in application.yml
```

### **Port already in use**
```bash
# Kill process on port 8081
lsof -i :8081
kill -9 <PID>
```

### **JWT token errors**
```bash
# Verify JWT secret is set
echo $APP_JWT_SECRET

# Regenerate token manually
POST /api/auth/login with valid credentials
```

---

## 📈 Post-Deployment

1. **Monitor Performance**
   - Set up CloudWatch/DataDog/New Relic
   - Monitor response times, error rates
   - Track database connection pool

2. **Enable Logging**
   - Configure centralized logging (ELK, Splunk)
   - Monitor application errors
   - Track user activity

3. **Setup Backups**
   - Daily database backups
   - Code repository backups
   - Configuration backups

4. **Auto-scaling**
   - Configure horizontal scaling (Kubernetes, AWS)
   - Set CPU/memory thresholds
   - Monitor load

5. **CI/CD Pipeline**
   - Setup GitHub Actions for automated deployment
   - Run tests before deployment
   - Verify all checks before release

---

## 🎯 Recommended Quick Start

**For immediate deployment:**

```bash
# 1. Build Docker image
docker build -t finance-backend:latest .

# 2. Run with Docker Compose
docker-compose up -d

# 3. Verify
curl http://localhost:8081/api/auth/health

# 4. Push to GitHub
git add Dockerfile docker-compose.yml
git commit -m "Add Docker deployment configuration"
git push origin main
```

**Then choose your cloud platform and deploy!**

---

**Need help with a specific deployment option? Let me know!**
