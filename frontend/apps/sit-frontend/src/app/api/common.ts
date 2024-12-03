export interface PageView<T> {
  readonly totalElements: number
  readonly totalPages: number
  readonly content: Array<T>
}
