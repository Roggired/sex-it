import { atomWithLazy } from 'jotai/utils'
import { createStore } from 'jotai/vanilla'
import { Realm, realmMap } from './realms'
import {parseRoleFromJWT, parseUserNameFromJWT, Role, UserInfo} from './role'

export interface Authorization {
  readonly accessToken: string
  readonly refreshToken: string
  readonly expiresIn?: number
}

const authStore = createStore()
export const roleAtom = atomWithLazy<Array<Role>>(() => {
  return parseRoleFromJWT(localStorage.getItem(ACCESS_TOKEN_KEY))
})
export const userDataAtom = atomWithLazy<UserInfo>(() => {
  return parseUserNameFromJWT(localStorage.getItem(ACCESS_TOKEN_KEY))
})

const ACCESS_TOKEN_KEY = 'auth:access'
const REFRESH_TOKEN_KEY = 'auth:refresh'

export const saveAuthorization = (authorization: Authorization) => {
  localStorage.setItem(ACCESS_TOKEN_KEY, authorization.accessToken)
  localStorage.setItem(REFRESH_TOKEN_KEY, authorization.refreshToken)
  authStore.set(roleAtom, parseRoleFromJWT(authorization.accessToken))
  authStore.set(userDataAtom, parseUserNameFromJWT(authorization.accessToken))
}

export const getCachedAuthorizationIfExists = (): Authorization | undefined => {
  const tokens = getTokens()
  return tokens
    ? { accessToken: tokens.access, refreshToken: tokens.refresh }
    : undefined
}

export const isAuthorized = (): boolean =>
  getCachedAuthorizationIfExists() !== undefined

export const hasRealm = (realm: Realm): boolean => {
  const roles = authStore.get(roleAtom)
  if (!roles.length) return false
  return roles.map((r) => realmMap[r].includes(realm)).some((r) => r)
}

export const clearTokens = () => {
  localStorage.removeItem(ACCESS_TOKEN_KEY)
  localStorage.removeItem(REFRESH_TOKEN_KEY)
}

const getTokens = (): { access: string; refresh: string } | undefined => {
  const access = localStorage.getItem('auth:access')
  const refresh = localStorage.getItem('auth:refresh')

  if (access && refresh) {
    return {
      access,
      refresh,
    }
  } else {
    return undefined
  }
}
