-- LeafGO 数据库初始化脚本
CREATE DATABASE IF NOT EXISTS leafgo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE leafgo;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  phone VARCHAR(20),
  avatar VARCHAR(255),
  user_type VARCHAR(20) NOT NULL COMMENT 'JOB_SEEKER, EMPLOYER',
  status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE, INACTIVE, BANNED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_email (email),
  INDEX idx_user_type (user_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 企业表
CREATE TABLE IF NOT EXISTS companies (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  name VARCHAR(100) NOT NULL,
  logo VARCHAR(255),
  scale VARCHAR(50),
  industry VARCHAR(100),
  description TEXT,
  location VARCHAR(255),
  website VARCHAR(255),
  verified BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_industry (industry),
  INDEX idx_verified (verified)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 职位表
CREATE TABLE IF NOT EXISTS jobs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  company_id BIGINT NOT NULL,
  title VARCHAR(100) NOT NULL,
  location VARCHAR(255),
  salary VARCHAR(50),
  job_type VARCHAR(50),
  experience VARCHAR(50),
  education VARCHAR(50),
  description TEXT,
  requirements TEXT,
  benefits TEXT,
  status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE, CLOSED, DRAFT',
  view_count INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE,
  INDEX idx_status (status),
  INDEX idx_location (location),
  FULLTEXT idx_title_description (title, description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 简历表
CREATE TABLE IF NOT EXISTS resumes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL UNIQUE,
  name VARCHAR(50) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(100),
  avatar VARCHAR(255),
  self_introduction TEXT,
  education TEXT,
  work_experience TEXT,
  skills TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 投递记录表
CREATE TABLE IF NOT EXISTS applications (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  job_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  resume_id BIGINT NOT NULL,
  cover_letter TEXT,
  status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, VIEWED, INTERVIEW, REJECTED, ACCEPTED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id),
  INDEX idx_job_id (job_id),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 会话表
CREATE TABLE IF NOT EXISTS conversations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user1_id BIGINT NOT NULL,
  user2_id BIGINT NOT NULL,
  last_message TEXT,
  last_message_time TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_users (user1_id, user2_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 消息表
CREATE TABLE IF NOT EXISTS messages (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  conversation_id BIGINT NOT NULL,
  sender_id BIGINT NOT NULL,
  receiver_id BIGINT NOT NULL,
  content TEXT NOT NULL,
  type VARCHAR(20) DEFAULT 'text',
  is_read BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE,
  FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_conversation_id (conversation_id),
  INDEX idx_receiver_unread (receiver_id, is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 轮播图表
CREATE TABLE IF NOT EXISTS banners (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(100) NOT NULL,
  description TEXT,
  image_url VARCHAR(255) NOT NULL,
  link_url VARCHAR(255),
  sort_order INT DEFAULT 0,
  active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入测试数据
INSERT INTO users (username, email, password, phone, user_type, status) VALUES
('张三', 'zhangsan@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138001', 'JOB_SEEKER', 'ACTIVE'),
('李四', 'lisi@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138002', 'JOB_SEEKER', 'ACTIVE'),
('王五', 'wangwu@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138003', 'EMPLOYER', 'ACTIVE'),
('赵六', 'zhaoliu@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138004', 'EMPLOYER', 'ACTIVE');

-- 测试企业
INSERT INTO companies (user_id, name, logo, scale, industry, description, location, website, verified) VALUES
(3, '科技创新有限公司', 'https://example.com/logo1.png', '100-499人', '互联网/IT', '一家专注于人工智能和大数据的高科技企业', '北京市海淀区', 'https://tech-innovation.com', TRUE),
(4, '智能制造集团', 'https://example.com/logo2.png', '500-999人', '制造业', '领先的智能制造解决方案提供商', '上海市浦东新区', 'https://smart-manufacturing.com', TRUE);

-- 测试职位
INSERT INTO jobs (company_id, title, location, salary, job_type, experience, education, description, requirements, benefits, status) VALUES
(1, '高级Java开发工程师', '北京市海淀区', '25k-40k', '全职', '3-5年', '本科', '负责核心系统开发和架构设计', '精通Java，熟悉Spring Boot，有分布式系统开发经验', '五险一金,年终奖,股票期权', 'ACTIVE'),
(1, '前端开发工程师', '北京市海淀区', '20k-35k', '全职', '1-3年', '本科', '负责Web前端开发和优化', '熟练掌握Vue或React，有大型项目经验优先', '五险一金,弹性工作,免费三餐', 'ACTIVE'),
(2, '产品经理', '上海市浦东新区', '20k-30k', '全职', '3-5年', '本科', '负责产品规划和需求分析', '有B端产品经验，熟悉制造业优先', '五险一金,带薪年假,节日福利', 'ACTIVE'),
(2, '数据分析师', '上海市浦东新区', '15k-25k', '全职', '1-3年', '本科', '负责数据分析和报表开发', '熟练使用SQL和Python，有制造业数据分析经验优先', '五险一金,定期体检,员工旅游', 'ACTIVE');

-- 测试轮播图数据
INSERT INTO banners (title, description, image_url, link_url, sort_order, active) VALUES
('加入我们，共创未来', '寻找志同道合的伙伴，一起打造美好明天', 'https://images.unsplash.com/photo-1521737711867-e3b97375f902?w=1200&h=400&fit=crop', '/jobs', 1, TRUE),
('校园招聘火热进行中', '2024届校园招聘全面启动，期待你的加入', 'https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=1200&h=400&fit=crop', '/campus', 2, TRUE),
('发现你的职业可能', '海量优质职位，精准匹配你的能力与期望', 'https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=1200&h=400&fit=crop', '/jobs', 3, TRUE);
