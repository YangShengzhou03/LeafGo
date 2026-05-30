<template>
  <div class="resume-page">
    <SeekerNavBar />
    <main class="main-content">
      <div class="container">
        <div class="resume-layout">
          <div class="resume-sidebar">
            <div class="resume-menu">
              <div class="menu-title">简历目录</div>
              <div
                v-for="item in menuItems"
                :key="item.id"
                :class="['menu-item', { active: activeSection === item.id }]"
                @click="scrollToSection(item.id)"
              >
                <span>{{ item.label }}</span>
              </div>
            </div>
          </div>

          <div class="resume-content">
            <div class="content-header">
              <div class="header-top">
                <div class="avatar-section">
                  <div class="avatar">
                    {{ (resume.name || userStore.userInfo?.username || '未').charAt(0) }}
                  </div>
                </div>
                <div class="basic-info">
                  <h1>{{ resume.name || userStore.userInfo?.username || '未填写姓名' }}</h1>
                  <div class="info-row">
                    <span class="info-item">
                      <span class="label">求职状态</span>
                      <span class="value">{{ resume.jobStatus || '未填写' }}</span>
                    </span>
                  </div>
                </div>
                <el-button type="primary" size="small" class="edit-btn" @click="editBasicInfo">
                  编辑基本信息
                </el-button>
              </div>
              <div class="contact-row">
                <div class="contact-item">
                  <span class="contact-label">手机号</span>
                  <span class="contact-value">{{
                    resume.phone || userStore.userInfo?.phone || '未填写'
                  }}</span>
                </div>
                <div class="contact-item">
                  <span class="contact-label">邮箱</span>
                  <span class="contact-value">{{
                    resume.email || userStore.userInfo?.email || '未填写'
                  }}</span>
                </div>
              </div>
            </div>

            <div id="advantage" class="resume-section">
              <div class="section-header">
                <h2>个人优势</h2>
                <el-button
                  v-if="editingSection !== 'advantage'"
                  type="primary"
                  size="small"
                  plain
                  @click="startEdit('advantage')"
                >
                  编辑
                </el-button>
                <div v-else class="edit-actions">
                  <el-button size="small" @click="cancelEdit">取消</el-button>
                  <el-button type="primary" size="small" @click="saveAdvantage">保存</el-button>
                </div>
              </div>
              <div class="section-content">
                <template v-if="editingSection === 'advantage'">
                  <el-input
                    v-model="editingContent"
                    type="textarea"
                    :rows="6"
                    placeholder="请输入个人优势，突出你的核心竞争力..."
                  />
                </template>
                <template v-else>
                  <p v-if="resume.advantage" class="content-text">{{ resume.advantage }}</p>
                  <div v-else class="empty-placeholder">
                    <span class="empty-icon">+</span>
                    <span class="empty-text">添加个人优势，展示你的核心竞争力</span>
                  </div>
                </template>
              </div>
            </div>

            <div id="intention" class="resume-section">
              <div class="section-header">
                <h2>期望职位</h2>
                <el-button type="primary" size="small" plain @click="startEdit('intention')">
                  编辑
                </el-button>
              </div>
              <div class="section-content">
                <div v-if="resume.jobIntention" class="intention-grid">
                  <div class="intention-item">
                    <span class="item-label">期望职位</span>
                    <span class="item-value">{{ resume.jobIntention }}</span>
                  </div>
                  <div class="intention-item">
                    <span class="item-label">期望薪资</span>
                    <span class="item-value salary">{{ resume.expectedSalary || '面议' }}</span>
                  </div>
                  <div class="intention-item">
                    <span class="item-label">期望城市</span>
                    <span class="item-value">{{ resume.expectedLocation || '未填写' }}</span>
                  </div>
                </div>
                <div v-else class="empty-placeholder">
                  <span class="empty-icon">+</span>
                  <span class="empty-text">添加期望职位，让HR更快找到你</span>
                </div>
              </div>
            </div>

            <div id="work" class="resume-section">
              <div class="section-header">
                <h2>工作/实习经历</h2>
                <el-button type="primary" size="small" plain @click="startEdit('work')">
                  添加经历
                </el-button>
              </div>
              <div class="section-content">
                <div
                  v-if="resume.workExperience && resume.workExperience.length > 0"
                  class="experience-list"
                >
                  <div
                    v-for="(work, index) in resume.workExperience"
                    :key="index"
                    class="experience-item"
                  >
                    <div class="experience-header">
                      <h3>{{ work.company }}</h3>
                      <span class="experience-time"
                        >{{ work.startDate }} - {{ work.endDate || '至今' }}</span
                      >
                    </div>
                    <p class="experience-position">{{ work.position }}</p>
                    <p class="experience-desc">{{ work.description }}</p>
                  </div>
                </div>
                <div v-else class="empty-placeholder">
                  <span class="empty-icon">+</span>
                  <span class="empty-text">添加工作经历，展示你的职业发展</span>
                </div>
              </div>
            </div>

            <div id="project" class="resume-section">
              <div class="section-header">
                <h2>项目经历</h2>
                <el-button type="primary" size="small" plain @click="startEdit('project')">
                  添加项目
                </el-button>
              </div>
              <div class="section-content">
                <div
                  v-if="resume.projectExperience && resume.projectExperience.length > 0"
                  class="experience-list"
                >
                  <div
                    v-for="(project, index) in resume.projectExperience"
                    :key="index"
                    class="experience-item"
                  >
                    <div class="experience-header">
                      <h3>{{ project.name }}</h3>
                      <span class="experience-time">{{ project.time }}</span>
                    </div>
                    <p class="experience-position">{{ project.role }}</p>
                    <p class="experience-desc">{{ project.description }}</p>
                  </div>
                </div>
                <div v-else class="empty-placeholder">
                  <span class="empty-icon">+</span>
                  <span class="empty-text">添加项目经历，展示你的实战能力</span>
                </div>
              </div>
            </div>

            <div id="education" class="resume-section">
              <div class="section-header">
                <h2>教育经历</h2>
                <el-button type="primary" size="small" plain @click="startEdit('education')">
                  添加经历
                </el-button>
              </div>
              <div class="section-content">
                <div v-if="resume.education && resume.education.length > 0" class="experience-list">
                  <div
                    v-for="(edu, index) in resume.education"
                    :key="index"
                    class="experience-item"
                  >
                    <div class="experience-header">
                      <h3>{{ edu.school }}</h3>
                      <span class="experience-time">{{ edu.startDate }} - {{ edu.endDate }}</span>
                    </div>
                    <p class="experience-position">{{ edu.major }} · {{ edu.degree }}</p>
                  </div>
                </div>
                <div v-else class="empty-placeholder">
                  <span class="empty-icon">+</span>
                  <span class="empty-text">添加教育经历，展示你的学历背景</span>
                </div>
              </div>
            </div>

            <div id="certificate" class="resume-section">
              <div class="section-header">
                <h2>资格证书</h2>
                <el-button type="primary" size="small" plain @click="startEdit('certificate')">
                  添加证书
                </el-button>
              </div>
              <div class="section-content">
                <div v-if="resume.certificates && resume.certificates.length > 0" class="tag-list">
                  <el-tag
                    v-for="cert in resume.certificates"
                    :key="cert"
                    type="success"
                    size="large"
                    >{{ cert }}</el-tag
                  >
                </div>
                <div v-else class="empty-placeholder">
                  <span class="empty-icon">+</span>
                  <span class="empty-text">添加资格证书，证明你的专业能力</span>
                </div>
              </div>
            </div>

            <div id="skills" class="resume-section">
              <div class="section-header">
                <h2>专业技能</h2>
                <el-button type="primary" size="small" plain @click="startEdit('skills')">
                  添加技能
                </el-button>
              </div>
              <div class="section-content">
                <div v-if="resume.skills && resume.skills.length > 0" class="skills-grid">
                  <div v-for="(skill, index) in resume.skills" :key="index" class="skill-tag">
                    {{ skill }}
                  </div>
                </div>
                <div v-else class="empty-placeholder">
                  <span class="empty-icon">+</span>
                  <span class="empty-text">添加专业技能，展示你的技术栈</span>
                </div>
              </div>
            </div>
          </div>

          <div class="resume-extra">
            <div class="diagnosis-card">
              <h3>简历诊断</h3>
              <div class="diagnosis-score">
                <span class="score-number">1</span>
                <span class="score-text">项待优化</span>
              </div>
              <div class="diagnosis-item">
                <span class="item-title">完善简历内容</span>
                <span class="item-count">1项建议完善</span>
              </div>
              <div
                class="diagnosis-suggestion"
                v-if="resume.education && resume.education.length > 0"
              >
                <div class="suggestion-header">
                  <span class="school-name">{{ resume.education[0].school }}</span>
                  <span class="school-time"
                    >{{ resume.education[0].startDate }} - {{ resume.education[0].endDate }}</span
                  >
                </div>
                <p class="suggestion-text">建议填写毕设/论文</p>
                <p class="suggestion-tip">
                  Boss对你的毕业论文很感兴趣，建议填写毕业设计或论文，以展现你的专业能力
                </p>
              </div>
            </div>

            <div class="privacy-card">
              <h3>隐私设置</h3>
              <div class="privacy-item">
                <span class="item-label">简历设置：对猎头中介等经纪人隐藏</span>
              </div>
              <div class="privacy-item">
                <span class="item-label">屏蔽公司：已屏蔽{{ blockedCompanies }}家</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
    <AppFooter />

    <el-dialog
      v-model="basicInfoDialogVisible"
      title="编辑基本信息"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="basicInfoForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="basicInfoForm.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="求职状态">
          <el-select v-model="basicInfoForm.jobStatus" placeholder="请选择求职状态">
            <el-option-group label="学生">
              <el-option label="在校-看看机会" value="在校-看看机会" />
              <el-option label="离校-随时到岗" value="离校-随时到岗" />
            </el-option-group>
            <el-option-group label="社会人士">
              <el-option label="在职-看看机会" value="在职-看看机会" />
              <el-option label="离职-随时到岗" value="离职-随时到岗" />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="basicInfoForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="basicInfoForm.email" placeholder="请输入邮箱" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="basicInfoDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveBasicInfo">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import SeekerNavBar from '@/components/SeekerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { resumeApi } from '@/api/resume'
