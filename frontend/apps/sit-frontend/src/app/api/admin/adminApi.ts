import {gatewayApi} from "../store";

export type SubStats = {
  "totalPsychos": number,
  "freeSubscriptions": number,
  "basicSubscriptions": number,
  "proSubscriptions": number
}

export type ConsultStats = {
  numberOfOnlinePerformed: number
}

export const adminApi = gatewayApi.injectEndpoints({
  endpoints: builder => ({

    getSubStats: builder.query<SubStats, void>({
      query: () => ({
        url: `stats/count-subscription-stats`,
        method: 'POST'
      }),
    }),

    getConsultStats: builder.mutation<ConsultStats, {
      from: string
      to: string
    }>({
      query: (body) => ({
        url: `stats/count-consults-stats`,
        method: 'POST',
        body,
      }),
    }),
  })
})
