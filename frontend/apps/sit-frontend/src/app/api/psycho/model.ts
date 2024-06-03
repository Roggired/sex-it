export type CreatePsychoProfileRequest = {
  name: string;
  email: string;
  price: number;
  isFirstFree: boolean;
  bio: string;
};

export type Psycho = CreatePsychoProfileRequest & {
  readonly id: number;
};
