import { request } from '@/utils/request'
import type { Company, Job, Message, Conversation, PageData } from '@/types'

export const companyApi = {
  getCompanyList(params: {
    page?: number
    size?: number
    industry?: string
    keyword?: string
  }): Promise<PageData<Company>> {
    return request.get('/companies', { params })
  },

  getMyCompany(): Promise<Company | null> {
    return request.get('/companies/my')
  },

  getCompanyDetail(companyId: number): Promise<Company> {
    return request.get(`/companies/${companyId}`)
  },

  updateCompany(companyId: number, data: Partial<Company>): Promise<Company> {
    return request.put(`/companies/${companyId}`, data)
  },

  createCompany(data: Partial<Company>): Promise<Company> {
    return request.post('/companies', data)
  },

  getCompanyJobs(
    companyId: number,
    params: { page?: number; size?: number }
  ): Promise<PageData<Job>> {
    return request.get(`/companies/${companyId}/jobs`, { params })
  },
}

export const messageApi = {
  getConversations(): Promise<Conversation[]> {
    return request.get('/messages/conversations')
  },

  getMessages(
    conversationId: number,
    params: { page?: number; size?: number }
  ): Promise<PageData<Message>> {
    return request.get(`/messages/conversations/${conversationId}`, { params })
  },

  sendMessage(data: { receiverId: number; content: string; type?: string }): Promise<Message> {
    return request.post('/messages', data)
  },

  markAsRead(messageId: number): Promise<void> {
    return request.put(`/messages/${messageId}/read`)
  },
}
