<template>
  <div class="messages-page">
    <SeekerNavBar />
    <main class="main-content">
      <div class="container">
        <div class="messages-layout">
          <div class="conversations-list">
            <div class="search-box">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索30天内的联系人"
                prefix-icon="Search"
                clearable
              />
            </div>
            <div class="filter-tabs">
              <span
                :class="['tab', { active: activeFilter === 'all' }]"
                @click="activeFilter = 'all'"
              >
                全部
              </span>
              <span
                :class="['tab', { active: activeFilter === 'unread' }]"
                @click="activeFilter = 'unread'"
              >
                未读({{ unreadCount }})
              </span>
              <span
                :class="['tab', { active: activeFilter === 'new' }]"
                @click="activeFilter = 'new'"
              >
                新招聘
              </span>
              <span
                :class="['tab', { active: activeFilter === 'communicated' }]"
                @click="activeFilter = 'communicated'"
              >
                仅沟通
              </span>
            </div>
            <div class="conversations" v-loading="loadingConversations">
              <div
                v-for="conversation in filteredConversations"
                :key="conversation.id"
                :class="[
                  'conversation-item',
                  { active: selectedConversation?.id === conversation.id },
                ]"
                @click="selectConversation(conversation)"
              >
                <div class="avatar">
                  {{ getConversationAvatar(conversation) }}
                </div>
                <div class="conversation-info">
                  <div class="conversation-header">
                    <span class="name">{{ getConversationName(conversation) }}</span>
                    <span class="time">{{ formatTime(conversation.lastMessageTime) }}</span>
                  </div>
                  <div class="last-message">
                    {{ conversation.lastMessage || '暂无消息' }}
                  </div>
                </div>
              </div>
              <el-empty
                v-if="filteredConversations.length === 0"
                description="暂无会话"
                :image-size="80"
              />
            </div>
          </div>

          <div class="chat-area">
            <div v-if="selectedConversation" class="chat-container">
              <div class="chat-header">
                <div class="user-info">
                  <div class="avatar">{{ getConversationAvatar(selectedConversation) }}</div>
                  <div class="info">
                    <span class="name">{{ getConversationName(selectedConversation) }}</span>
                    <span class="status">在线</span>
                  </div>
                </div>
              </div>

              <div class="chat-messages" ref="messagesContainer" v-loading="loadingMessages">
                <div
                  v-for="message in messages"
                  :key="message.id"
                  :class="['message', { 'message-sent': message.senderId === currentUserId }]"
                >
                  <div class="message-avatar">
                    {{
                      message.senderId === currentUserId
                        ? '我'
                        : getConversationAvatar(selectedConversation)
                    }}
                  </div>
                  <div class="message-content">
                    <div class="message-text">{{ message.content }}</div>
                    <div class="message-time">{{ formatTime(message.createdAt) }}</div>
                  </div>
                </div>
              </div>

              <div class="chat-input">
                <el-input
                  v-model="newMessage"
                  type="textarea"
                  :rows="3"
                  placeholder="输入消息..."
                  @keyup.enter.ctrl="sendMessage"
                />
                <el-button type="primary" @click="sendMessage" :disabled="!newMessage.trim()">
                  发送
                </el-button>
              </div>
            </div>
            <div v-else class="empty-chat">
              <el-empty description="选择一个会话开始聊天" :image-size="120" />
            </div>
          </div>
        </div>
      </div>
    </main>
    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import SeekerNavBar from '@/components/SeekerNavBar.vue'
import AppFooter from '@/components/AppFooter.vue'
import { messageApi, type Conversation, type Message } from '@/api/message'
import { useUserStore } from '@/store/user'

defineOptions({
  name: 'MessagesPage',
})

const userStore = useUserStore()
const currentUserId = computed(() => userStore.userInfo?.id)

const searchKeyword = ref('')
const activeFilter = ref('all')
const selectedConversation = ref<Conversation | null>(null)
const conversations = ref<Conversation[]>([])
const messages = ref<Message[]>([])
const newMessage = ref('')
const messagesContainer = ref<HTMLElement | null>(null)
const loadingConversations = ref(false)
const loadingMessages = ref(false)

const unreadCount = computed(() => {
  return conversations.value.filter(() => {
    return messages.value.some((m) => !m.isRead && m.receiverId === currentUserId.value)
  }).length
})

