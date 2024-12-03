export interface FeedbackView {
  readonly id: number
  readonly rating: number
  readonly creationTime: string
  readonly text: string
}

export interface GetLastTenFeedbackByPsychoParams {
  readonly psychoId: number
}
