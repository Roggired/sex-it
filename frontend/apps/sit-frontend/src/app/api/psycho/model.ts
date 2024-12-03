export type CreatePsychoProfileRequest = {
  name: string;
  email: string;
  price: number;
  isFirstFree: boolean;
  bio: string;
  rating?: number;
  feedbacks?: Array<string>;
};

export type Psycho = CreatePsychoProfileRequest & {
  readonly id: number;
};

export type ApiMode = 'CLIENT' | 'PSYCHO';

export interface GetPsychoListFilters {
  readonly name?: string
  readonly priceFrom?: number
  readonly priceTo?: number
  readonly minRating?: number
}

export interface GetPsychoListRequest {
  readonly filters: GetPsychoListFilters
}

export interface GetPsychoListParams {
  readonly body: GetPsychoListRequest
  readonly pageNumber: number
  readonly pageSize: number
}

export interface PsychoCatalogueView {
  readonly id: number
  readonly name: string
  readonly price: number
  readonly rating?: number
}
