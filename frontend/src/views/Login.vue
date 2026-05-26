<template>
  <main class="login-page">
    <section class="login-shell">
      <aside class="brand-panel">
        <div class="brand-mark">LeafGO</div>
        <h1>找工作，招人才</h1>
        <p>简单直接，高效匹配</p>
      </aside>

      <section class="form-panel">
        <div class="form-head">
          <h4>验证码登录/注册</h4>
          <p>首次验证通过即注册 LeafGO 账号</p>
        </div>

        <div class="role-switch">
          <button
            type="button"
            :class="{ active: userType === 'JOB_SEEKER' }"
            @click="userType = 'JOB_SEEKER'"
          >
            找工作
          </button>
          <button
            type="button"
            :class="{ active: userType === 'EMPLOYER' }"
            @click="userType = 'EMPLOYER'"
          >
            招人才
          </button>
        </div>

        <el-form class="account-form" @submit.prevent>
          <el-form-item>
            <el-input
              v-model="form.phone"
              size="large"
              placeholder="请输入手机号"
              :prefix-icon="Iphone"
              maxlength="11"
            />
          </el-form-item>

          <el-form-item>
            <div class="code-row">
              <el-input
                v-model="form.code"
                size="large"
                placeholder="请输入验证码"
                :prefix-icon="Key"
                maxlength="6"
                @keyup.enter="submit"
              />
              <button class="code-btn" :disabled="codeSending || countdown > 0" @click="sendCode">
                {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
              </button>
            </div>
          </el-form-item>

          <label class="agreement-line">
            <el-checkbox v-model="agreed" />
            <span>
              已阅读并同意
              <RouterLink to="/terms">用户协议</RouterLink>
              和
              <RouterLink to="/privacy-policy">隐私政策</RouterLink>
            </span>
          </label>

          <el-button
            type="primary"
            size="large"
            class="submit-btn"
            :loading="loading"
            @click="submit"
          >
            登录/注册
          </el-button>
        </el-form>
      </section>
    </section>
  </main>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Iphone, Key } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'

defineOptions({
  name: 'LoginPage',
})

type UserType = 'JOB_SEEKER' | 'EMPLOYER'

const router = useRouter()
const userStore = useUserStore()

const userType = ref<UserType>('JOB_SEEKER')
const loading = ref(false)
const agreed = ref(false)
const codeSending = ref(false)
const countdown = ref(0)

const form = reactive({
  phone: '',
  code: '',
})

let timer: ReturnType<typeof setInterval> | null = null

const startCountdown = (): void => {
  countdown.value = 60
  timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      if (timer) clearInterval(timer)
      timer = null
    }
  }, 1000)
}

const sendCode = async (): Promise<void> => {
  if (!/^1\d{10}$/.test(form.phone)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }

  codeSending.value = true
  try {
    ElMessage.success('验证码已发送')
    startCountdown()
  } catch {
    ElMessage.error('发送失败，请稍后重试')
  } finally {
    codeSending.value = false
  }
}

const validate = (): boolean => {
  if (!agreed.value) {
    ElMessage.warning('请先阅读并同意用户协议和隐私政策')
    return false
  }
  if (!/^1\d{10}$/.test(form.phone)) {
    ElMessage.warning('请输入正确的手机号')
    return false
  }
  if (form.code.length < 4) {
    ElMessage.warning('请输入验证码')
    return false
  }
  return true
}

