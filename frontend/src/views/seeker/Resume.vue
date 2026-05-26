<template>
  <div class="seeker-resume">
    <div class="container">
      <div class="resume-header">
        <h2>我的简历</h2>
        <el-button type="primary" @click="handleSave" :loading="saving"> 保存简历 </el-button>
      </div>

      <el-form
        ref="resumeFormRef"
        :model="resumeForm"
        :rules="resumeRules"
        label-width="100px"
        class="resume-form"
      >
        <div class="form-section">
          <h3>基本信息</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="resumeForm.name" placeholder="请输入姓名" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="resumeForm.phone" placeholder="请输入手机号" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="resumeForm.email" placeholder="请输入邮箱" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div class="form-section">
          <h3>自我介绍</h3>
          <el-form-item prop="selfIntroduction">
            <el-input
              v-model="resumeForm.selfIntroduction"
              type="textarea"
              :rows="4"
              placeholder="请输入自我介绍"
            />
          </el-form-item>
        </div>

        <div class="form-section">
          <h3>教育经历</h3>
          <div v-for="(edu, index) in resumeForm.education" :key="index" class="list-item">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="学校">
                  <el-input v-model="edu.school" placeholder="请输入学校名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="专业">
                  <el-input v-model="edu.major" placeholder="请输入专业" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="学历">
                  <el-select v-model="edu.degree" placeholder="请选择学历">
                    <el-option label="大专" value="大专" />
                    <el-option label="本科" value="本科" />
                    <el-option label="硕士" value="硕士" />
                    <el-option label="博士" value="博士" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="时间段">
                  <el-date-picker
                    v-model="edu.dateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始时间"
                    end-placeholder="结束时间"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-button
              type="danger"
              text
              @click="removeEducation(index)"
              v-if="resumeForm.education.length > 1"
            >
              删除
            </el-button>
          </div>
          <el-button type="primary" text @click="addEducation">
            <el-icon><Plus /></el-icon>
            添加教育经历
          </el-button>
        </div>

        <div class="form-section">
          <h3>工作经历</h3>
          <div v-for="(work, index) in resumeForm.workExperience" :key="index" class="list-item">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="公司">
                  <el-input v-model="work.company" placeholder="请输入公司名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="职位">
                  <el-input v-model="work.position" placeholder="请输入职位" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="工作描述">
              <el-input
                v-model="work.description"
                type="textarea"
                :rows="3"
                placeholder="请描述工作内容和成就"
              />
            </el-form-item>
            <el-button
              type="danger"
              text
              @click="removeWork(index)"
              v-if="resumeForm.workExperience.length > 1"
            >
              删除
            </el-button>
          </div>
          <el-button type="primary" text @click="addWork">
            <el-icon><Plus /></el-icon>
            添加工作经历
          </el-button>
        </div>

        <div class="form-section">
          <h3>技能特长</h3>
          <div v-for="(skill, index) in resumeForm.skills" :key="index" class="skill-item">
            <el-input v-model="skill.name" placeholder="技能名称" style="width: 200px" />
            <el-select v-model="skill.level" placeholder="熟练程度" style="width: 150px">
              <el-option label="了解" value="了解" />
              <el-option label="熟练" value="熟练" />
              <el-option label="精通" value="精通" />
            </el-select>
            <el-button
              type="danger"
              text
              @click="removeSkill(index)"
              v-if="resumeForm.skills.length > 1"
            >
              删除
            </el-button>
          </div>
          <el-button type="primary" text @click="addSkill">
            <el-icon><Plus /></el-icon>
            添加技能
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: 'SeekerResume',
})
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { resumeApi } from '@/api'
import type { Resume } from '@/types'
import { useUserStore } from '@/store/user'

interface EducationFormItem {
  school: string
  major: string
  degree: string
  dateRange: [string, string] | []
}

interface WorkExperienceFormItem {
  company: string
  position: string
  description: string
}

interface SkillFormItem {
  name: string
  level: string
}

interface ResumeFormData {
  name: string
  phone: string
  email: string
  selfIntroduction: string
  education: EducationFormItem[]
  workExperience: WorkExperienceFormItem[]
  skills: SkillFormItem[]
}

