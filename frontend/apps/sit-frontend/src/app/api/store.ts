import { configureStore } from '@reduxjs/toolkit';
import { fetchBaseQuery } from '@reduxjs/toolkit/query';
import { createApi } from '@reduxjs/toolkit/query/react';

const BASE_URL = 'http://172.28.0.6:8080/api';

export const gatewayApi = createApi({
  reducerPath: 'gatewayServiceApi',
  baseQuery: fetchBaseQuery({ baseUrl: BASE_URL }),
  endpoints: () => ({}),
  tagTypes: [],
});

export const store = configureStore({
  reducer: {
    [gatewayApi.reducerPath]: gatewayApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(gatewayApi.middleware),
});
