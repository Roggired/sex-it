import {Page} from "../../shared/page/page";
import './subscription-page.scss'
import {subscriptionApi} from "../../../api/subscription/subscription-api";
import {SubscriptionState} from "./subscription-state";
import {SuiLoader} from "../../../sui/sui-loader/sui-loder";
import {AvailableSubscriptions} from "./available-subscriptions";
import {AvailableSubscription, SubscriptionType} from "../../../api/subscription/model";
import {SuiModal} from "../../../sui/modal/sui-modal";
import {useState} from "react";
import Magic from "../../../../assets/magic.png";

export const SubscriptionPage = () => {
  const {data: currentSubscriptionResponse} = subscriptionApi.useGetCurrentSubscriptionQuery();
  const {data: availableSubscriptions} = subscriptionApi.useGetAvailableSubscriptionsQuery();
  const [mutate] = subscriptionApi.useCreateSubscriptionMutation()
  const [selectedSubscription, setSelectedSubscription] = useState<SubscriptionType | null>(null);

  if (!currentSubscriptionResponse || ! availableSubscriptions) {
    return (
      <Page center={true}>
        <div className="subscription-page__content">
          <SuiLoader/>
        </div>
      </Page>
    )
  }

  const onSelect = (selectedCard: SubscriptionType) => {
    setSelectedSubscription(selectedCard);
  }

  const onSelectConfirmed = () => {
    if (selectedSubscription) {
      mutate({ type: selectedSubscription });
    }
  }

  return (
    <Page>
      <div className="subscription-page__content">
        <div className="subscription-page__content__container">
          <h2>Активная подписка</h2>
          <SubscriptionState currentSubscription={currentSubscriptionResponse.current}/>
        </div>
        <div className="subscription-page__content__container">
          <h2>Подписки</h2>
          <AvailableSubscriptions availableSubscriptions={availableSubscriptions} onSelectConfirmed={onSelect}/>
        </div>
      </div>
      <SuiModal handleClose={onSelectConfirmed} isOpen={selectedSubscription !== null}>
        <h2>Уведомление</h2>
        <p>Так как это все-таки не реальная система, а курсовая
          работа - у нас нет интеграции с платежной
          системой. Поэтому давайте представим, что тут
          “произошла магия”.</p>
        <Magic/>
      </SuiModal>
    </Page>
  );
};
