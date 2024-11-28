import type {
  BaseQueryFn,
  FetchArgs,
  FetchBaseQueryError,
} from '@reduxjs/toolkit/query'
import { fetchBaseQuery } from '@reduxjs/toolkit/query'
import { Mutex } from 'async-mutex'
import {Authorization, clearTokens, getCachedAuthorizationIfExists, saveAuthorization} from "./auth-cache";

const mutex = new Mutex()
// const GATEWAY_URL = process.env['NX_GATEWAY_URL']
const GATEWAY_URL = 'http://localhost:31505/api/v1'

const baseQuery = fetchBaseQuery({ baseUrl: GATEWAY_URL })

const baseQueryWithAccessToken = fetchBaseQuery({
  baseUrl: GATEWAY_URL,
  prepareHeaders: (headers) => {
    const accessToken = getCachedAuthorizationIfExists()?.accessToken

    if (accessToken) {
      headers.set('Authorization', `Bearer ${accessToken}`)
    }

    return headers
  },
})

export const baseQueryWithTokenRefresh: BaseQueryFn<
  string | FetchArgs,
  unknown,
  FetchBaseQueryError
> = async (args, api, extraOptions) => {
  await mutex.waitForUnlock()

  let result = await baseQueryWithAccessToken(args, api, extraOptions)
  const refreshToken = getCachedAuthorizationIfExists()?.refreshToken

  if (
    result.error &&
    (result.error.status === 401 || result.error.status === 'FETCH_ERROR')
  ) {
    if (!mutex.isLocked()) {
      const release = await mutex.acquire()
      try {
        const refreshResult = await baseQuery(
          {
            url: '/api/v1/sso/refresh-access',
            method: 'POST',
            body: {
              refreshToken,
            },
          },
          api,
          extraOptions
        )

        if (refreshResult.data) {
          saveAuthorization(refreshResult.data as Authorization)
          result = await baseQueryWithAccessToken(args, api, extraOptions)
        } else {
          clearTokens()
          window.location.href = '/'
        }
      } finally {
        release()
      }
    } else {
      await mutex.waitForUnlock()
      result = await baseQueryWithAccessToken(args, api, extraOptions)
    }
  }
  return result
}
