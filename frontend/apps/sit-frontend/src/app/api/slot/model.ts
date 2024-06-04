import { ApiMode } from 'apps/sit-frontend/src/app/api/psycho/model';

export type SlotStatus = 'EMPTY' | 'DONE' | 'PLANNED' | 'NEED_REVIEW';

export type VisitType = 'ONLINE' | 'OFFLINE';
export type AnonType = 'ANON' | 'NE_ANON';

export type SlotView = {
  id: number;
  time: string;
  status: SlotStatus;
  dayId: number;
};

export type Slot = SlotView & {
  anonType: AnonType;
  visitType: VisitType;
  status: SlotStatus;
  description?: string;
  link?: string;
  address?: string;
};

export type CreateSlotRequest = {
  time: string;
  yearId: number;
  monthId: number;
  dayId: number;
};

export type GetSlotsByMonth = {
  yearId: number;
  monthId: number;
  mode: ApiMode;
  psychoId: number;
};

export type GetSlotByDay = GetSlotsByMonth & {
  dayId: number;
};
