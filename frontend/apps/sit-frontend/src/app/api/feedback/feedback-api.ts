import {gatewayApi} from 'apps/sit-frontend/src/app/api/store';
import {FeedbackView, GetLastTenFeedbackByPsychoParams} from "./model";

export const feedbackApi = gatewayApi.injectEndpoints({
  endpoints: (builder) => ({
    getLastTenFeedbacksByPsycho: builder.query<Array<FeedbackView>, GetLastTenFeedbackByPsychoParams>({
      query: (params) => ({
        url: 'feedbacks/last-ten-by-psycho',
        method: 'GET',
        params: {
          psychoId: params.psychoId
        }
      }),
    }),
  })
});
