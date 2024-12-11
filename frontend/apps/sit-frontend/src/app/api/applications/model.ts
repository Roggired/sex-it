import {
  AnonType,
  SlotStatus,
  VisitType,
} from 'apps/sit-frontend/src/app/api/slot/model';

export type Application = {
  id: number;
  slot: {
    id: number;
    time: string;
    yearId: number;
    monthId: number;
    dayId: number;
  };
  creationTime: string;
  anonType: AnonType;
  visitType: VisitType;
  status: SlotStatus;
  description?: string;
  link?: string;
  address?: string;
  clientName?: string;
  notes?: string;
  results?: string;
  friendName?: string;
};

export type CreateApplicationRequest = {
  slotId: number;
  anonType: AnonType;
  visitType: VisitType;
  description: string;
};

export type AcceptedApplication = Application & {
  psycho: {
    id: number;
    name: string;
    price: number;
  };
};
