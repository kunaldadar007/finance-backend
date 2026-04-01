# AWS Elastic Beanstalk Deployment Guide

## Prerequisites
- AWS Account (free tier available)
- AWS CLI installed

## Step 1: Install AWS CLI & Elastic Beanstalk CLI

```bash
# Download AWS CLI: https://aws.amazon.com/cli/

# Install EB CLI
pip install awsebcli --upgrade --user
```

## Step 2: Configure AWS Credentials

```bash
aws configure
# Enter:
# - AWS Access Key ID
# - AWS Secret Access Key
# - Default region: us-east-1
# - Output format: json
```

## Step 3: Initialize Elastic Beanstalk

```bash
cd c:\workspace\nareshproject\finance-backend
eb init -p "Java 17 running on 64bit Windows Server 2016" finance-backend --region us-east-1
```

## Step 4: Create Environment

```bash
eb create finance-backend-env --instance-type t2.micro --envvars APP_JWT_SECRET="your-secret-key"
```

## Step 5: Configure Database Connection

```bash
eb setenv SPRING_DATASOURCE_URL="jdbc:oracle:thin:@your-rds-endpoint:1521:ORCLPDB"
eb setenv SPRING_DATASOURCE_USERNAME="C##kunal"
eb setenv SPRING_DATASOURCE_PASSWORD="kunal123"
```

## Step 6: Deploy Application

```bash
# Build first
./mvnw clean package -DskipTests

# Deploy to EB
eb deploy
```

## Step 7: Monitor Deployment

```bash
# Check status
eb status

# View logs
eb logs

# SSH into instance (optional)
eb ssh
```

## Step 8: Access Your Application

```bash
# Get EB URL
eb open

# Or manually: http://finance-backend-env.elasticbeanstalk.com/api/auth/health
```

## Step 9: Enable HTTPS (SSL/TLS)

```bash
# Go to AWS Console -> Certificate Manager
# Request free certificate for your domain
# Attach to Elastic Beanstalk environment
```

## Cleanup (when done testing)

```bash
eb terminate finance-backend-env
```

---

## Cost Estimate
- **Free Tier**: Up to 750 hours/month of t2.micro EC2
- **RDS Database**: $0 for free tier (db.t2.micro)
- **Total**: FREE for 12 months (AWS free tier)
