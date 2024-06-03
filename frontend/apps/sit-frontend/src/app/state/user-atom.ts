import { atomWithStorage } from 'jotai/utils';

type User = {
  readonly id: number;
  readonly type: 'CLIENT' | 'PSYCHO';
};

export const psycho: User = {
  id: 1,
  type: 'PSYCHO',
};

export const client: User = {
  id: 2,
  type: 'CLIENT',
};

export const userAtom = atomWithStorage('user', {} as User);