import { useUserStore } from '@/store/user'

defineOptions({
  name: 'ResumePage',
})

const userStore = useUserStore()

const activeSection = ref('advantage')
const blockedCompanies = ref(0)

const menuItems = [
  { id: 'advantage', label: '个人优势' },
  { id: 'intention', label: '期望职位' },
  { id: 'work', label: '工作经历' },
  { id: 'project', label: '项目经历' },
  { id: 'education', label: '教育经历' },
  { id: 'certificate', label: '资格证书' },
  { id: 'skills', label: '专业技能' },
]

interface WorkExperience {
  company: string
  startDate: string
  endDate?: string
  position: string
  description: string
  tags?: string[]
}

interface ProjectExperience {
  name: string
  time: string
  role: string
  description: string
  tags?: string[]
}

interface Education {
  school: string
  startDate: string
  endDate: string
  major: string
  degree: string
  rank?: string
  courses?: string
  achievements?: string
}

const resume = ref({
  name: '',
  gender: '',
  jobStatus: '',
  phone: '',
  email: '',
  advantage: '',
  jobIntention: '',
  expectedSalary: '',
  expectedLocation: '',
  workExperience: [] as WorkExperience[],
  projectExperience: [] as ProjectExperience[],
  education: [] as Education[],
  certificates: [] as string[],
  skills: [] as string[],
})

