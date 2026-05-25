import { request } from '@/utils/request'

export const configApi = {
  getHotKeywords: (): Promise<string[]> => {
    return request.get<string[]>('/config/hot-keywords')
  },
}
