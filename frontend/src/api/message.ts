import { request } from '@/utils/request'
import type { User } from '@/types'

export interface Conversation {
  id: number
  user1Id: number
  user2Id: number
  lastMessage?: string
  lastMessageTime?: string
  createdAt: string
  updatedAt: string
  user1?: User
  user2?: User
}

export interface Message {
  id: number
  conversationId: number
  senderId: number
  receiverId: number
  content: string
  type?: string
  isRead: boolean
  createdAt: string
  updatedAt: string
  sender?: User
  receiver?: User
}

export interface MessageListResponse {
  content: Message[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

export const messageApi = {
  getConversations(): Promise<Conversation[]> {
    return request.get('/messages/conversations')
  },

  getMessages(
    conversationId: number,
    params?: { page?: number; size?: number }
  ): Promise<MessageListResponse> {
    return request.get(`/messages/conversations/${conversationId}`, { params })
  },

  sendMessage(data: { receiverId: number; content: string; type?: string }): Promise<Message> {
    return request.post('/messages', data)
  },

  markAsRead(messageId: number): Promise<void> {
    return request.put(`/messages/${messageId}/read`)
  },
}
