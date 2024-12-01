import {getLocalizedSubscriptionName, Subscription} from "../../../api/subscription/model";
import './subscription-state.scss'
import {getRuStringFromDate} from "../../../utils/dates";

export interface SubscriptionStateProps {
  readonly currentSubscription?: Subscription
}

export const SubscriptionState = ({ currentSubscription }: SubscriptionStateProps) => {
  if (!currentSubscription) {
    return (
      <div className="current-subscription">
        <svg width="33" height="32" viewBox="0 0 33 32" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path
            d="M29.8333 16.0001C29.8333 23.3634 23.8633 29.3334 16.5 29.3334C9.13663 29.3334 3.16663 23.3634 3.16663 16.0001C3.16663 8.63675 9.13663 2.66675 16.5 2.66675C23.8633 2.66675 29.8333 8.63675 29.8333 16.0001Z"
            fill="#F44336"/>
          <path d="M20.2707 10.344L22.156 12.2293L12.7293 21.656L10.844 19.7707L20.2707 10.344Z" fill="white"/>
          <path d="M22.156 19.7707L20.2707 21.656L10.844 12.2293L12.7293 10.344L22.156 19.7707Z" fill="white"/>
        </svg>
        <span className="current-subscription__text">
          Нет активной подписки
        </span>
      </div>
    );
  }

  return (
    <div className="current-subscription">
      <svg width="33" height="32" viewBox="0 0 33 32" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path
          d="M15.5 2C7.7805 2 1.5 8.2805 1.5 16C1.5 23.7195 7.7805 30 15.5 30H17C24.7195 30 31 23.7195 31 16C31 13.3005 30.2304 10.7782 28.9014 8.6377L31.5195 6.01953L30.1055 4.60547L27.7119 6.99902C25.1417 3.94509 21.2945 2 17 2H15.5ZM17 4C20.7432 4 24.0906 5.72347 26.293 8.41797L17.1875 17.5234L11.332 11.668L9.91797 13.082L17.1875 20.3516L27.4414 10.0977C28.4314 11.842 29 13.8551 29 16C29 22.617 23.6165 28 17 28C10.3835 28 5 22.617 5 16C5 9.383 10.3835 4 17 4Z"
          fill="#05A400"/>
      </svg>
      <div className="current-subscription__container">
        <span className="current-subscription__text">Подписка: {getLocalizedSubscriptionName(currentSubscription.type)}</span>
        <span className="current-subscription__text">Оплачено до {getRuStringFromDate(new Date(currentSubscription.validUntil))}</span>
      </div>
    </div>
  )
};