const userStore = useUserStore()
const resumeFormRef = ref<FormInstance>()
const saving = ref(false)

const resumeForm = reactive<ResumeFormData>({
  name: '',
  phone: '',
  email: '',
  selfIntroduction: '',
  education: [
    {
      school: '',
      major: '',
      degree: '',
      dateRange: [],
    },
  ],
  workExperience: [
    {
      company: '',
      position: '',
      description: '',
    },
  ],
  skills: [
    {
      name: '',
      level: '',
    },
  ],
})

const resumeRules: FormRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' },
  ],
}

const addEducation = (): void => {
  resumeForm.education.push({
    school: '',
    major: '',
    degree: '',
    dateRange: [],
  })
}

const removeEducation = (index: number): void => {
  resumeForm.education.splice(index, 1)
}

const addWork = (): void => {
  resumeForm.workExperience.push({
    company: '',
    position: '',
    description: '',
  })
}

const removeWork = (index: number): void => {
  resumeForm.workExperience.splice(index, 1)
}

const addSkill = (): void => {
  resumeForm.skills.push({
    name: '',
    level: '',
  })
}

const removeSkill = (index: number): void => {
  resumeForm.skills.splice(index, 1)
}

const handleSave = async (): Promise<void> => {
  if (!resumeFormRef.value) return

  await resumeFormRef.value.validate(async (valid) => {
    if (valid) {
      saving.value = true
      try {
        const payload = {
          name: resumeForm.name,
          phone: resumeForm.phone,
          email: resumeForm.email,
          selfIntroduction: resumeForm.selfIntroduction,
          education: JSON.stringify(resumeForm.education),
          workExperience: JSON.stringify(resumeForm.workExperience),
          skills: JSON.stringify(resumeForm.skills),
        }
        await resumeApi.updateResume(userStore.userInfo!.id, payload as unknown as Partial<Resume>)
        ElMessage.success('简历保存成功')
      } finally {
        saving.value = false
      }
    }
  })
}

onMounted(async (): Promise<void> => {
  if (userStore.userInfo) {
    try {
      const resume = await resumeApi.getResume(userStore.userInfo.id)
      resumeForm.name = resume.name || ''
      resumeForm.phone = resume.phone || ''
      resumeForm.email = resume.email || ''
      resumeForm.selfIntroduction = resume.selfIntroduction || ''
      if (typeof resume.education === 'string' && resume.education) {
        const eduArray = JSON.parse(resume.education)
        resumeForm.education = eduArray.map((edu: Education) => ({
          school: edu.school,
          major: edu.major,
          degree: edu.degree,
          dateRange: [edu.startDate, edu.endDate] as [string, string]
        }))
      } else if (Array.isArray(resume.education)) {
        resumeForm.education = resume.education.map((edu) => ({
          school: edu.school,
          major: edu.major,
          degree: edu.degree,
          dateRange: [edu.startDate, edu.endDate] as [string, string]
        }))
      }
      if (typeof resume.workExperience === 'string' && resume.workExperience) {
        resumeForm.workExperience = JSON.parse(resume.workExperience)
      } else if (Array.isArray(resume.workExperience)) {
        resumeForm.workExperience = resume.workExperience
      }
      if (typeof resume.skills === 'string' && resume.skills) {
        resumeForm.skills = JSON.parse(resume.skills)
      } else if (Array.isArray(resume.skills)) {
        resumeForm.skills = resume.skills
      }
    } catch {
      // 暂无简历数据
    }
  }
})
</script>

<style scoped lang="scss">
.seeker-resume {
  .resume-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
      color: #303133;
    }
  }

  .resume-form {
    background: #fff;
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .form-section {
      margin-bottom: 30px;
      padding-bottom: 20px;
      border-bottom: 1px solid #f0f2f5;

      &:last-child {
        border-bottom: none;
        margin-bottom: 0;
      }

      h3 {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 20px 0;
      }

      .list-item {
        background: #f5f7fa;
        padding: 16px;
        border-radius: 8px;
        margin-bottom: 16px;
      }

      .skill-item {
        display: flex;
        gap: 12px;
        align-items: center;
        margin-bottom: 12px;
      }
    }
  }
}
</style>
