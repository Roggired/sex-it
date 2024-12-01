export type SubscriptionType = 'FREE' | 'BASIC' | 'PRO';

export const getLocalizedSubscriptionName = (type: SubscriptionType) => {
  if (type == 'FREE') {
    return 'Пробная';
  }

  if (type == 'BASIC') {
    return 'Базовая';
  }

  return 'Premium';
}

export interface Subscription {
  readonly id: number,
  readonly type: SubscriptionType,
  readonly psychoId: string,
  readonly validUntil: string,
  readonly paidAt: string,
  readonly suspended: boolean,
  readonly isExpired: boolean,
}

export interface CurrentSubscriptionResponse {
  readonly current: Subscription
}

export interface CreateSubscriptionRequest {
  readonly type: SubscriptionType
}

export interface AvailableSubscription {
  readonly type: SubscriptionType,
  readonly price: number
}
