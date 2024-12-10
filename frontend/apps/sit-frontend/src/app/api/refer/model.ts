export interface FriendshipRequest {
  psychoId: number;
}

export interface PsychoProfileForCatalogueView {
  id: number;
  name: string;
  price: number;
  rating?: number;
}

export interface SlotMonthView {
  slotId: number;
  slotName: string;
  availableSlots: number;
}

export interface NewApplicationRequest {
  date: string;
  time: string;
  comment: string;
}

export interface ApplicationView {
  applicationId: number;
  status: string;
  dateCreated: string;
}

export interface UpdateReferRequest {
  psychoId: number;
  status: string;
}

export interface FilterAvailablePsycho {
  name?: string
}

export interface PsychoProfileForFriendshipView {
    id: number
    name: string
}

export interface PsychoRefersView {
  id: number
  name: string
  percent: number
  status: string
  }
