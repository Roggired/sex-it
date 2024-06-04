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