const filteredConversations = computed(() => {
  let filtered = conversations.value

  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    filtered = filtered.filter((c) => {
      const name = getConversationName(c).toLowerCase()
      return name.includes(keyword)
    })
  }

  if (activeFilter.value === 'unread') {
    filtered = filtered.filter(() => {
      return messages.value.some((m) => !m.isRead && m.receiverId === currentUserId.value)
    })
  }

  return filtered
})

const fetchConversations = async (): Promise<void> => {
  loadingConversations.value = true
  try {
    conversations.value = await messageApi.getConversations()
  } catch (error) {
    console.error('Failed to fetch conversations:', error)
    ElMessage.error('获取会话列表失败')
  } finally {
    loadingConversations.value = false
  }
}

const selectConversation = async (conversation: Conversation): Promise<void> => {
  selectedConversation.value = conversation
  await fetchMessages()
}

const fetchMessages = async (): Promise<void> => {
  if (!selectedConversation.value) return

  loadingMessages.value = true
  try {
    const data = await messageApi.getMessages(selectedConversation.value.id)
    messages.value = data.content
    await nextTick()
    scrollToBottom()
  } catch (error) {
    console.error('Failed to fetch messages:', error)
    ElMessage.error('获取消息失败')
  } finally {
    loadingMessages.value = false
  }
}

const sendMessage = async (): Promise<void> => {
  if (!selectedConversation.value || !newMessage.value.trim()) return

  try {
    const receiverId =
      selectedConversation.value.user1Id === currentUserId.value
        ? selectedConversation.value.user2Id
        : selectedConversation.value.user1Id

    await messageApi.sendMessage({
      receiverId,
      content: newMessage.value.trim(),
    })

    newMessage.value = ''
    await fetchMessages()
    await fetchConversations()
  } catch (error) {
    console.error('Failed to send message:', error)
    ElMessage.error('发送消息失败')
  }
}

const getConversationName = (conversation: Conversation): string => {
  if (conversation.user1Id === currentUserId.value) {
    return conversation.user2?.username || '未知用户'
  }
  return conversation.user1?.username || '未知用户'
}

const getConversationAvatar = (conversation: Conversation): string => {
  const name = getConversationName(conversation)
  return name.charAt(0).toUpperCase()
}

const formatTime = (time?: string): string => {
  if (!time) return ''

  const date = new Date(time)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 60) {
    return `${minutes}分钟前`
  } else if (hours < 24) {
    return `${hours}小时前`
  } else if (days < 7) {
    return `${days}天前`
  } else {
    const month = date.getMonth() + 1
    const day = date.getDate()
    return `${month.toString().padStart(2, '0')}月${day.toString().padStart(2, '0')}日`
  }
}

const scrollToBottom = (): void => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

onMounted(() => {
  fetchConversations()
})
</script>

<style scoped lang="scss">
@use '@/assets/styles/variables.scss' as *;

.messages-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #f0f7f4 0%, #f5f5f5 100%);
}

.main-content {
  padding: 20px 0;
}

.container {
  max-width: 1246px;
  margin: 0 auto;
  padding: 0 24px;
}

