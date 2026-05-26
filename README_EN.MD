# LeafGO

<div align="center">

![LeafGO Logo](frontend/src/assets/images/logo.png)

**Connecting Talent with Opportunities, Starting a New Career Chapter**

[![GitHub stars](https://img.shields.io/github/stars/YangShengzhou03/LeafGo?style=social)](https://github.com/YangShengzhou03/LeafGo)
[![GitHub forks](https://img.shields.io/github/forks/YangShengzhou03/LeafGo?style=social)](https://github.com/YangShengzhou03/LeafGo)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Vue](https://img.shields.io/badge/Vue-3.4.0-brightgreen.svg)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)

[Live Demo](#) | [Documentation](#) | [GitHub](https://github.com/YangShengzhou03/LeafGo)

</div>

---

## Introduction

**LeafGO** is a modern intelligent recruitment platform dedicated to providing efficient and convenient recruitment and job-seeking services for job seekers and enterprises. The platform adopts a front-end and back-end separation architecture and provides a complete recruitment and job-seeking solution.

### Core Positioning

- **Job Seekers**: Intelligent job recommendations, online resume management, real-time communication
- **Recruiters**: Efficient talent screening, job posting management, employer branding
- **Platform Management**: Data statistics analysis, content review management, user behavior analysis

### Project Features

- **Modern UI Design** - Providing ultimate user experience
- **High-Performance Architecture** - Front-end and back-end separation, supporting high concurrency
- **Multi-Platform Support** - Web platform support
- **Intelligent Matching** - Job search and filtering
- **Real-time Communication** - Online chat, message push
- **Secure and Reliable** - JWT authentication, data encryption, permission control

---

## Features

### Job Seeker Features

#### Job Search & Discovery
- **Intelligent Search**: Keyword search, map search, voice search
- **Multi-dimensional Filtering**: Job type, salary range, work experience, education requirements, company industry
- **Hot Recommendations**: Personalized job recommendations based on user profiles
- **Job Favorites**: Save interesting jobs for later viewing

#### Resume Management
- **Online Resume**: Visual resume editor
- **Multiple Resumes**: Support creating multiple resumes for different positions
- **Resume Templates**: Various professional resume templates
- **Resume Export**: Support PDF, Word format export
- **Privacy Settings**: Resume public/hidden settings

#### Application Management
- **One-Click Application**: Quick resume submission
- **Application Records**: Real-time tracking of application status
- **Status Notifications**: View, interview, hiring and other status notifications
- **Application Analysis**: Application data statistics and analysis

#### Instant Messaging
- **Online Chat**: Real-time communication with HR
- **Message Notifications**: Real-time push of new messages
- **Chat History**: View historical messages
- **File Transfer**: Support sending resumes, images and other files

#### Company Browsing
- **Company Details**: Company introduction, scale, financing status
- **Active Jobs**: View all active jobs of the company
- **Company Verification**: Identify verified companies to ensure job security
- **Company Reviews**: View employee reviews and interview experiences

### Recruiter Features

#### Job Management
- **Post Jobs**: Quick job posting
- **Edit Jobs**: Modify job details anytime
- **Online/Offline Management**: Flexible control of job display status
- **Batch Operations**: Batch posting, editing, offline jobs
- **Job Templates**: Quick posting with common job templates

#### Candidate Management
- **Resume Reception**: Real-time reception of job seeker resumes
- **Resume Screening**: Intelligent screening of matching candidates
- **Status Management**: Pending, viewed, interview, hired, rejected
- **Talent Pool**: Build corporate talent pool
- **Batch Processing**: Batch viewing, batch operations

#### Company Display
- **Company Homepage**: Complete company information display
- **Brand Promotion**: Upload company logo, promotional images
- **Company Verification**: Complete company verification to enhance credibility
- **Data Statistics**: Job views, application data analysis

#### Data Analysis
- **Recruitment Data**: Job posting, resume reception statistics
- **Effectiveness Analysis**: Recruitment channel effectiveness analysis
- **Trend Analysis**: Recruitment trend visualization
- **Report Export**: Data report export function

### Admin Panel Features

#### User Management
- **User List**: View all registered users
- **User Review**: Review user qualifications
- **Status Management**: Enable/disable user accounts
- **User Statistics**: User growth trend analysis

#### Company Management
- **Company List**: View all registered companies
- **Company Review**: Review company verification applications
- **Company Classification**: Classification management by industry, scale
- **Violation Handling**: Handle company violations

#### Job Management
- **Job Review**: Review posted job information
- **Violation Handling**: Handle violating jobs
- **Job Statistics**: Job posting data analysis
- **Featured Jobs**: Recommend quality jobs

#### Data Statistics
- **Platform Data**: User, company, job, application statistics
- **Trend Analysis**: Platform development trend analysis
- **Data Visualization**: Chart display of key indicators
- **Report Generation**: Regular generation of statistical reports

---

## Technology Stack

### Frontend Stack

- **Framework**: Vue 3.4 + TypeScript 5.3
- **UI Components**: Element Plus 2.5
- **State Management**: Pinia 2.1
- **Router**: Vue Router 4.2
- **HTTP Client**: Axios 1.6
- **Build Tool**: Vite 5.0
- **Styling**: SCSS
- **Utilities**: Day.js, NProgress

### Backend Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.2 + Spring Security + JWT
- **ORM**: Spring Data JPA
- **Database**: MariaDB 10.11 (MySQL Compatible)
- **API Documentation**: Swagger/OpenAPI 3.0

### DevOps

- **Containerization**: Docker + Docker Compose

---

## Project Structure

```
leaf-go/
├── backend/                    # Backend Service
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/leafgo/
│   │   │   │   ├── config/         # Configuration
│   │   │   │   ├── controller/     # Controllers
│   │   │   │   ├── service/        # Business Logic
│   │   │   │   ├── repository/     # Data Access
│   │   │   │   ├── entity/         # Entities
│   │   │   │   ├── dto/            # Data Transfer Objects
│   │   │   │   ├── security/       # Security Configuration
│   │   │   │   ├── exception/      # Exception Handling
│   │   │   │   └── util/           # Utilities
│   │   │   └── resources/          # Resources
│   │   └── test/                   # Tests
│   └── pom.xml                     # Maven Configuration
│
├── frontend/                   # Frontend Project (Vue 3)
│   ├── src/
│   │   ├── api/                    # API Interfaces
│   │   ├── assets/                 # Static Assets
│   │   ├── components/             # Components
│   │   ├── composables/            # Composables
│   │   ├── layouts/                # Layouts
│   │   ├── router/                 # Router Configuration
│   │   ├── store/                  # State Management
│   │   ├── types/                  # TypeScript Types
│   │   ├── utils/                  # Utilities
│   │   └── views/                  # Views
│   │       ├── seeker/             # Job Seeker
│   │       ├── employer/           # Recruiter
│   │       └── admin/              # Admin Panel
│   ├── public/                     # Public Assets
│   ├── package.json                # Dependencies
│   └── vite.config.ts              # Vite Configuration
│
├── data.sql                    # Database Initialization
├── docker-compose.yml          # Docker Compose Configuration
├── LICENSE                     # License
└── README.md                   # Project README
```

---

## Quick Start

### Requirements

- **Node.js** >= 18.0
- **Java** >= 17
- **Maven** >= 3.8
- **MariaDB** >= 10.11 (or MySQL >= 8.0)
- **Docker** >= 20.0 (optional)

### Installation

#### 1. Clone Project

```bash
git clone https://github.com/yourusername/leaf-go.git
cd leaf-go
```

#### 2. Database Initialization

```bash
# Create database
mysql -u root -p < data.sql

# Or use Docker
docker-compose up -d mariadb
```

#### 3. Start Backend Service

```bash
cd backend

# Configure database connection
# Edit src/main/resources/application.yml

# Build
mvn clean package

# Run
java -jar target/leafgo-backend-1.0.0.jar
```

#### 4. Start Frontend Service

```bash
cd frontend

# Install dependencies
npm install

# Development mode
npm run dev

# Production build
npm run build
```

#### 5. Using Docker Compose (Recommended)

```bash
# Start all services
docker-compose up -d

# Check service status
docker-compose ps

# Stop services
docker-compose down
```

### Access URLs

- **Frontend Web**: http://localhost:8080
- **Backend API**: http://localhost:8081
- **API Documentation**: http://localhost:8081/swagger-ui.html
- **Admin Panel**: http://localhost:8080/admin

### Default Accounts

#### Job Seeker Account
- Phone: `13397062330`
- Verification Code: `123456`

#### Recruiter Account
- Phone: `13397062331`
- Verification Code: `123456`

#### Admin Account
- Phone: `13397062332`
- Verification Code: `123456`

---

## Modules

### User Authentication Module

- User Registration (Job Seeker/Recruiter)
- User Login (Email/Phone)
- Password Recovery
- Third-party Login (WeChat/QQ)
- JWT Token Authentication
- Permission Control

### Job Module

- Job Posting
- Job Editing
- Job Search
- Job Recommendation
- Job Favorites
- Job Statistics

### Resume Module

- Resume Creation
- Resume Editing
- Resume Preview
- Resume Export
- Multiple Resume Management

### Application Module

- Resume Submission
- Application Records
- Status Tracking
- Application Statistics

### Message Module

- Instant Messaging
- Message Push
- Chat History
- File Transfer

### Company Module

- Company Registration
- Company Verification
- Company Display
- Company Management

---

## API Documentation

### Main Endpoints

#### Authentication Endpoints
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `POST /auth/logout` - User logout

#### Job Endpoints
- `GET /jobs` - Get job list
- `GET /jobs/{id}` - Get job details
- `POST /jobs` - Post job (Recruiter)
- `PUT /jobs/{id}` - Update job
- `DELETE /jobs/{id}` - Delete job
- `GET /jobs/search` - Search jobs
- `GET /jobs/my` - Get my jobs

#### User Endpoints
- `GET /users/{id}` - Get user info
- `PUT /users/{id}` - Update user info

#### Resume Endpoints
- `GET /resumes/{userId}` - Get resume
- `PUT /resumes/{userId}` - Update resume

#### Application Endpoints
- `POST /applications` - Submit application
- `GET /applications` - Get application records
- `PUT /applications/{id}/status` - Update application status

#### Message Endpoints
- `GET /messages/conversations` - Get conversations
- `POST /messages` - Send message

#### Company Endpoints
- `GET /companies` - Get company list
- `GET /companies/{id}` - Get company detail
- `GET /companies/{id}/jobs` - Get company jobs

#### Banner Endpoints
- `GET /banners` - Get banner list

---

## Database Design

### Core Tables

- `users` - User table
- `companies` - Company table
- `jobs` - Job table
- `resumes` - Resume table
- `applications` - Application table
- `messages` - Message table
- `conversations` - Conversation table
- `banners` - Banner table

---

## Deployment

### Docker Deployment

```bash
# Build images
docker-compose build

# Start services
docker-compose up -d

# View logs
docker-compose logs -f
```

### Production Deployment

#### Backend Deployment

```bash
# Package
mvn clean package -Dmaven.test.skip=true

# Run
nohup java -jar target/leafgo-backend-1.0.0.jar \
  --spring.profiles.active=prod \
  > app.log 2>&1 &
```

#### Frontend Deployment

```bash
# Build
npm run build

# Deploy with Nginx
cp -r dist/* /usr/share/nginx/html/
```

#### Nginx Configuration

```nginx
server {
    listen 80;
    server_name yourdomain.com;

    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

## Development Guide

### Code Standards

- **Java**: Follow Alibaba Java Coding Guidelines
- **Vue**: Follow Vue Official Style Guide
- **TypeScript**: Use ESLint + Prettier
- **Git Commits**: Follow Conventional Commits

### Branch Management

- `master` - Production branch
- `develop` - Development branch
- `feature/*` - Feature development branch
- `hotfix/*` - Hotfix branch

### Development Workflow

1. Create `feature` branch from `develop`
2. Complete feature development and testing
3. Submit Pull Request
4. Merge after code review
5. Regular release to production

---

## Contributing

We welcome all forms of contributions, including but not limited to:

- Bug reports
- Feature suggestions
- Code improvements
- Documentation improvements

### Submitting Issues

Before submitting an issue, please ensure:

1. Search existing issues to avoid duplicates
2. Use a clear title to describe the problem
3. Provide detailed reproduction steps
4. Attach relevant logs or screenshots

### Submitting Pull Requests

1. Fork this repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Submit Pull Request

---

## License

This project is licensed under the [MIT License](LICENSE)

---

## Contact

- **Project Homepage**: https://github.com/yourusername/leaf-go
- **Issue Tracker**: https://github.com/yourusername/leaf-go/issues
- **Email**: support@leafgo.com

---

## Acknowledgments

Thanks to the following open source projects for their support:

- [Vue.js](https://vuejs.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [React Native](https://reactnative.dev/)
- [Element Plus](https://element-plus.org/)
- [MariaDB](https://mariadb.org/)

---

<div align="center">

**If this project helps you, please give it a Star**

Made by LeafGO Team

</div>
