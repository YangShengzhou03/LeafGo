<template>
  <div class="seeker-profile">
    <div class="container">
      <div class="profile-header">
        <el-avatar :size="80" :src="userStore.userInfo?.avatar">
          {{ userStore.userInfo?.username?.charAt(0) }}
        </el-avatar>
        <div class="profile-info">
          <h2>{{ userStore.userInfo?.username }}</h2>
          <p class="email">{{ userStore.userInfo?.email }}</p>
        </div>
      </div>

      <el-form
        ref="profileFormRef"
        :model="profileForm"
        :rules="profileRules"
        label-width="100px"
        class="profile-form"
      >
        <div class="form-section">
          <h3>基本信息</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="profileForm.username" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="profileForm.phone" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="邮箱">
                <el-input :model-value="userStore.userInfo?.email" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="用户类型">
                <el-tag type="primary">求职者</el-tag>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div class="form-section">
          <h3>修改密码</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="旧密码" prop="oldPassword">
                <el-input v-model="passwordForm.oldPassword" type="password" show-password />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="passwordForm.newPassword" type="password" show-password />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="确认密码" prop="confirmPassword">
                <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <el-form-item>
          <el-button type="primary" @click="handleSave" :loading="saving">保存修改</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { userApi } from '@/api'
import { useUserStore } from '@/store/user'
import type { User } from '@/types'

defineOptions({
  name: 'SeekerProfile',
})

const userStore = useUserStore()
const profileFormRef = ref<FormInstance>()
const saving = ref(false)

const profileForm = reactive({
  username: '',
  phone: '',
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})

const profileRules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
}

const handleSave = async (): Promise<void> => {
  saving.value = true
  try {
    if (profileForm.username !== userStore.userInfo?.username || profileForm.phone !== userStore.userInfo?.phone) {
      await userStore.updateUserInfo({
        username: profileForm.username,
        phone: profileForm.phone,
      } as Partial<User>)
      ElMessage.success('基本信息更新成功')
    }
    if (passwordForm.newPassword) {
      if (passwordForm.newPassword !== passwordForm.confirmPassword) {
        ElMessage.error('两次密码不一致')
        return
      }
      if (!passwordForm.oldPassword) {
        ElMessage.error('请输入旧密码')
        return
      }
      ElMessage.success('密码修改成功')
      passwordForm.oldPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
    }
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  if (userStore.userInfo) {
    profileForm.username = userStore.userInfo.username
    profileForm.phone = userStore.userInfo.phone || ''
  }
})
</script>

<style scoped lang="scss">
.seeker-profile {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 24px 0;

  .profile-header {
    background: #fff;
    border-radius: 12px;
    padding: 30px;
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .profile-info {
      h2 {
        margin: 0 0 4px 0;
        font-size: 20px;
        font-weight: 600;
      }

      .email {
        margin: 0;
        color: #909399;
        font-size: 14px;
      }
    }
  }

  .profile-form {
    background: #fff;
    border-radius: 12px;
    padding: 30px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .form-section {
      margin-bottom: 30px;
      padding-bottom: 20px;
      border-bottom: 1px solid #f0f2f5;

      h3 {
        margin: 0 0 20px 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }
  }
}
</style>
