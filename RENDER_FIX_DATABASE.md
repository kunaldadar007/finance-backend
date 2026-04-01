# Render Deployment Error Fix - Database Connection

## ❌ Problem

```
Caused by: java.net.ConnectException: Connection refused
at oracle.jdbc.driver.T4CConnection.logon(T4CConnection.java:700)
...
Connection refused, socket connect lapse 0 ms. localhost 1521
```

**Root Cause**: Application deployed but **cannot connect to Oracle database**

---

## ✅ Solution

The application is looking for an Oracle database at `localhost:1521`, but Render doesn't have an Oracle database server running. 

You need to configure **environment variables** to point to your actual database.

---

## 🔧 Fix Steps in Render Dashboard

### **Step 1: Go to Environment Variables**
1. Render Dashboard → Your Service → **Environment**
2. Look for "Environment Variables" section
3. **Add or Update** these variables:

### **Step 2: Add Database Configuration**

| Key | Value | Description |
|-----|-------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:oracle:thin:@YOUR_DB_HOST:1521:ORCLPDB` | Your actual Oracle database address |
| `SPRING_DATASOURCE_USERNAME` | `C##kunal` | Oracle username |
| `SPRING_DATASOURCE_PASSWORD` | `kunal123` | Oracle password |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Auto-create/update database schema |
| `APP_JWT_SECRET` | `your-32-char-secret-key-here` | JWT encryption key (min 32 chars) |

### **Step 3: Find Your Database Host**

Replace `YOUR_DB_HOST` with **one of**:

**Option A: Local Database (if Oracle runs on your computer)**
```
SPRING_DATASOURCE_URL = jdbc:oracle:thin:@YOUR_PC_IP:1521:ORCLPDB
```
Example: `jdbc:oracle:thin:@192.168.1.100:1521:ORCLPDB`

**Option B: AWS RDS Oracle Database**
```
SPRING_DATASOURCE_URL = jdbc:oracle:thin:@your-rds-instance.c3w7zb5h9l0z.us-east-1.rds.amazonaws.com:1521:ORCLPDB
```

**Option C: Oracle Cloud Database**
```
SPRING_DATASOURCE_URL = jdbc:oracle:thin:@your-cloud-db.example.com:1521:ORCLPDB
```

**Option D: Docker Oracle (on Render)**
```
SPRING_DATASOURCE_URL = jdbc:oracle:thin:@oracle-db:1521:ORCLPDB
```

---

## 🚀 Redeploy After Fixing

**In Render Dashboard:**
1. Click your service
2. Scroll down to "Redeploy"
3. Click "Manual Deploy" or "Deploy latest commit"
4. Watch logs for success message

**Expected Log Message:**
```
INFO c.z.finance.FinanceBackendApplication : Started FinanceBackendApplication in X.XXX seconds
```

---

## 🆘 Still Getting Database Errors?

### **If using database-less deployment (testing only):**

Add this environment variable:
```
SPRING_JPA_HIBERNATE_DDL_AUTO = none
```

This allows the app to start without a database (API endpoints may fail, but health check works).

---

### **If database is behind a firewall:**

Make sure:
1. ✅ Oracle database is accepting external connections
2. ✅ Firewall allows port 1521 inbound
3. ✅ Connection string is correct (host:port:SID)
4. ✅ Username and password are correct

---

## 🧪 Test After Deployment

Once redeployed, test the health endpoint:

```bash
curl https://YOUR_SERVICE_URL.onrender.com/api/auth/health
```

Should return:
```
Auth service is healthy
```

---

## 📋 Complete Environment Variables Checklist

```bash
# Database Configuration (REQUIRED)
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@YOUR_DB_HOST:1521:ORCLPDB
SPRING_DATASOURCE_USERNAME=C##kunal
SPRING_DATASOURCE_PASSWORD=kunal123

# Database Schema (OPTIONAL)
SPRING_JPA_HIBERNATE_DDL_AUTO=update  # or: validate, create, create-drop

# Security (REQUIRED)
APP_JWT_SECRET=your-super-secret-key-minimum-32-characters-long

# JWT Token Expiration (OPTIONAL)
APP_JWT_EXPIRATION=86400000  # 24 hours in milliseconds

# Logging (OPTIONAL)
SPRING_JPA_SHOW_SQL=false  # Set to true for SQL debugging
```

---

## 🔍 Common Issues & Solutions

| Error | Cause | Solution |
|-------|-------|----------|
| `Connection refused` | Database not running or wrong host | Verify `SPRING_DATASOURCE_URL` is correct |
| `Invalid username/password` | Wrong credentials | Check `SPRING_DATASOURCE_USERNAME` & `PASSWORD` |
| `Timeout waiting for connection` | Firewall blocking port 1521 | Open port 1521 in database firewall |
| `ORA-01017: invalid username/password` | Wrong Oracle user | Use `C##kunal` not just `kunal` |
| `Network Adapter could not establish connection` | Wrong hostname | Verify hostname/IP is reachable |

---

## 📞 Need Help?

### **Check Render Logs**
1. Service page → **Logs**
2. Look for error message
3. Match against troubleshooting table above

### **Test Database Locally First**
```bash
cd c:\workspace\nareshproject\finance-backend
./mvnw spring-boot:run
```

If it works locally, the issue is with the environment variables in Render.

### **Render Support**
- Docs: https://render.com/docs
- Status: https://status.render.com

---

## ✨ Next Steps

1. ✅ Add environment variables to Render
2. ✅ Redeploy the application
3. ✅ Check logs for success
4. ✅ Test health endpoint
5. ✅ Test API endpoints (register, login, dashboard)

Once database connection works, your Finance Backend will be fully operational on Render! 🎉
