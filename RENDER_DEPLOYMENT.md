# Render.com Deployment Guide - Finance Backend

## 🎯 Why Render?

✅ **FREE** forever (no credit card required)  
✅ **Easy deployment** from GitHub  
✅ **Auto-deploy** on push to main  
✅ **100% uptime SLA** on paid plans  
✅ **Built-in monitoring** and logging  
✅ **Custom domains** included  

---

## 📋 Prerequisites

1. **Render Account**: https://render.com (Sign up with GitHub)
2. **GitHub Repository**: Your project pushed to GitHub ✅
3. **Database**: Oracle database (local, RDS, or cloud)

---

## 🚀 Step-by-Step Deployment

### **Step 1: Create Render Account**

1. Go to https://render.com
2. Click "Get Started"
3. Sign up with GitHub account
4. Authorize Render to access your GitHub

---

### **Step 2: Connect GitHub Repository**

1. In Render Dashboard, click "New +"
2. Select "Web Service"
3. Click "Connect Repository"
4. Search for: `finance-backend`
5. Click "Connect"

---

### **Step 3: Configure Web Service**

Fill in the form:

| Field | Value |
|-------|-------|
| **Name** | `finance-backend` |
| **Environment** | `Java` |
| **Region** | `Oregon (us-west)` or nearest to you |
| **Branch** | `main` |
| **Build Command** | `./mvnw clean package -DskipTests` |
| **Start Command** | `java -jar target/finance-backend-*.jar` |
| **Instance Type** | `Free` |

---

### **Step 4: Set Environment Variables**

Before deploying, add these environment variables in Render:

Click on "Advanced" → "Environment Variables"

Add these:

```
PORT = 8081
APP_JWT_SECRET = your-super-secret-key-32-chars-minimum
SPRING_DATASOURCE_URL = jdbc:oracle:thin:@YOUR_ORACLE_HOST:1521:ORCLPDB
SPRING_DATASOURCE_USERNAME = C##kunal
SPRING_DATASOURCE_PASSWORD = kunal123
SPRING_JPA_HIBERNATE_DDL_AUTO = update
SPRING_JPA_SHOW_SQL = true
```

⚠️ **Important**: Replace `YOUR_ORACLE_HOST` with:
- `localhost` if Oracle runs on Render server
- Your RDS/Cloud Oracle hostname
- Your external Oracle server IP/hostname

---

### **Step 5: Deploy Application**

1. Click "Create Web Service"
2. Render starts building automatically
3. Watch the build logs in real-time

**Expected output:**
```
Building Java application...
Building with Maven...
[INFO] BUILD SUCCESS
Deploying application...
Your service is live!
```

---

### **Step 6: Access Your Application**

Once deployed, Render provides a live URL:

```
https://finance-backend.onrender.com/api/auth/health
```

Response:
```
Auth service is healthy
```

---

## ✅ What's Working After Deployment

1. **Health Check**
   ```bash
   curl https://finance-backend.onrender.com/api/auth/health
   ```

2. **Register User**
   ```bash
   curl -X POST https://finance-backend.onrender.com/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{"email":"user@example.com","password":"Test123"}'
   ```

3. **Login**
   ```bash
   curl -X POST https://finance-backend.onrender.com/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email":"user@example.com","password":"Test123"}'
   ```

4. **Dashboard Summary**
   ```bash
   curl -H "Authorization: Bearer {token}" \
     https://finance-backend.onrender.com/api/dashboard/summary
   ```

---

## 🔄 Auto-Deploy Feature

Render automatically redeploys when you push to GitHub:

```bash
# Make changes to your code
git add .
git commit -m "Update feature"
git push origin main

# Render automatically detects push and redeploys!
# Check deployment logs in Render Dashboard
```

---

## 📊 Render Dashboard Features

### **View Logs**
- Real-time application logs
- Build logs
- Error tracking

### **Metrics**
- CPU usage
- Memory usage
- Request count
- Response times

### **Custom Domain**
1. Go to "Settings" → "Custom Domains"
2. Add your domain: `api.yourdomain.com`
3. Add DNS CNAME record pointing to Render
4. Free SSL certificate auto-generated

### **Restart Service**
- Manual restart available
- Useful for clearing cache
- No downtime with proper graceful shutdown

---

## 💰 Pricing

| Plan | Cost | Uptime | Resources |
|------|------|--------|-----------|
| **Free** | $0 | Spins down after 15min inactivity | Shared |
| **Starter** | $7/month | 99.5% | Dedicated (0.5 CPU, 512MB RAM) |
| **Standard** | $25/month | 99.95% | Dedicated (1 CPU, 2GB RAM) |

