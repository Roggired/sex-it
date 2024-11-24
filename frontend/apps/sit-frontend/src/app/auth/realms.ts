import { Role } from './role'

export enum Realm {
  COURSE_LISTENER,
  COURSE_VIEWER,
  COURSE_EDITOR,
  PROGRAM_EDITOR,
  GROUP_EDITOR,
  INSTRUCTOR,
}

export const realmMap: {
  [key in Role]: Array<Realm>
} = {
  [Role.STUDENT]: [Realm.COURSE_LISTENER],
  [Role.COURSE_AUTHOR]: [Realm.COURSE_VIEWER, Realm.COURSE_EDITOR],
  [Role.HEAD_TEACHER]: [
    Realm.COURSE_VIEWER,
    Realm.PROGRAM_EDITOR,
    Realm.GROUP_EDITOR,
    Realm.INSTRUCTOR,
  ],
  [Role.TEACHER]: [Realm.COURSE_VIEWER, Realm.INSTRUCTOR],
}
