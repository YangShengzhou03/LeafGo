export interface User {
  id: number
  username: string
  email: string
  phone?: string
  avatar?: string
  userType: 'JOB_SEEKER' | 'EMPLOYER'
  status: 'ACTIVE' | 'INACTIVE' | 'BANNED'
  createdAt: string
  updatedAt: string
}

export interface Company {
  id: number
  userId: number
  name: string
  logo?: string
  scale: string
  industry: string
  description?: string
  province?: string
  city?: string
  district?: string
  address?: string
  website?: string
  verified: boolean
  jobCount?: number
  createdAt: string
  updatedAt: string
}

export interface Job {
  id: number
  companyId: number
  company?: Company | string
  title: string
  province?: string
  city?: string
  district?: string
  address?: string
  salary: string
  jobType: string
  experience: string
  education: string
  description: string
  benefits: string | string[]
  status: 'ACTIVE' | 'CLOSED' | 'DRAFT'
  viewCount: number
  tags?: string | string[]
  skills?: string[]
  online?: boolean
  companyName?: string
  industry?: string
  stage?: string
  createdAt: string
  updatedAt: string
}

export interface Resume {
  id: number
  userId: number
  name: string
  phone: string
  email: string
  avatar?: string
  selfIntroduction?: string
  education: Education[] | string
  workExperience: WorkExperience[] | string
  skills: Skill[] | string
  createdAt: string
  updatedAt: string
}

export interface Education {
  id: number
  school: string
  major: string
  degree: string
  startDate: string
  endDate: string
}

export interface WorkExperience {
  id: number
  company: string
  position: string
  startDate: string
  endDate?: string
  description: string
}

export interface Skill {
  id: number
  name: string
  level: string
}

export interface Application {
  id: number
  jobId: number
  job?: Job
  userId: number
  user?: User
  resumeId: number
  resume?: Resume
  coverLetter?: string
  status: 'PENDING' | 'VIEWED' | 'INTERVIEW' | 'REJECTED' | 'ACCEPTED'
  createdAt: string
  updatedAt: string
}

export interface Message {
  id: number
  senderId: number
  sender?: User
  receiverId: number
  receiver?: User
  content: string
  type: 'text' | 'image' | 'file'
  isRead: boolean
  createdAt: string
}

export interface Conversation {
  id: number
  user1Id: number
  user1?: User
  user2Id: number
  user2?: User
  lastMessage?: string
  lastMessageTime?: string
  createdAt: string
  updatedAt: string
}

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export interface PageData<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

export interface JobQuery {
  page?: number
  size?: number
  keyword?: string
  location?: string
  jobType?: string
  salary?: string
  experience?: string
}

export interface Boss {
  id: number
  name: string
  position: string
  hiring: string
}
