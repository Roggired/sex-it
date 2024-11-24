import {Authorization} from "../auth/auth-cache";
import {gatewayApi} from "./store";

export interface TokenExchangeRequest {
  authorizationCode: string
  state: string | null
  redirectUri: string
}

export interface RefreshAccessRequest {
  refreshToken: string
}

export const authService = gatewayApi.injectEndpoints({
  endpoints: (builder) => ({
    tokenExchange: builder.mutation<Authorization, TokenExchangeRequest>({
      query: (params: TokenExchangeRequest) => ({
        url: "sso/token",
        method: 'POST',
        body: params,
      }),
    }),
    logout: builder.mutation({
      query: () => ({
        url: 'sso/logout',
        method: 'GET',
      }),
    }),
    refreshAccess: builder.mutation<Authorization, RefreshAccessRequest>({
      query: (params: RefreshAccessRequest) => ({
        url: 'sso/refresh-access',
        method: 'POST',
        body: params,
      }),
    }),
  }),
  overrideExisting: true,
})
