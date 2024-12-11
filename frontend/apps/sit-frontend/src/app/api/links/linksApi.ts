import {gatewayApi} from "../store";
import {PageView} from "../common";

export type LinkView = {
  "id": number,
  "name": string
}

export const linksApi = gatewayApi.injectEndpoints({
  endpoints: (builder) => ({


    getMyLinks: builder.query<PageView<LinkView>, void>({
      query: () => ({
        url: 'referral/my-refer',
        params: {
          pageNumber: 0,
          pageSize: 10000
        },
      }),
      providesTags: ['Links']
    }),

    getMyLinksPaid: builder.query<PageView<LinkView>, void>({
      query: () => ({
        url: 'referral/my-refer/paid',
        params: {
          pageNumber: 0,
          pageSize: 10000
        },
      }),
      providesTags: ['Links']
    }),

    getMyLinksAccepted: builder.query<PageView<LinkView>, void>({
      query: () => ({
        url: 'referral/my-refer/accepted',
        params: {
          pageNumber: 0,
          pageSize: 10000
        },
      }),
      providesTags: ['Links']
    }),

    payByLink: builder.mutation<void, number>({
      query: (id) => ({
        url: `referral/my-refer/${id}`,
        method: 'POST'
      }),
      invalidatesTags: ['Links']
    }),

  }),
});
