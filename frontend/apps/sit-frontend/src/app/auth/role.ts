import { useAtomValue } from 'jotai'
import { jwtDecode } from 'jwt-decode'
import { roleAtom } from './auth-cache'

export type User = UserInfo & {
  readonly id: string
  readonly email: string
  readonly username: string
}

export interface UserInfo {
  readonly id: number
  readonly name: string
  readonly surname: string
  readonly patronymic?: string
}

export enum Role {
  CLIENT = 'CLIENT',
  PSYCHO = 'PSYCHO',
  ADMIN = 'ADMIN',
}

type JWTPayload = {
  readonly iat: number
  readonly email: string
  readonly family_name: string
  readonly given_name: string
  readonly patronymic_name?: string
  readonly realm_access: {
    readonly roles: Array<string>
  }
}

export const parseRoleFromJWT = (token?: string | null): Array<Role> => {
  if (!token) return []
  const decodedToken = jwtDecode(token) as JWTPayload
  return decodedToken.realm_access.roles.filter(
    (role) => role in Role
  ) as Array<Role>
}

export const parseUserNameFromJWT = (token?: string | null): UserInfo => {
  if (!token)
    return {
      id: 0,
      name: '',
      surname: '',
      patronymic: '',
    }
  const decodedToken = jwtDecode(token) as JWTPayload
  console.log(decodedToken)
  return {
    id: decodedToken.iat,
    name: decodedToken.given_name,
    surname: decodedToken.family_name,
    patronymic: decodedToken?.patronymic_name,
  }
}

export const useUserRoles = (): {
  readonly roles: Array<Role>
  readonly isPsycho: boolean
  readonly isClient: boolean
} => {
  const roles = useAtomValue(roleAtom)

  return {
    roles,
    isPsycho: roles.includes(Role.PSYCHO),
    isClient: roles.includes(Role.CLIENT),
  }
}
