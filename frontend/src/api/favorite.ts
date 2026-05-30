import { request } from '@/utils/request'
import type { Job } from '@/types'

export interface FavoriteStatus {
  favorited: boolean
  jobId: number
}

export interface Favorite {
  id: number
  userId: number
  jobId: number
  job?: Job
  createdAt: string
}

export const favoriteApi = {
  addFavorite(jobId: number): Promise<FavoriteStatus> {
    return request.post(`/favorites/${jobId}`)
  },

  removeFavorite(jobId: number): Promise<FavoriteStatus> {
    return request.delete(`/favorites/${jobId}`)
  },

  getFavoriteStatus(jobId: number): Promise<FavoriteStatus> {
    return request.get(`/favorites/${jobId}/status`)
  },

  getMyFavorites(): Promise<Favorite[]> {
    return request.get('/favorites')
  },
}
