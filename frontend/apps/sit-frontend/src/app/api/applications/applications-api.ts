import {
  AcceptedApplication,
  Application,
  CreateApplicationRequest,
} from 'apps/sit-frontend/src/app/api/applications/model';
import { gatewayApi } from 'apps/sit-frontend/src/app/api/store';

export const applicationsApi = gatewayApi.injectEndpoints({
  endpoints: (builder) => ({
    createApplication: builder.mutation<object, CreateApplicationRequest>({
      query: (body) => ({
        url: 'applications',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['CalSlots'],
    }),

    getApplications: builder.query<Array<Application>, number>({
      query: (psychoId) => ({
        url: 'applications',
        params: {
          psychoId,
        },
      }),
      providesTags: ['PsychoApps'],
    }),

    getAcceptedApplications: builder.query<Array<AcceptedApplication>, {
      psychoName: string
      appStatus: string
    }>({
      query: (psychoName) => ({
        url: 'applications/accepted',
        params: {
          ...psychoName,
        },
      }),
    }),

    acceptApplication: builder.mutation<void, number>({
      query: (appId) => ({
        url: `applications/${appId}/accept`,
        method: 'POST',
      }),
      invalidatesTags: ['PsychoApps', 'CalSlots', 'DaySlots'],
    }),

    rejectApplication: builder.mutation<void, number>({
      query: (appId) => ({
        url: `applications/${appId}/reject`,
        method: 'POST',
      }),
      invalidatesTags: ['PsychoApps', 'CalSlots', 'DaySlots'],
    }),


    getAppById: builder.query<Application, number>({
      query: (id) => ({
        url: `applications/${id}`,
      }),
      providesTags: ['PsychoApps']
    }),


    patchNote: builder.mutation<void, {
      note: string
      appId: number
    }>({
      query: ({appId, note}) => ({
        url: `applications/${appId}/note`,
        method: 'PATCH',
        body: {
          note
        }
      }),
      invalidatesTags: ['PsychoApps', 'CalSlots', 'DaySlots'],
    }),

    finishApplication: builder.mutation<void, {
      note: string
      appId: number
    }>({
      query: ({appId, note}) => ({
        url: `applications/${appId}/finish`,
        method: 'POST',
        body: {
          note
        }
      }),
      invalidatesTags: ['PsychoApps', 'CalSlots', 'DaySlots'],
    }),

  }),
});
