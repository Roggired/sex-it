import { configureStore } from '@reduxjs/toolkit';
import { createApi } from '@reduxjs/toolkit/query/react';
import {baseQueryWithTokenRefresh} from "../auth/interceptor";

export const gatewayApi = createApi({
  reducerPath: 'gatewayServiceApi',
  // baseQuery: fetchBaseQuery({ baseUrl: process.env['NX_GATEWAY_URL'] }),
  // baseQuery: fetchBaseQuery({ baseUrl: 'http://localhost:31505/' }),
  baseQuery: baseQueryWithTokenRefresh,
  endpoints: () => ({}),
  tagTypes: ['DaySlots', 'CalSlots', 'PsychoApps', 'subscriptions', 'ClientApps', 'FriendshipRequests', 'FriendsOfPsycho', 'Friendships'],
});

export const store = configureStore({
  reducer: {
    [gatewayApi.reducerPath]: gatewayApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(gatewayApi.middleware),
});
