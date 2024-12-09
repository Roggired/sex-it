import { gatewayApi } from 'apps/sit-frontend/src/app/api/store';
import { PsychoProfileForCatalogueView, FriendshipRequest, UpdateReferRequest, NewApplicationRequest, SlotMonthView, ApplicationView } from 'apps/sit-frontend/src/app/api/refer/model';
import { PageView } from '../common';

export const referralProgramApi = gatewayApi.injectEndpoints({
  endpoints: (build) => ({
    // Получение доступных психологов (для друга) с пагинацией
    getAvailablePsycho: build.query<PageView<PsychoProfileForCatalogueView>, { pageNumber: number; pageSize: number }>({
      query: ({ pageNumber, pageSize }) => ({
        url: 'referral/available-psycho',
        params: { pageNumber, pageSize }, // передаем параметры пагинации
      }),
    }),

    // Создание запроса на дружбу (друган)
    createFriendship: build.mutation<void, FriendshipRequest>({
      query: (friendshipRequest) => ({
        url: 'referral/create-friend',
        method: 'POST',
        body: friendshipRequest,
      }),
    }),

    // Получение статуса дружбы для другана
    getFriendFriendship: build.query<{ psychoName: string; friendshipStatus: string }, void>({
      query: () => ({
        url: 'referral/my-psycho',
        method: 'GET',
      }),
    }),

    // Получение статуса дружбы для психа по ID
    getFriendFriendshipForPsycho: build.query<{ friendName: string; percent: number }, { psychoId: number }>({
      query: ({ psychoId }) => ({
        url: `referral/my-friend/${psychoId}`,
        method: 'GET',
      }),
    }),

    // Обновление статуса дружбы (псих)
    updateFriendship: build.mutation<void, UpdateReferRequest>({
      query: (request) => ({
        url: 'referral/my-friend',
        method: 'POST',
        body: request,
      }),
    }),

    // Создание реферальной программы
    createReferralProgram: build.query<string, void>({
      query: () => ({
        url: 'referral/create-refer',
        method: 'GET',
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
  }),
});
