import {
  AcceptedApplication,
  Application,
  CreateApplicationRequest,
} from 'apps/sit-frontend/src/app/api/applications/model';
import { gatewayApi } from 'apps/sit-frontend/src/app/api/store';

export const applicationsApi = gatewayApi.injectEndpoints({
  endpoints: (builder) => ({
    createApplication: builder.mutation<object, {body: CreateApplicationRequest, referralId: number | undefined}>({
      query: ({body, referralId}) => ({
        url: 'applications',
        method: 'POST',
        body: body,
        params: { referId: referralId }
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
      providesTags: ['ClientApps']
    }),

    acceptApplication: builder.mutation<void, { appId: number, address: string }>({
      query: ({appId, address}) => ({
        url: `applications/${appId}/accept`,
        method: 'POST',
        body: {
          address
        }
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

    revokeApp: builder.mutation<void, number>({
      query: (appId) => ({
        url: `applications/${appId}/revoke`,
        method: 'POST',
      }),
      invalidatesTags: ['ClientApps'],
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
