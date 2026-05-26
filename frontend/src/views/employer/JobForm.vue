<template>
  <div class="employer-job-form">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑职位' : '发布新职位' }}</h2>
    </div>

    <el-form
      ref="jobFormRef"
      :model="jobForm"
      :rules="jobRules"
      label-width="120px"
      class="job-form"
    >
      <el-form-item label="职位名称" prop="title">
        <el-input v-model="jobForm.title" placeholder="请输入职位名称" />
      </el-form-item>

      <el-form-item label="工作地点" required>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item prop="province">
              <el-input v-model="jobForm.province" placeholder="省份" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="city">
              <el-input v-model="jobForm.city" placeholder="城市" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="district">
              <el-input v-model="jobForm.district" placeholder="区/县" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-input v-model="jobForm.address" placeholder="详细地址（可选）" style="margin-top: 10px" />
      </el-form-item>

      <el-form-item label="薪资范围" prop="salary">
        <el-input v-model="jobForm.salary" placeholder="如：10-15K" />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="职位类型" prop="jobType">
            <el-select v-model="jobForm.jobType" placeholder="请选择">
              <el-option label="全职" value="全职" />
              <el-option label="兼职" value="兼职" />
              <el-option label="实习" value="实习" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工作经验" prop="experience">
            <el-select v-model="jobForm.experience" placeholder="请选择">
              <el-option label="经验不限" value="经验不限" />
              <el-option label="1-3年" value="1-3年" />
              <el-option label="3-5年" value="3-5年" />
              <el-option label="5-10年" value="5-10年" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="学历要求" prop="education">
        <el-select v-model="jobForm.education" placeholder="请选择">
          <el-option label="学历不限" value="学历不限" />
          <el-option label="大专" value="大专" />
          <el-option label="本科" value="本科" />
          <el-option label="硕士" value="硕士" />
        </el-select>
      </el-form-item>

      <el-form-item label="职位描述" prop="description">
        <el-input
          v-model="jobForm.description"
          type="textarea"
          :rows="10"
          placeholder="请详细描述职位职责和任职要求&#10;&#10;示例：&#10;岗位职责：&#10;负责核心系统开发和架构设计&#10;&#10;任职要求：&#10;精通Java，熟悉Spring Boot"
        />
      </el-form-item>

      <el-form-item label="福利待遇">
        <el-select
          v-model="jobForm.benefits"
          multiple
          filterable
          allow-create
          placeholder="请选择或输入福利待遇"
        >
          <el-option label="五险一金" value="五险一金" />
          <el-option label="年终奖" value="年终奖" />
          <el-option label="股票期权" value="股票期权" />
          <el-option label="带薪年假" value="带薪年假" />
          <el-option label="免费三餐" value="免费三餐" />
          <el-option label="弹性工作" value="弹性工作" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          {{ isEdit ? '保存' : '发布' }}
        </el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { jobApi } from '@/api'
import type { Job } from '@/types'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)
const jobFormRef = ref<FormInstance>()
const submitting = ref(false)

const jobForm = reactive({
  title: '',
  province: '',
  city: '',
  district: '',
  address: '',
  salary: '',
  jobType: '',
  experience: '',
  education: '',
  description: '',
  benefits: [] as string[],
})

const jobRules: FormRules = {
  title: [{ required: true, message: '请输入职位名称', trigger: 'blur' }],
  city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
  salary: [{ required: true, message: '请输入薪资范围', trigger: 'blur' }],
  jobType: [{ required: true, message: '请选择职位类型', trigger: 'change' }],
  description: [{ required: true, message: '请输入职位描述', trigger: 'blur' }],
}

const fetchJobDetail = async (): Promise<void> => {
  const jobId = Number(route.params.id)
  const job = await jobApi.getJobDetail(jobId)
  Object.assign(jobForm, job)
}

const handleSubmit = async (): Promise<void> => {
  if (!jobFormRef.value) return

  await jobFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (isEdit.value) {
          await jobApi.updateJob(Number(route.params.id), jobForm as Partial<Job>)
          ElMessage.success('职位更新成功')
        } else {
          await jobApi.createJob(jobForm as Partial<Job>)
          ElMessage.success('职位发布成功')
        }
        router.push('/employer/jobs')
      } finally {
        submitting.value = false
      }
    }
  })
}

onMounted(() => {
  if (isEdit.value) {
    fetchJobDetail()
  }
})
</script>

<style scoped lang="scss">
.employer-job-form {
  .page-header {
    margin-bottom: 20px;

    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
    }
  }

  .job-form {
    background: #fff;
    padding: 24px;
    border-radius: 8px;
    max-width: 800px;
  }
}
</style>
