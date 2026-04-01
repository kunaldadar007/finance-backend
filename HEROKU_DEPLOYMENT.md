# Heroku Deployment Guide (EASIEST)

## Prerequisites
- Heroku Account (free): https://signup.heroku.com/
- GitHub account (already have ✅)
- Heroku CLI: https://devcenter.heroku.com/articles/heroku-cli

## Step 1: Create Procfile (for Heroku)

Heroku needs to know how to run your app. Create this file:

```bash
# File: Procfile (in project root)
web: java -jar target/finance-backend-*.jar
```

## Step 2: Configure Port

Update your application.yml to use environment variables:

```yaml
server:
  port: ${PORT:8081}  # Use PORT from Heroku, default to 8081 locally
```

## Step 3: Install Heroku CLI

Download: https://devcenter.heroku.com/articles/heroku-cli

**For Windows:**
- Download installer
- Run setup
- Verify: `heroku --version`

## Step 4: Login to Heroku

```bash
heroku login
# Opens browser for authentication
```

## Step 5: Create Heroku App

```bash
cd c:\workspace\nareshproject\finance-backend
heroku create finance-backend-kunal
```

**Output:**
```
Creating app... done, ⬢ finance-backend-kunal
https://finance-backend-kunal.herokuapp.com/ | https://git.heroku.com/finance-backend-kunal.git
```

## Step 6: Set Environment Variables

```bash
# JWT Configuration
heroku config:set APP_JWT_SECRET="your-super-secret-key-must-be-32-chars-minimum"

# Database Configuration
heroku config:set SPRING_DATASOURCE_URL="jdbc:oracle:thin:@your-oracle-server:1521:ORCLPDB"
heroku config:set SPRING_DATASOURCE_USERNAME="C##kunal"
heroku config:set SPRING_DATASOURCE_PASSWORD="kunal123"
heroku config:set SPRING_JPA_HIBERNATE_DDL_AUTO="update"
```

## Step 7: Add Heroku Remote to Git

```bash
heroku git:remote -a finance-backend-kunal
```

## Step 8: Deploy Application

**Option A: Deploy from GitHub (Easiest)**
```bash
# Go to Heroku Dashboard
# App → Deploy → GitHub
# Connect GitHub account
# Search: finance-backend
# Click Connect
# Enable automatic deploy from main branch
```

**Option B: Deploy via Git CLI**
```bash
git push heroku main
```

**Output:**
```
Counting objects: 100% (50/50)
Compressing objects: 100% (45/45)
Writing objects: 100% (50/50)
Delta compression using 12 threads
remote: Compressing source files... done.
remote: Building source:
remote: -----> Building on the Heroku-20 stack
remote: -----> Java app detected
remote: -----> Installing OpenJDK 17...
remote: -----> Building with Maven 3.9.0
remote: -----> Executing: mvn clean compile assembly:single
remote: -----> Build succeeded!
remote: -----> Launching... done, v5
remote:        https://finance-backend-kunal.herokuapp.com/ deployed to Heroku
```

## Step 9: Monitor Logs

**Real-time logs:**
```bash
heroku logs --tail -a finance-backend-kunal
```

**View past logs:**
```bash
heroku logs -n 100 -a finance-backend-kunal
```

## Step 10: Test Your Deployed App

```bash
# Health check
curl https://finance-backend-kunal.herokuapp.com/api/auth/health

# Register user
curl -X POST https://finance-backend-kunal.herokuapp.com/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Test123"}'

# Login
curl -X POST https://finance-backend-kunal.herokuapp.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Test123"}'
```

## Step 11: Custom Domain (Optional)

```bash
# Add custom domain
heroku domains:add api.finance.yourdomain.com -a finance-backend-kunal

# Point your domain DNS to Heroku
# DNS CNAME: api.finance.yourdomain.com -> api.finance.yourdomain.com.herokudns.com
```

## Step 12: Enable SSL/TLS (Free)

```bash
# Heroku provides free SSL for all apps
# Auto-enabled for *.herokuapp.com
# Custom domains get free SSL too (go to Heroku dashboard)
```

## Useful Heroku Commands

```bash
# View app info
heroku info -a finance-backend-kunal

# View all config vars
heroku config -a finance-backend-kunal

# Add new config var
heroku config:set VARIABLE_NAME="value" -a finance-backend-kunal

# Remove config var
heroku config:unset VARIABLE_NAME -a finance-backend-kunal

# View processes
heroku ps -a finance-backend-kunal

# Restart app
heroku restart -a finance-backend-kunal

# Scale dynos
heroku ps:scale web=2 -a finance-backend-kunal

# View metrics
heroku metrics -a finance-backend-kunal

# Delete app
heroku apps:destroy -a finance-backend-kunal
```

## Troubleshooting

### **App crashes after deployment**
```bash
# Check logs
heroku logs --tail

# Common causes:
# 1. Missing environment variables
# 2. Database connection failed
# 3. Port binding issue
```

### **Database connection timeout**
```bash
# Verify connection string
heroku config -a finance-backend-kunal

# Test connection locally first
./mvnw spring-boot:run
```

### **Build fails**
```bash
# Clear build cache and rebuild
heroku repo:purge_cache -a finance-backend-kunal
git commit --allow-empty -m "Rebuild"
git push heroku main
```

### **Too many dynos**
```bash
# Free tier: 550 dyno-hours/month
# Enough for 1 app running 24/7
heroku ps -a finance-backend-kunal  # Check current usage
```

## Cost Breakdown

| Item | Free Plan | Hobby Plan | Pro Plan |
|------|-----------|-----------|----------|
| **Monthly Cost** | $0 | $7/month | $25+/month |
| **Dyno Hours** | 550/month | Unlimited | Unlimited |
| **Database** | None | $9+/month | Included |
| **Performance** | Sleep after 30min | Always on | Premium |
| **Uptime SLA** | None | 99.5% | 99.95% |

**Recommendation for you:**
- **Free Plan**: Testing/Development
- **Hobby Plan**: Production (costs $7/month for app + $9/month for database)

## Final Step: Push to GitHub

```bash
# Add deployment files
git add Procfile AWS_DEPLOYMENT.md HEROKU_DEPLOYMENT.md
git commit -m "Add deployment guides and Procfile"
git push origin main
```

---

**Done!** Your app is now live at: https://finance-backend-kunal.herokuapp.com/

🎉 Access it: `https://finance-backend-kunal.herokuapp.com/api/auth/health`