**For your needs**: Free plan is sufficient for testing.

---

## 🔗 Connect GitHub for Auto-Deploy

By default, Render watches your GitHub repository:

✅ **Push to main** → Auto redeploy  
✅ **Pull request** → Preview deployment  
✅ **Branch protection** → Prevent bad deploys  

### **Disable Auto-Deploy** (optional)
1. Service Settings
2. Toggle "Auto-Deploy" OFF
3. Deploy manually via Dashboard

---

## 🚨 Troubleshooting

### **Build Fails**
```bash
# Check build logs in Render Dashboard
# Common issues:
1. Missing environment variables
2. Wrong database connection string
3. Java version mismatch
```

### **Application Crashes**
```bash
# Check runtime logs
# Common issues:
1. Database unavailable
2. Port binding issue
3. Memory overflow
```

### **Database Connection Timeout**
```bash
# Verify connection string
SPRING_DATASOURCE_URL = jdbc:oracle:thin:@YOUR_HOST:1521:ORCLPDB

# Test connection locally first
./mvnw spring-boot:run
```

### **Service Spins Down**
```bash
Free plan spins down after 15 minutes of inactivity
Solution: Upgrade to Starter plan ($7/month) or use paid health check service
```

---

## 📈 Production Checklist

- [ ] Change JWT secret to strong random value
- [ ] Set up custom domain with SSL
- [ ] Enable monitoring and alerts
- [ ] Configure database backups
- [ ] Set up error tracking (Sentry, Rollbar)
- [ ] Enable CORS properly
- [ ] Rotate secrets regularly
- [ ] Implement rate limiting
- [ ] Add request logging
- [ ] Setup uptime monitoring

---

## 🔐 Security Best Practices

1. **Never commit secrets** to GitHub
2. **Use environment variables** for sensitive data
3. **Rotate secrets** regularly
4. **Enable 2FA** on GitHub and Render accounts
5. **Review deploy logs** regularly
6. **Monitor error logs** for attacks
7. **Use strong JWT secret** (32+ characters)
8. **Enable HTTPS** (auto-enabled on Render)

---

## 📱 Monitoring Deployed App

### **View Logs**
```bash
# In Render Dashboard:
1. Select your service
2. Click "Logs"
3. Filter by timeframe or keyword
```

### **Set Up Alerts**
```bash
# In Render Dashboard:
1. Click "Notifications"
2. Add email for deploy failures
3. Enable critical error alerts
```

### **Health Checks**
```bash
# Render automatically checks /api/auth/health every 30 seconds
# If unhealthy, service is marked down
# You can customize health check endpoint in render.yaml
```

---

## 🔄 Redeployment

### **Manual Redeploy**
1. Dashboard → Select Service
2. Click "Manual Deploy"
3. Select branch
4. Click "Deploy"

### **Redeploy via Git**
```bash
git commit --allow-empty -m "Force redeploy"
git push origin main
```

---

## 📝 Update render.yaml (Optional)

The `render.yaml` file in your repo tells Render how to deploy.

**Current config** handles:
- ✅ Java 17 runtime
- ✅ Maven build
- ✅ Health check monitoring
- ✅ Environment variables
- ✅ Port configuration

**You can modify** to add:
- Custom build/start commands
- Worker services
- Cron jobs
- Static file serving
- Databases

---

## 🎉 Your App is Live!

Once deployment completes:

1. **App URL**: https://finance-backend.onrender.com
2. **Health Check**: https://finance-backend.onrender.com/api/auth/health
3. **All Endpoints**: Ready to use!

---

## 💡 Pro Tips

1. **Free plan limitations**:
   - Spins down after 15 min inactivity
   - Slower cold starts
   - Shared resources

2. **Upgrade to Starter ($7/month)** to:
   - Keep service always running
   - Faster deployments
   - Dedicated resources

3. **Monitor resource usage** in Dashboard to avoid overages

4. **Use Render PostgreSQL** for databases (not available for Oracle - use external)

5. **Integrate with GitHub Actions** for advanced CI/CD

---

## 📞 Support

- **Render Docs**: https://render.com/docs
- **Render Status**: https://status.render.com
- **GitHub Integration**: https://render.com/docs/github

---

## ✨ What's Next?

After successful deployment:

1. ✅ Test all API endpoints
2. ✅ Monitor logs and metrics
3. ✅ Setup custom domain
4. ✅ Add team members
5. ✅ Setup CI/CD pipeline
6. ✅ Configure alerts
7. ✅ Plan for scaling

---

**Your Finance Backend is now deployed on Render! 🚀**

Live at: https://finance-backend.onrender.com