const fetchResume = async (): Promise<void> => {
  if (!userStore.userInfo?.id) return

  try {
    const data = await resumeApi.getResume(userStore.userInfo.id)

    resume.value = {
      name: data.name || userStore.userInfo?.username || '',
      gender: '',
      jobStatus: '',
      phone: data.phone || userStore.userInfo?.phone || '',
      email: data.email || userStore.userInfo?.email || '',
      advantage: data.selfIntroduction || '',
      jobIntention: '',
      expectedSalary: '',
      expectedLocation: '',
      workExperience: parseJsonArray(data.workExperience) as WorkExperience[],
      projectExperience: [] as ProjectExperience[],
      education: parseJsonArray(data.education) as Education[],
      certificates: [] as string[],
      skills: parseSkills(data.skills),
    }
  } catch (error) {
    console.error('Failed to fetch resume:', error)
  }
}

const parseJsonArray = <T,>(data: T[] | string | undefined): T[] => {
  if (!data) return []
  if (Array.isArray(data)) return data
  try {
    const parsed = JSON.parse(data)
    return Array.isArray(parsed) ? parsed : []
  } catch {
    return []
  }
}

const parseSkills = (data: unknown): string[] => {
  if (!data) return []
  if (Array.isArray(data)) {
    return data.map((s) => {
      if (typeof s === 'string') return s
      if (typeof s === 'object' && s !== null && 'name' in s) return (s as { name: string }).name
      return String(s)
    })
  }
  if (typeof data === 'string') {
    try {
      const parsed = JSON.parse(data)
      return parseSkills(parsed)
    } catch {
      return data.split(',').map((s) => s.trim())
    }
  }
  return []
}