const submit = async (): Promise<void> => {
  if (!validate()) return

  loading.value = true
  try {
    try {
      const res = await userStore.login(form.phone, form.code)
      router.push(res.userType === 'EMPLOYER' ? '/employer' : '/seeker')
      ElMessage.success('登录成功')
    } catch {
      await userStore.register({
        username: form.phone,
        phone: form.phone,
        code: form.code,
        userType: userType.value,
      })
      router.push(userType.value === 'EMPLOYER' ? '/employer' : '/seeker')
      ElMessage.success('注册成功')
    }
  } catch (error) {
    const message = error instanceof Error ? error.message : '操作失败，请稍后重试'
    ElMessage.error(message)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background: #f5f5f5;
}

.login-shell {
  width: min(100%, 700px);
  display: grid;
  grid-template-columns: 280px 1fr;
  background: #fff;
  border-radius: $rounded-lg;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
}

.brand-panel {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 10px;
  padding: 40px 32px;
  background: linear-gradient(135deg, #f0fff4 0%, #fff 100%);

  .brand-mark {
    display: inline-flex;
    align-items: center;
    height: 26px;
    padding: 0 10px;
    border-radius: $rounded-sm;
    background: $primary;
    color: #fff;
    font-size: 12px;
    font-weight: 700;
    width: fit-content;
  }

  h1 {
    margin: 10px 0 0;
    color: #222;
    font-size: 24px;
    line-height: 1.3;
    font-weight: 700;
  }

  p {
    margin: 0;
    color: #999;
    font-size: 14px;
    line-height: 1.6;
  }
}

.form-panel {
  min-width: 0;
  padding: 36px 40px 32px;
}

.form-head {
  margin-bottom: 24px;

  h2 {
    margin: 0;
    color: #222;
    font-size: 24px;
    line-height: 1.3;
  }

  p {
    margin: 6px 0 0;
    color: #999;
    font-size: 13px;
  }
}

.role-switch {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 8px;
  margin-bottom: 24px;

  button {
    height: 38px;
    border-radius: $rounded-md;
    color: #999;
    font-size: 14px;
    border: 1px solid #eee;
    background: #fff;
    transition: all 0.2s;

    &.active {
      color: $primary;
      border-color: $primary;
      background: #f0fff4;
    }

    &:hover {
      color: $primary;
      border-color: $primary;
    }
  }
}

.account-form {
  :deep(.el-form-item) {
    margin-bottom: 20px;
  }

  :deep(.el-input__wrapper) {
    min-height: 44px;
    border-radius: $rounded-md;
    background: #fafafa;
    box-shadow: inset 0 0 0 1px #eee;
    transition:
      box-shadow 0.2s,
      background 0.2s;
  }

  :deep(.el-input__wrapper:hover),
  :deep(.el-input__wrapper.is-focus) {
    background: #fff;
    box-shadow: inset 0 0 0 1px $primary;
  }
}

.code-row {
  position: relative;
  width: 100%;

  :deep(.el-input__wrapper) {
    padding-right: 110px;
  }
}

.code-btn {
  position: absolute;
  right: 4px;
  top: 4px;
  height: 36px;
  padding: 0 14px;
  border-radius: $rounded-sm;
  border: none;
  color: $primary;
  background: transparent;
  font-size: 13px;
  white-space: nowrap;
  cursor: pointer;
  transition: color 0.2s;
  z-index: 1;

  &:hover:not(:disabled) {
    color: $primary-deep;
  }

  &:disabled {
    color: #bbb;
    cursor: not-allowed;
  }
}

.agreement-line {
  display: flex;
  align-items: flex-start;
  gap: 6px;
  margin: 0 0 20px;
  color: #999;
  font-size: 12px;
  line-height: 1.6;

  :deep(.el-checkbox) {
    height: 20px;
  }

  a {
    color: $primary;
  }
}

.submit-btn {
  width: 100%;
  height: 44px;
  border-radius: $rounded-md;
  background: $primary;
  border-color: $primary;
  font-size: 15px;
  letter-spacing: 0.5px;
  transition:
    background 0.2s,
    border-color 0.2s;

  &:hover,
  &:focus {
    background: $primary-deep;
    border-color: $primary-deep;
  }
}

@media (max-width: 820px) {
  .login-page {
    align-items: flex-start;
    padding: 12px;
  }

  .login-shell {
    grid-template-columns: 1fr;
  }

  .brand-panel {
    display: none;
  }

  .form-panel {
    padding: 24px 20px;
  }
}
</style>
