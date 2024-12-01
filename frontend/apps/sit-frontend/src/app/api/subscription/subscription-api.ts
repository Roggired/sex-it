import {gatewayApi} from "../store";
import {AvailableSubscription, CreateSubscriptionRequest, CurrentSubscriptionResponse, Subscription} from "./model";

export const subscriptionApi = gatewayApi.injectEndpoints({
  endpoints: (builder) => ({
    createSubscription: builder.mutation<Subscription, CreateSubscriptionRequest>({
      query: (body) => ({
        url: 'subscriptions',
        method: 'POST',
        body,
      }),
      invalidatesTags: ['subscriptions'],
    }),

    getAvailableSubscriptions: builder.query<Array<AvailableSubscription>, void>({
      query: () => ({
        url: 'subscriptions/available',
      }),
      providesTags: ['subscriptions'],
    }),

    getCurrentSubscription: builder.query<CurrentSubscriptionResponse, void>({
      query: () => ({
        url: 'subscriptions/current',
      }),
      providesTags: ['subscriptions'],
    })
  })
});