const scrollToSection = (sectionId: string): void => {
  activeSection.value = sectionId
  const element = document.getElementById(sectionId)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

const editingSection = ref('')
const editingContent = ref('')

const startEdit = (section: string): void => {
  editingSection.value = section
  switch (section) {
    case 'advantage':
      editingContent.value = resume.value.advantage
      break
    case 'intention':
      editingContent.value = resume.value.jobIntention
      break
    default:
      editingContent.value = ''
  }
}

const saveAdvantage = async (): Promise<void> => {
  resume.value.advantage = editingContent.value
  editingSection.value = ''
  ElMessage.success('保存成功')
}

const cancelEdit = (): void => {
  editingSection.value = ''
}

const basicInfoDialogVisible = ref(false)
const basicInfoForm = ref({
  name: '',
  gender: '',
  jobStatus: '',
  phone: '',
  email: '',
})

const editBasicInfo = (): void => {
  basicInfoForm.value = {
    name: resume.value.name,
    gender: resume.value.gender,
    jobStatus: resume.value.jobStatus,
    phone: resume.value.phone,
    email: resume.value.email,
  }
  basicInfoDialogVisible.value = true
}

const saveBasicInfo = async (): Promise<void> => {
  resume.value.name = basicInfoForm.value.name
  resume.value.gender = basicInfoForm.value.gender
  resume.value.jobStatus = basicInfoForm.value.jobStatus
  resume.value.phone = basicInfoForm.value.phone
  resume.value.email = basicInfoForm.value.email
  basicInfoDialogVisible.value = false
  ElMessage.success('保存成功')
}

onMounted(() => {
  fetchResume()
})
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.resume-page {
  min-height: 100vh;
  background: linear-gradient(180deg, rgba($primary-deep, 0.04) 0%, #f5f5f5 25%);
}

.main-content {
  padding: 20px 0;
}

.container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 0 24px;
}

.resume-layout {
  display: grid;
  grid-template-columns: 240px 1fr 280px;
  gap: 20px;
}

.resume-sidebar {
  background: white;
  border-radius: 8px;
  padding: 20px;
  position: sticky;
  top: 20px;
  height: fit-content;

  .resume-menu {
    margin-bottom: 20px;

    .menu-title {
      font-size: 14px;
      font-weight: 500;
      color: #222;
      margin-bottom: 12px;
    }

    .menu-item {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 10px 12px;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.2s;
      font-size: 14px;
      color: #666;

      &:hover {
        background: #f5f5f5;
        color: #222;
      }

      &.active {
        background: rgba($primary, 0.1);
        color: $primary;
        font-weight: 500;
      }
    }
  }
}

.resume-content {
  background: white;
  border-radius: 8px;
  padding: 24px;

  .content-header {
    margin-bottom: 32px;
    padding-bottom: 24px;
    border-bottom: 1px solid #f0f0f0;

    .header-top {
      display: flex;
      align-items: flex-start;
      gap: 20px;
      margin-bottom: 20px;
    }

    .avatar-section {
      .avatar {
        width: 80px;
        height: 80px;
        border-radius: 50%;
        background: linear-gradient(135deg, $primary 0%, $primary-deep 100%);
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 32px;
        font-weight: 500;
      }
    }

    .basic-info {
      flex: 1;

      h1 {
        font-size: 24px;
        font-weight: 500;
        color: #222;
        margin: 0 0 12px 0;
      }

      .info-row {
        display: flex;
        gap: 24px;
      }

      .info-item {
        display: flex;
        align-items: center;
        gap: 8px;

        .label {
          font-size: 14px;
          color: #999;
        }

        .value {
          font-size: 14px;
          color: #666;
        }
      }
    }

    .edit-btn {
      margin-top: 8px;
    }

    .contact-row {
      display: flex;
      gap: 40px;
      padding: 16px 0;
      border-top: 1px solid #f5f5f5;
    }

    .contact-item {
      display: flex;
      align-items: center;
      gap: 12px;

      .contact-label {
        font-size: 14px;
        color: #999;
      }

      .contact-value {
        font-size: 14px;
        color: #333;
      }
    }
  }

  .resume-section {
    margin-bottom: 32px;

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      h2 {
        font-size: 18px;
        font-weight: 500;
        color: #222;
        margin: 0;
      }
    }

    .section-content {
      font-size: 14px;
      line-height: 1.8;
      color: #666;
    }
  }
}

