<template>
  <div class="employer-company">
    <div class="page-header">
      <h2>企业信息</h2>
    </div>

    <el-form
      ref="companyFormRef"
      :model="companyForm"
      :rules="companyRules"
      label-width="120px"
      class="company-form"
    >
      <el-form-item label="企业名称" prop="name">
        <el-input v-model="companyForm.name" placeholder="请输入企业名称" />
      </el-form-item>

      <el-form-item label="企业Logo">
        <el-upload
          class="logo-uploader"
          action="http://localhost:8081/upload"
          :show-file-list="false"
          :on-success="handleLogoSuccess"
        >
          <img v-if="companyForm.logo" :src="companyForm.logo" class="logo" />
          <el-icon v-else class="logo-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业规模" prop="scale">
            <el-select v-model="companyForm.scale" placeholder="请选择">
              <el-option label="0-20人" value="0-20人" />
              <el-option label="20-99人" value="20-99人" />
              <el-option label="100-499人" value="100-499人" />
              <el-option label="500-999人" value="500-999人" />
              <el-option label="1000人以上" value="1000人以上" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属行业" prop="industry">
            <el-select v-model="companyForm.industry" placeholder="请选择">
              <el-option label="互联网/IT" value="互联网/IT" />
              <el-option label="金融" value="金融" />
              <el-option label="教育" value="教育" />
              <el-option label="制造业" value="制造业" />
              <el-option label="医疗健康" value="医疗健康" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="公司地址" required>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item prop="province">
              <el-input v-model="companyForm.province" placeholder="省份" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="city">
              <el-input v-model="companyForm.city" placeholder="城市" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="district">
              <el-input v-model="companyForm.district" placeholder="区/县" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-input
          v-model="companyForm.address"
          placeholder="详细地址（可选）"
          style="margin-top: 10px"
        />
      </el-form-item>

      <el-form-item label="公司官网">
        <el-input v-model="companyForm.website" placeholder="请输入公司官网" />
      </el-form-item>

      <el-form-item label="公司简介" prop="description">
        <el-input
          v-model="companyForm.description"
          type="textarea"
          :rows="6"
          placeholder="请详细介绍公司情况"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="handleSubmit" :loading="submitting"> 保存 </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { companyApi } from '@/api'
import type { Company } from '@/types'

defineOptions({
  name: 'EmployerCompany',
})

const companyFormRef = ref<FormInstance>()
const submitting = ref(false)
const companyId = ref<number | null>(null)

const companyForm = reactive({
  name: '',
  logo: '',
  scale: '',
  industry: '',
  province: '',
  city: '',
  district: '',
  address: '',
  website: '',
  description: '',
})

const companyRules: FormRules = {
  name: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  scale: [{ required: true, message: '请选择企业规模', trigger: 'change' }],
  industry: [{ required: true, message: '请选择所属行业', trigger: 'change' }],
  city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
  description: [{ required: true, message: '请输入公司简介', trigger: 'blur' }],
}

const handleLogoSuccess = (response: { data: string }): void => {
  companyForm.logo = response.data
}

const fetchCompanyInfo = async (): Promise<void> => {
  try {
    const company = await companyApi.getMyCompany()
    if (company) {
      companyId.value = company.id
      Object.assign(companyForm, company)
    }
  } catch {
    // 暂无企业信息
  }
}

const handleSubmit = async (): Promise<void> => {
  if (!companyFormRef.value) return

  await companyFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (!companyId.value) {
          ElMessage.error('企业信息不存在')
          return
        }
        await companyApi.updateCompany(companyId.value, companyForm as Partial<Company>)
        ElMessage.success('保存成功')
      } finally {
        submitting.value = false
      }
    }
  })
}

onMounted(() => {
  fetchCompanyInfo()
})
</script>

<style scoped lang="scss">
.employer-company {
  .page-header {
    margin-bottom: 20px;

    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
    }
  }

  .company-form {
    background: #fff;
    padding: 24px;
    border-radius: 8px;
    max-width: 800px;

    .logo-uploader {
      :deep(.el-upload) {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        width: 120px;
        height: 120px;
        display: flex;
        align-items: center;
        justify-content: center;

        &:hover {
          border-color: #409eff;
        }
      }

      .logo {
        width: 120px;
        height: 120px;
        object-fit: cover;
      }

      .logo-uploader-icon {
        font-size: 28px;
        color: #8c939d;
      }
    }
  }
}
</style>
