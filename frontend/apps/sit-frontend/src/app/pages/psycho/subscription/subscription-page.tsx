import {Page} from "../../shared/page/page";
import './subscription-page.scss'
import {subscriptionApi} from "../../../api/subscription/subscription-api";
import {SubscriptionState} from "./subscription-state";
import {SuiLoader} from "../../../sui/sui-loader/sui-loder";
import {AvailableSubscriptions} from "./available-subscriptions";
import {AvailableSubscription, SubscriptionType} from "../../../api/subscription/model";
import {useState} from "react";
import Magic from "../../../../assets/magic.png";
import {Modal} from "../../../sui/modal/sui-modal";
import {SuiButton} from "../../../sui/sui-button/sui-button";

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
    mutate({ type: selectedCard });
  }

  const onModalClose = () => {
    setSelectedSubscription(null);
  }

  return (
    <Page>
      <div className="subscription-page__content">
        <div className="subscription-page__content__container">
          <h2>Активная подписка</h2>
          <SubscriptionState currentSubscription={currentSubscriptionResponse.current} usageStats={currentSubscriptionResponse.usageStats}/>
        </div>
        <div className="subscription-page__content__container">
          <h2>Подписки</h2>
          <AvailableSubscriptions availableSubscriptions={availableSubscriptions} onSelectConfirmed={onSelect}/>
        </div>
      </div>
      <Modal onClose={onModalClose} isOpen={selectedSubscription !== null} centered={true}>
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
          <h2 style={{ margin: 0 }}>Уведомление</h2>
          <p style={{ marginTop: "20px" }}>Так как это все-таки не реальная система, а курсовая
            работа - у нас нет интеграции с платежной
            системой. Поэтому давайте представим, что тут
            “произошла магия”.</p>
          <img src={Magic}/>
          <SuiButton style={{ marginTop: "20px" }} onClick={onModalClose}>
            Понятно
          </SuiButton>
        </div>
      </Modal>
    </Page>
  );
};
