export type CreateFriendProfileRequest = {
  name: string;
  email: string;
  percent: number;
};

export type Friend = CreateFriendProfileRequest & {
  readonly id: number;
};


export interface FriendshipProjectionByPsycho {
  friendName: string;
  friendPercent: number;
}
