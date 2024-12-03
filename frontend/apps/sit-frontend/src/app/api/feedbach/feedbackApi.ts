import {gatewayApi} from "../store";

export const feedA = gatewayApi.injectEndpoints({
  endpoints: (build) => ({
    giveFeedback: build.mutation<void, {
      appId: number
      rating: number
      text: string
    }>({
      query: ({appId, ...body}) => ({
        url: `applications/${appId}/give-feedback`,
        method: 'POST',
        body
      }),
    }),
  })
})