.messages-layout {
  display: grid;
  grid-template-columns: 360px 1fr;
  gap: 0;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  height: calc(100vh - 160px);
  min-height: 600px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.conversations-list {
  border-right: 1px solid #f0f0f0;
  display: flex;
  flex-direction: column;
  height: 100%;
  background: linear-gradient(180deg, #fafafa 0%, #ffffff 100%);

  .search-box {
    padding: 16px;
    border-bottom: 1px solid #f0f0f0;

    :deep(.el-input__wrapper) {
      border-radius: 16px;
      background: #f5f5f5;
      box-shadow: none;

      &:hover {
        background: #f0f0f0;
      }

      &.is-focus {
        background: white;
        box-shadow: 0 0 0 2px rgba($primary, 0.2);
      }
    }
  }

  .filter-tabs {
    display: flex;
    padding: 12px 16px;
    gap: 16px;
    border-bottom: 1px solid #f0f0f0;

    .tab {
      font-size: 14px;
      color: #666;
      cursor: pointer;
      transition: all 0.2s ease;
      padding: 4px 8px;
      border-radius: 12px;

      &:hover {
        color: $primary;
        background: rgba($primary, 0.05);
      }

      &.active {
        color: $primary;
        font-weight: 500;
        background: rgba($primary, 0.1);
      }
    }
  }

  .conversations {
    flex: 1;
    overflow-y: auto;

    .conversation-item {
      display: flex;
      gap: 12px;
      padding: 16px;
      cursor: pointer;
      transition: all 0.2s ease;
      border-bottom: 1px solid #f0f0f0;

      &:hover {
        background: rgba($primary, 0.03);
      }

      &.active {
        background: rgba($primary, 0.08);
        border-left: 3px solid $primary;
      }

      .avatar {
        width: 48px;
        height: 48px;
        border-radius: 50%;
        background: linear-gradient(135deg, $primary 0%, $primary-soft 100%);
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        font-weight: 500;
        flex-shrink: 0;
        box-shadow: 0 2px 8px rgba($primary, 0.3);
      }

      .conversation-info {
        flex: 1;
        min-width: 0;

        .conversation-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 4px;

          .name {
            font-size: 15px;
            font-weight: 500;
            color: #222;
          }

          .time {
            font-size: 12px;
            color: #999;
          }
        }

        .last-message {
          font-size: 13px;
          color: #666;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
  }
}

.chat-area {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: linear-gradient(180deg, #ffffff 0%, #fafafa 100%);

  .empty-chat {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .chat-container {
    display: flex;
    flex-direction: column;
    height: 100%;
  }

  .chat-header {
    padding: 16px 24px;
    border-bottom: 1px solid #f0f0f0;
    background: white;

    .user-info {
      display: flex;
      align-items: center;
      gap: 12px;

      .avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: linear-gradient(135deg, $primary 0%, $primary-soft 100%);
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 16px;
        font-weight: 500;
        box-shadow: 0 2px 8px rgba($primary, 0.3);
      }

      .info {
        display: flex;
        flex-direction: column;
        gap: 2px;

        .name {
          font-size: 16px;
          font-weight: 500;
          color: #222;
        }

        .status {
          font-size: 12px;
          color: $primary;
        }
      }
    }
  }

  .chat-messages {
    flex: 1;
    overflow-y: auto;
    padding: 24px;
    display: flex;
    flex-direction: column;
    gap: 16px;
    background: linear-gradient(180deg, #f9f9f9 0%, #ffffff 100%);

    .message {
      display: flex;
      gap: 12px;

      &.message-sent {
        flex-direction: row-reverse;

        .message-content {
          align-items: flex-end;

          .message-text {
            background: linear-gradient(135deg, $primary 0%, $primary-deep 100%);
            color: white;
            border-radius: 12px 12px 4px 12px;
            box-shadow: 0 2px 8px rgba($primary, 0.25);
          }
        }
      }

      .message-avatar {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        background: linear-gradient(135deg, #e0e0e0 0%, #f5f5f5 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
        flex-shrink: 0;
      }

      .message-content {
        display: flex;
        flex-direction: column;
        gap: 4px;
        max-width: 60%;

        .message-text {
          padding: 12px 16px;
          background: white;
          border-radius: 12px 12px 12px 4px;
          font-size: 14px;
          line-height: 1.5;
          color: #222;
          box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
        }

        .message-time {
          font-size: 12px;
          color: #999;
        }
      }
    }
  }

  .chat-input {
    padding: 16px 24px;
    border-top: 1px solid #f0f0f0;
    display: flex;
    gap: 12px;
    align-items: flex-end;
    background: white;

    :deep(.el-textarea__inner) {
      border-radius: 12px;
      background: #f5f5f5;
      border: none;
      resize: none;

      &:hover {
        background: #f0f0f0;
      }

      &:focus {
        background: white;
        box-shadow: 0 0 0 2px rgba($primary, 0.2);
      }
    }

    .el-button {
      height: 40px;
      border-radius: 16px;
      padding: 0 24px;
      background: linear-gradient(135deg, $primary 0%, $primary-deep 100%);
      border: none;
      box-shadow: 0 2px 8px rgba($primary, 0.3);
      transition: all 0.2s ease;

      &:hover {
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba($primary, 0.4);
      }

      &:active {
        transform: translateY(0);
      }
    }
  }
}
</style>