.edit-actions {
  display: flex;
  gap: 8px;
}

.empty-placeholder {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px;
  background: #fafafa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    background: #f5f5f5;
  }

  .empty-icon {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    border: 2px dashed #ddd;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    color: #ccc;
  }

  .empty-text {
    color: #999;
    font-size: 14px;
  }
}

.content-text {
  color: #333;
  line-height: 1.8;
  white-space: pre-wrap;
}

.intention-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;

  .intention-item {
    .item-label {
      display: block;
      font-size: 13px;
      color: #999;
      margin-bottom: 6px;
    }

    .item-value {
      display: block;
      font-size: 16px;
      color: #333;
      font-weight: 400;

      &.salary {
        color: $primary;
      }
    }
  }
}

.experience-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.experience-item {
  padding: 20px;
  background: #fafafa;
  border-radius: 8px;
  transition: all 0.2s;

  &:hover {
    background: #f5f5f5;
  }
}

.experience-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;

  h3 {
    font-size: 16px;
    font-weight: 500;
    color: #222;
    margin: 0;
  }

  .experience-time {
    font-size: 13px;
    color: #999;
  }
}

.experience-position {
  font-size: 14px;
  color: #666;
  margin: 0 0 8px 0;
}

.experience-desc {
  font-size: 14px;
  color: #999;
  margin: 0;
  line-height: 1.6;
}

.tag-list {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.skills-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;

  .skill-tag {
    padding: 8px 16px;
    background: #f0f0f0;
    border-radius: 4px;
    font-size: 14px;
    color: #666;
    transition: all 0.2s;

    &:hover {
      background: #e8e8e8;
    }
  }
}

.resume-extra {
  display: flex;
  flex-direction: column;
  gap: 16px;

  .diagnosis-card,
  .privacy-card,
  .tools-card,
  .attachment-card {
    background: white;
    border-radius: 8px;
    padding: 20px;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #222;
      margin: 0 0 16px 0;
    }
  }

  .diagnosis-card {
    .diagnosis-score {
      display: flex;
      align-items: baseline;
      gap: 4px;
      margin-bottom: 16px;

      .score-number {
        font-size: 24px;
        font-weight: 600;
        color: $primary;
      }

      .score-text {
        font-size: 14px;
        color: #666;
      }
    }

    .diagnosis-item {
      margin-bottom: 16px;

      .item-title {
        display: block;
        font-size: 14px;
        font-weight: 500;
        color: #222;
        margin-bottom: 4px;
      }

      .item-count {
        font-size: 12px;
        color: #999;
      }
    }

    .diagnosis-suggestion {
      padding: 12px;
      background: #fafafa;
      border-radius: 6px;

      .suggestion-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 8px;

        .school-name {
          font-size: 14px;
          font-weight: 500;
          color: #222;
        }

        .school-time {
          font-size: 12px;
          color: #999;
        }
      }

      .suggestion-text {
        font-size: 14px;
        color: #222;
        margin: 0 0 4px 0;
      }

      .suggestion-tip {
        font-size: 12px;
        color: #999;
        margin: 0;
        line-height: 1.5;
      }
    }
  }

  .privacy-card {
    .privacy-item {
      margin-bottom: 8px;

      .item-label {
        font-size: 14px;
        color: #666;
      }
    }
  }

  .tools-card {
    .tool-item {
      .tool-label {
        display: block;
        font-size: 14px;
        font-weight: 500;
        color: #222;
        margin-bottom: 4px;
      }

      .tool-desc {
        font-size: 12px;
        color: #999;
      }
    }
  }

  .attachment-card {
    .attachment-info {
      margin-bottom: 16px;

      .attachment-count {
        font-size: 14px;
        color: #666;
      }
    }

    .attachment-list {
      .attachment-item {
        display: flex;
        gap: 12px;
        padding: 12px;
        background: #fafafa;
        border-radius: 6px;

        .file-icon {
          width: 32px;
          height: 32px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: $primary;
        }

        .file-info {
          flex: 1;
          display: flex;
          flex-direction: column;
          gap: 4px;

          .file-name {
            font-size: 14px;
            color: #222;
          }

          .file-meta {
            font-size: 12px;
            color: #999;
          }
        }
      }
    }
  }
}
</style>
