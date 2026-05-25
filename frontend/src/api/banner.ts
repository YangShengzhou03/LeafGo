import { request } from '@/utils/request'

export interface Banner {
  id: number
  title: string
  description: string
  imageUrl: string
  linkUrl?: string
  sortOrder: number
  active: boolean
}

export const bannerApi = {
  getBanners: (): Promise<Banner[]> => {
    return request.get<Banner[]>('/banners')
  },
}
