import { useAtomValue } from 'jotai'
import { jwtDecode } from 'jwt-decode'
import { roleAtom } from './auth-cache'

export type User = UserInfo & {
  readonly id: string
  readonly email: string
  readonly username: string
}

export interface UserInfo {
  readonly name: string
  readonly surname: string
  readonly patronymic?: string
}

export enum Role {
  STUDENT = 'STUDENT',
  COURSE_AUTHOR = 'COURSE_AUTHOR',
  TEACHER = 'TEACHER',
  HEAD_TEACHER = 'HEAD_TEACHER',
}

type JWTPayload = {
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
      name: '',
      surname: '',
      patronymic: '',
    }
  const decodedToken = jwtDecode(token) as JWTPayload
  return {
    name: decodedToken.given_name,
    surname: decodedToken.family_name,
    patronymic: decodedToken?.patronymic_name,
  }
}

export const useUserRoles = (): {
  readonly roles: Array<Role>
  readonly isTeacher: () => boolean
  readonly isHeadTeacher: () => boolean
} => {
  const roles = useAtomValue(roleAtom)

  return {
    roles,
    isTeacher: () => Role.TEACHER in roles,
    isHeadTeacher: () => roles.includes(Role.HEAD_TEACHER),
  }
}
