import {
  CreateSlotRequest,
  GetSlotByDay,
  GetSlotsByMonth,
  Slot,
  SlotView,
} from 'apps/sit-frontend/src/app/api/slot/model';
import { gatewayApi } from 'apps/sit-frontend/src/app/api/store';

export const slotApi = gatewayApi.injectEndpoints({
  endpoints: (builder) => ({
    createSlot: builder.mutation<object, CreateSlotRequest>({
      query: (body) => ({
        url: 'slots',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['CalSlots'],
    }),

    getSlotsByMonths: builder.query<Array<SlotView>, GetSlotsByMonth>({
      query: (params) => ({
        url: 'slots/by-month',
        params,
      }),
      providesTags: ['CalSlots'],
    }),

    getSlotsByDay: builder.query<Array<Slot>, GetSlotByDay>({
      query: (params) => ({
        url: 'slots/by-day',
        params,
      }),
      providesTags: ['DaySlots'],
    }),

    removeSlot: builder.mutation<void, number>({
      query: (slotId) => ({
        url: `slots/${slotId}`,
        method: 'DELETE',
      }),
      invalidatesTags: ['DaySlots'],
    }),
  }),
});
