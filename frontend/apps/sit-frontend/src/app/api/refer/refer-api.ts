import { gatewayApi } from 'apps/sit-frontend/src/app/api/store';
import { FilterAvailablePsycho, PsychoProfileForFriendshipView, FriendshipRequest, UpdateReferRequest, NewApplicationRequest, SlotMonthView, ApplicationView, PsychoRefersView} from 'apps/sit-frontend/src/app/api/refer/model';
import { PageView } from '../common';

export const referralProgramApi = gatewayApi.injectEndpoints({
  endpoints: (build) => ({
    // Получение доступных психологов (для друга) с пагинацией
    getAvailablePsycho: build.query<PageView<PsychoProfileForFriendshipView>, {request: FilterAvailablePsycho;  pageNumber: number; pageSize: number }>({
      query: ({ request, pageNumber, pageSize }) => ({
        url: 'referral/available-psycho',
        method: 'POST',
        body: request,
        params: { pageNumber, pageSize }, // передаем параметры пагинации
      }),
      providesTags: ['FriendshipRequests']
    }),

    // Создание запроса на дружбу (друган)
    createFriendship: build.mutation<void, FriendshipRequest>({
      query: (friendshipRequest) => ({
        url: 'referral/create-friend',
        method: 'POST',
        body: friendshipRequest,
      }),
      invalidatesTags: ['FriendshipRequests', 'Friendships']
    }),

    // Получение статуса дружбы для другана
    getFriendFriendship: build.query<PageView<{ id: number; psychoName: string; friendshipStatus: string }>, string | undefined>({
      query: (param) => ({
        url: 'referral/my-psycho',
        method: 'POST',
        body: {
          name: param,
        },
        params: { pageNumber: 0, pageSize: 10000 }
      }),
      providesTags: ['Friendships']
    }),

    // Получение дружбы для психа
    getFriendFriendshipForPsycho: build.query<PageView<PsychoRefersView>, { pageNumber: number, pageSize: number }>({
      query: ({ pageNumber, pageSize }) => ({
        url: `referral/my-friend`,
        params: { pageNumber, pageSize }, // передаем параметры пагинации
        method: 'GET',
      }),
      providesTags: ['FriendsOfPsycho']
    }),

    // Обновление статуса дружбы (псих)
    updateFriendship: build.mutation<void, UpdateReferRequest>({
      query: (request) => ({
        url: 'referral/my-friend',
        method: 'POST',
        body: request,
      }),
      invalidatesTags: ['FriendsOfPsycho']
    }),

    // Создание реферальной программы
    createReferralProgram: build.mutation<number, number>({
      query: (psychoId) => ({
        url: `referral/create-refer?psychoId=${psychoId}`,
        method: 'POST',
      }),
    }),

    // Получение реферальной программы по ID
    getReferralProgram: build.query<SlotMonthView[], { referId: number; yearId?: number; monthId: number }>({
      query: ({ referId, yearId = 2024, monthId }) => ({
        url: `referral/${referId}`,
        method: 'GET',
        params: { referId, yearId, monthId },
      }),
    }),

    // Создание заявки по реферальной программе
    createReferralApplication: build.mutation<ApplicationView, { referId: number; applicationRequest: NewApplicationRequest }>({
      query: ({ referId, applicationRequest }) => ({
        url: `referral/${referId}`,
        method: 'POST',
        body: applicationRequest, // передаем тело запроса для создания заявки
      }),
    }),

    cancelFriendshipRequest: build.mutation<void, number>({
      query: (param) => ({
        url: `referral/my-friend/${param}`,
        method: 'DELETE'
      }),
      invalidatesTags: ['FriendshipRequests', 'Friendships']
    })
  }),
});
