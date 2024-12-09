import { Role } from './role'

export enum Realm {
  CLIENT,
  PSYCHO,
  ADMIN,
  PSYCHO_FRIEND
}

export const realmMap: {
  [key in Role]: Array<Realm>
} = {
  [Role.CLIENT]: [Realm.CLIENT],
  [Role.PSYCHO]: [Realm.PSYCHO],
  [Role.ADMIN]: [Realm.ADMIN],
  [Role.PSYCHO_FRIEND]: [Realm.PSYCHO_FRIEND]
}
