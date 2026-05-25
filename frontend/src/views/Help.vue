<template>
  <div class="static-page">
    <NavBar />
    <div class="page-container">
      <h1>使用帮助</h1>
      <p>快速了解LeafGO职达招聘平台的使用方法</p>

      <div class="help-tabs">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="求职者指南" name="seeker">
            <div class="help-content">
              <div class="help-section">
                <h3>如何注册账号？</h3>
                <p>
                  点击页面右上角"注册"按钮，填写手机号、验证码和密码即可完成注册。注册后可完善简历信息，开始求职之旅。
                </p>
              </div>
              <div class="help-section">
                <h3>如何创建简历？</h3>
                <p>
                  登录后进入"我的简历"页面，填写基本信息、教育经历、工作经历等内容。完善的简历能获得更多HR的关注。
                </p>
              </div>
              <div class="help-section">
                <h3>如何投递职位？</h3>
                <p>
                  在职位详情页点击"立即沟通"或"投递简历"按钮，即可向HR发送求职意向。HR会在第一时间查看您的简历。
                </p>
              </div>
              <div class="help-section">
                <h3>如何与HR沟通？</h3>
                <p>
                  进入"消息"页面，可以查看HR的回复并进行在线沟通。及时回复消息能提高求职成功率。
                </p>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="企业招聘指南" name="employer">
            <div class="help-content">
              <div class="help-section">
                <h3>如何发布职位？</h3>
                <p>
                  企业认证通过后，进入"职位管理"页面，点击"发布职位"，填写职位名称、薪资、要求等信息即可发布。
                </p>
              </div>
              <div class="help-section">
                <h3>如何搜索人才？</h3>
                <p>
                  进入"人才搜索"页面，通过职位、技能、学历等条件筛选，找到匹配的求职者并主动发起沟通。
                </p>
              </div>
              <div class="help-section">
                <h3>如何管理候选人？</h3>
                <p>在"候选人管理"页面可以查看所有投递简历的求职者，进行筛选、面试安排等操作。</p>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="常见问题" name="faq">
            <div class="help-content">
              <el-collapse>
                <el-collapse-item title="忘记密码怎么办？" name="1">
                  <p>点击登录页面的"忘记密码"，通过手机验证码重置密码。</p>
                </el-collapse-item>
                <el-collapse-item title="如何修改绑定的手机号？" name="2">
                  <p>进入"账号设置"页面，在"安全设置"中可以更换绑定的手机号。</p>
                </el-collapse-item>
                <el-collapse-item title="如何隐藏简历？" name="3">
                  <p>在"简历设置"中可以将简历状态设为"隐藏"，隐藏后HR将无法搜索到您的简历。</p>
                </el-collapse-item>
                <el-collapse-item title="如何举报违规职位？" name="4">
                  <p>在职位详情页点击"举报"按钮，选择举报原因并提交，我们会尽快处理。</p>
                </el-collapse-item>
                <el-collapse-item title="如何注销账号？" name="5">
                  <p>
                    进入"账号设置"页面，在"账号注销"中申请注销。注销后数据将无法恢复，请谨慎操作。
                  </p>
                </el-collapse-item>
              </el-collapse>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import NavBar from '@/components/NavBar.vue'
import AppFooter from '@/components/AppFooter.vue'

defineOptions({
  name: 'HelpPage',
})

const activeTab = ref('seeker')
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.static-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.page-container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 40px 24px;

  h1 {
    font-size: 28px;
    color: #222;
    margin-bottom: 16px;
  }

  > p {
    font-size: 16px;
    color: #666;
    margin-bottom: 32px;
  }
}

.help-tabs {
  background: white;
  padding: 24px;
  border-radius: 8px;
}

.help-content {
  padding-top: 16px;
}

.help-section {
  padding: 20px;
  background: #fafafa;
  border-radius: 8px;
  margin-bottom: 16px;

  h3 {
    font-size: 16px;
    color: #222;
    margin-bottom: 12px;
  }

  p {
    font-size: 14px;
    color: #666;
    margin: 0;
    line-height: 1.8;
  }
}
</style>
