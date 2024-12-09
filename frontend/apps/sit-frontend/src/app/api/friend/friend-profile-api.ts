import { gatewayApi } from 'apps/sit-frontend/src/app/api/store';
import { CreateFriendProfileRequest } from 'apps/sit-frontend/src/app/api/friend/model';
import { Friend } from 'apps/sit-frontend/src/app/api/friend/model';


export const friendProfileApi = gatewayApi.injectEndpoints({
  endpoints: (build) => ({
    // Эндпоинт для создания друга
    createFriend: build.mutation<Friend, { body: CreateFriendProfileRequest }>({
      query: ({ body }) => ({
        url: `friend/profile`,
        method: 'POST',
        body,
      }),
    }),
  }),
});
