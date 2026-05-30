<template>
  <div class="employer-messages">
    <div class="messages-container">
      <div class="conversations-list">
        <div class="list-header">
          <h3>消息列表</h3>
        </div>
        <div class="conversations">
          <div
            v-for="conv in conversations"
            :key="conv.id"
            class="conversation-item"
            :class="{ active: activeConversation?.id === conv.id }"
            @click="selectConversation(conv)"
          >
            <el-avatar :size="40">
              {{ conv.user1?.username?.charAt(0) || conv.user2?.username?.charAt(0) || '?' }}
            </el-avatar>
            <div class="conv-info">
              <div class="conv-name">
                {{
                  conv.user1Id === userStore.userInfo?.id
                    ? conv.user2?.username
                    : conv.user1?.username
                }}
              </div>
              <div class="conv-last-msg">{{ conv.lastMessage }}</div>
            </div>
            <div class="conv-time">{{ formatTime(conv.lastMessageTime) }}</div>
          </div>
          <el-empty v-if="conversations.length === 0" description="暂无消息" />
        </div>
      </div>

      <div class="chat-area">
        <div class="chat-header" v-if="activeConversation">
          <h3>
            {{
              activeConversation.user1Id === userStore.userInfo?.id
                ? activeConversation.user2?.username
                : activeConversation.user1?.username
            }}
          </h3>
        </div>
        <div class="chat-placeholder" v-else>
          <el-icon size="48" color="#dcdfe6"><ChatDotRound /></el-icon>
          <p>选择一个会话开始聊天</p>
        </div>

        <div class="chat-messages" ref="messagesContainer" v-if="activeConversation">
          <div
            v-for="msg in messages"
            :key="msg.id"
            class="message-item"
            :class="{ 'is-mine': msg.senderId === userStore.userInfo?.id }"
          >
            <el-avatar :size="36">
              {{ msg.sender?.username?.charAt(0) || '?' }}
            </el-avatar>
            <div class="message-content">
              <div class="message-text">{{ msg.content }}</div>
              <div class="message-time">{{ formatTime(msg.createdAt) }}</div>
            </div>
          </div>
          <div v-if="messages.length === 0" class="empty-messages">
            <el-empty description="暂无消息记录" />
          </div>
        </div>

        <div class="chat-input" v-if="activeConversation">
          <el-input v-model="messageInput" placeholder="输入消息..." @keyup.enter="sendMessage">
            <template #append>
              <el-button type="primary" @click="sendMessage">发送</el-button>
            </template>
          </el-input>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { ChatDotRound } from '@element-plus/icons-vue'
import { messageApi } from '@/api'
import { useUserStore } from '@/store/user'
import type { Conversation, Message } from '@/types'
import dayjs from 'dayjs'

defineOptions({
  name: 'EmployerMessages',
})

const userStore = useUserStore()
const messagesContainer = ref<HTMLElement>()
const conversations = ref<Conversation[]>([])
const activeConversation = ref<Conversation | null>(null)
const messages = ref<Message[]>([])
const messageInput = ref('')

const fetchConversations = async (): Promise<void> => {
  try {
    const data = await messageApi.getConversations()
    conversations.value = data
  } catch {
    conversations.value = []
  }
}

const selectConversation = async (conv: Conversation): Promise<void> => {
  activeConversation.value = conv
  try {
    const data = await messageApi.getMessages(conv.id, { size: 50 })
    messages.value = data.content.reverse()
    scrollToBottom()
  } catch {
    messages.value = []
  }
}

const sendMessage = async (): Promise<void> => {
  if (!messageInput.value.trim() || !activeConversation.value) return
  try {
    const receiverId =
      activeConversation.value.user1Id === userStore.userInfo?.id
        ? activeConversation.value.user2Id
        : activeConversation.value.user1Id
    const msg = await messageApi.sendMessage({
      receiverId,
      content: messageInput.value,
    })
    messages.value.push(msg)
    messageInput.value = ''
    scrollToBottom()
    fetchConversations()
  } catch {
    // 发送失败
  }
}

const scrollToBottom = (): void => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

const formatTime = (time?: string): string => {
  if (!time) return ''
  return dayjs(time).format('MM-DD HH:mm')
}

onMounted(() => {
  fetchConversations()
})
</script>

<style scoped lang="scss">
.employer-messages {
  height: calc(100vh - 140px);

  .messages-container {
    display: flex;
    height: 100%;
    background: #fff;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .conversations-list {
      width: 300px;
      border-right: 1px solid #e4e7ed;
      display: flex;
      flex-direction: column;

      .list-header {
        padding: 16px;
        border-bottom: 1px solid #e4e7ed;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
        }
      }

      .conversations {
        flex: 1;
        overflow-y: auto;

        .conversation-item {
          display: flex;
          align-items: center;
          padding: 12px 16px;
          cursor: pointer;
          transition: background 0.3s;

          &:hover {
            background: #f5f7fa;
          }

          &.active {
            background: #ecf5ff;
          }

          .conv-info {
            flex: 1;
            margin-left: 12px;
            overflow: hidden;

            .conv-name {
              font-size: 14px;
              font-weight: 500;
              color: #303133;
              margin-bottom: 4px;
            }

            .conv-last-msg {
              font-size: 12px;
              color: #909399;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
          }

          .conv-time {
            font-size: 12px;
            color: #c0c4cc;
            margin-left: 8px;
          }
        }
      }
    }

    .chat-area {
      flex: 1;
      display: flex;
      flex-direction: column;

      .chat-header {
        padding: 16px 20px;
        border-bottom: 1px solid #e4e7ed;
        background: #fafafa;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
        }
      }

      .chat-placeholder {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        p {
          margin-top: 12px;
          color: #909399;
          font-size: 14px;
        }
      }

      .chat-messages {
        flex: 1;
        overflow-y: auto;
        padding: 20px;
        display: flex;
        flex-direction: column;

        .empty-messages {
          flex: 1;
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .message-item {
          display: flex;
          align-items: flex-start;
          margin-bottom: 16px;

          .message-content {
            margin-left: 12px;
            max-width: 60%;

            .message-text {
              background: #f0f2f5;
              padding: 10px 14px;
              border-radius: 12px 12px 12px 0;
              font-size: 14px;
              color: #303133;
              line-height: 1.5;
            }

            .message-time {
              font-size: 12px;
              color: #c0c4cc;
              margin-top: 4px;
            }
          }

          &.is-mine {
            flex-direction: row-reverse;

            .message-content {
              margin-left: 0;
              margin-right: 12px;
              display: flex;
              flex-direction: column;
              align-items: flex-end;

              .message-text {
                background: #00bebd;
                color: #fff;
                border-radius: 12px 12px 0 12px;
              }

              .message-time {
                text-align: right;
              }
            }
          }
        }
      }

      .chat-input {
        padding: 16px 20px;
        border-top: 1px solid #e4e7ed;
        background: #fafafa;
      }
    }
  }
}
</style>
