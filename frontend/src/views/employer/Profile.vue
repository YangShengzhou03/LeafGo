<template>
  <div class="employer-profile">
    <EmployerNavBar />
    <main class="main-content">
      <div class="container">
        <el-breadcrumb separator="/" class="breadcrumb">
          <el-breadcrumb-item :to="{ path: '/employer' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>企业信息</el-breadcrumb-item>
        </el-breadcrumb>

        <div class="profile-header">
          <div class="user-info-card">
            <el-avatar :size="80" class="user-avatar">
              {{ userStore.userInfo?.username?.charAt(0) || 'H' }}
            </el-avatar>
            <div class="user-info-right">
              <h1 class="username">{{ userStore.userInfo?.username || 'HR' }}</h1>
              <div class="user-meta">
                <el-tag type="success">招聘者</el-tag>
                <span class="company-name" v-if="company">{{ company.name }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="profile-content">
          <div class="profile-section">
            <div class="section-header">
              <h2>企业信息</h2>
              <el-button v-if="!company" type="primary" @click="showCreateDialog = true">
                <el-icon><Plus /></el-icon>
                创建企业
              </el-button>
            </div>

            <div v-if="company" class="company-info">
              <el-descriptions :column="2" border class="company-desc">
                <el-descriptions-item label="企业名称">{{ companyForm.name }}</el-descriptions-item>
                <el-descriptions-item label="企业规模">{{ companyForm.scale || '未设置' }}</el-descriptions-item>
                <el-descriptions-item label="所属行业">{{ companyForm.industry || '未设置' }}</el-descriptions-item>
                <el-descriptions-item label="公司官网">{{ companyForm.website || '未设置' }}</el-descriptions-item>
                <el-descriptions-item label="公司地址" :span="2">
                  {{ companyForm.province }}{{ companyForm.city }}{{ companyForm.district }}{{ companyForm.address }}
                </el-descriptions-item>
                <el-descriptions-item label="公司简介" :span="2">
                  {{ companyForm.description || '暂无简介' }}
                </el-descriptions-item>
              </el-descriptions>

              <div class="action-buttons">
                <span class="link-text" @click="showEditDialog = true">修改</span>
              </div>
            </div>

            <div v-else class="no-company">
              <el-empty description="您还未创建企业">
                <el-button type="primary" @click="showCreateDialog = true">创建企业</el-button>
              </el-empty>
            </div>
          </div>
        </div>
      </div>
    </main>
    <AppFooter />

    <el-dialog v-model="showCreateDialog" title="创建企业" width="600px">
      <el-form
        ref="createCompanyFormRef"
        :model="createCompanyForm"
        :rules="companyRules"
        label-width="120px"
      >
        <el-form-item label="企业名称" prop="name">
          <el-input v-model="createCompanyForm.name" placeholder="请输入企业名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="企业规模" prop="scale">
              <el-select v-model="createCompanyForm.scale" placeholder="请选择">
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
              <el-select v-model="createCompanyForm.industry" placeholder="请选择">
                <el-option label="互联网/IT" value="互联网/IT" />
                <el-option label="金融" value="金融" />
                <el-option label="教育" value="教育" />
                <el-option label="制造业" value="制造业" />
                <el-option label="医疗健康" value="医疗健康" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="公司简介" prop="description">
          <el-input
            v-model="createCompanyForm.description"
            type="textarea"
            :rows="4"
            placeholder="请介绍公司情况"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreateCompany" :loading="creating">创建</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showEditDialog" title="编辑企业信息" width="700px">
      <el-form
        ref="companyFormRef"
        :model="companyForm"
        :rules="companyRules"
        label-width="120px"
      >
        <el-form-item label="企业名称" prop="name">
          <el-input v-model="companyForm.name" placeholder="请输入企业名称" />
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
            :rows="4"
            placeholder="请介绍公司情况"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateCompany" :loading="submitting">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import EmployerNavBar from '@/components/EmployerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { useUserStore } from '@/store/user'
import { companyApi } from '@/api/company'
import type { Company } from '@/types'
import type { FormInstance, FormRules } from 'element-plus'

defineOptions({
  name: 'EmployerProfile',
})

const userStore = useUserStore()
const company = ref<Company | null>(null)
const submitting = ref(false)
const creating = ref(false)
const showCreateDialog = ref(false)
const showEditDialog = ref(false)

const companyFormRef = ref<FormInstance>()
const createCompanyFormRef = ref<FormInstance>()

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

const createCompanyForm = reactive({
  name: '',
  scale: '',
  industry: '',
  description: '',
})

const companyRules: FormRules = {
  name: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  scale: [{ required: true, message: '请选择企业规模', trigger: 'change' }],
  industry: [{ required: true, message: '请选择所属行业', trigger: 'change' }],
  province: [{ required: true, message: '请输入省份', trigger: 'blur' }],
  city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
  district: [{ required: true, message: '请输入区/县', trigger: 'blur' }],
  description: [{ required: true, message: '请输入公司简介', trigger: 'blur' }],
}

const fetchCompany = async (): Promise<void> => {
  try {
    const data = await companyApi.getMyCompany()
    company.value = data
    if (data) {
      Object.assign(companyForm, {
        name: data.name || '',
        logo: data.logo || '',
        scale: data.scale || '',
        industry: data.industry || '',
        province: data.province || '',
        city: data.city || '',
        district: data.district || '',
        address: data.address || '',
        website: data.website || '',
        description: data.description || '',
      })
    }
  } catch {
    console.error('获取企业信息失败')
  }
}

const handleLogoSuccess = (response: { url: string }): void => {
  companyForm.logo = response.url
  ElMessage.success('Logo上传成功')
}

const handleUpdateCompany = async (): Promise<void> => {
  if (!companyFormRef.value || !company.value) return

  await companyFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        await companyApi.updateCompany(company.value!.id, companyForm)
        ElMessage.success('企业信息更新成功')
        showEditDialog.value = false
        await fetchCompany()
      } catch {
        ElMessage.error('更新失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleCreateCompany = async (): Promise<void> => {
  if (!createCompanyFormRef.value) return

  await createCompanyFormRef.value.validate(async (valid) => {
    if (valid) {
      creating.value = true
      try {
        await companyApi.createCompany(createCompanyForm)
        ElMessage.success('企业创建成功')
        showCreateDialog.value = false
        await fetchCompany()
      } catch {
        ElMessage.error('创建失败')
      } finally {
        creating.value = false
      }
    }
  })
}

onMounted(() => {
  fetchCompany()
})
</script>

<style scoped lang="scss">
.employer-profile {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

.main-content {
  flex: 1;
  padding: 24px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.breadcrumb {
  margin-bottom: 24px;
}

.profile-header {
  margin-bottom: 24px;
}

.user-info-card {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.user-avatar {
  background: #409eff;
  color: #fff;
  font-size: 32px;
}

.user-info-right {
  flex: 1;
}

.username {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #303133;
}

.user-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.company-name {
  font-size: 14px;
  color: #909399;
}

.profile-content {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.profile-section {
  h2 {
    font-size: 20px;
    font-weight: 600;
    margin: 0 0 24px 0;
    color: #303133;
  }
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;

  h2 {
    margin: 0;
  }
}

.company-desc {
  margin-bottom: 24px;
}

.action-buttons {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.no-company {
  padding: 40px 0;
}

.link-text {
  color: #00b85c;
  cursor: pointer;
  font-size: 14px;

  &:hover {
    color: #85ce61;
    text-decoration: underline;
  }
}
</style>
