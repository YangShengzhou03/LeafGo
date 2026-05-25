<template>
  <div class="seeker-messages">
    <div class="container">
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
                {{ conv.user2?.username?.charAt(0) }}
              </el-avatar>
              <div class="conv-info">
                <div class="conv-name">{{ conv.user2?.username }}</div>
                <div class="conv-last-msg">{{ conv.lastMessage }}</div>
              </div>
              <div class="conv-time">{{ formatTime(conv.lastMessageTime) }}</div>
            </div>
          </div>
        </div>

        <div class="chat-area">
          <div class="chat-header" v-if="activeConversation">
            <h3>{{ activeConversation.user2?.username }}</h3>
          </div>

          <div class="chat-messages" ref="messagesContainer">
            <div
              v-for="msg in messages"
              :key="msg.id"
              class="message-item"
              :class="{ 'is-mine': msg.senderId === userStore.userInfo?.id }"
            >
              <el-avatar :size="36">
                {{ msg.sender?.username?.charAt(0) }}
              </el-avatar>
              <div class="message-content">
                <div class="message-text">{{ msg.content }}</div>
                <div class="message-time">{{ formatTime(msg.createdAt) }}</div>
              </div>
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
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: 'SeekerMessages',
})
import { ref, onMounted, nextTick } from 'vue'
import { messageApi } from '@/api'
import { useUserStore } from '@/store/user'
import type { Conversation, Message } from '@/types'
import dayjs from 'dayjs'

const userStore = useUserStore()
const messagesContainer = ref<HTMLElement>()
const conversations = ref<Conversation[]>([])
const activeConversation = ref<Conversation | null>(null)
const messages = ref<Message[]>([])
const messageInput = ref('')

const fetchConversations = async (): Promise<void> => {
  const data = await messageApi.getConversations()
  conversations.value = data
}

const selectConversation = async (conv: Conversation): Promise<void> => {
  activeConversation.value = conv
  const data = await messageApi.getMessages(conv.id, { size: 50 })
  messages.value = data.content.reverse()
  scrollToBottom()
}

const sendMessage = async (): Promise<void> => {
  if (!messageInput.value.trim() || !activeConversation.value) return

  const msg = await messageApi.sendMessage({
    receiverId: activeConversation.value.user2Id,
    content: messageInput.value,
  })

  messages.value.push(msg)
  messageInput.value = ''
  scrollToBottom()
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
.seeker-messages {
  .messages-container {
    display: flex;
    height: calc(100vh - 200px);
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
        padding: 16px;
        border-bottom: 1px solid #e4e7ed;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
        }
      }

      .chat-messages {
        flex: 1;
        padding: 16px;
        overflow-y: auto;

        .message-item {
          display: flex;
          margin-bottom: 16px;

          &.is-mine {
            flex-direction: row-reverse;

            .message-content {
              align-items: flex-end;
            }
          }

          .message-content {
            margin-left: 12px;
            display: flex;
            flex-direction: column;

            .message-text {
              background: #f0f2f5;
              padding: 10px 14px;
              border-radius: 8px;
              max-width: 400px;
              word-wrap: break-word;
            }

            .message-time {
              font-size: 12px;
              color: #c0c4cc;
              margin-top: 4px;
            }
          }

          &.is-mine .message-content .message-text {
            background: #00bebd;
            color: #fff;
          }
        }
      }

      .chat-input {
        padding: 16px;
        border-top: 1px solid #e4e7ed;
      }
    }
  }
}
</style>
