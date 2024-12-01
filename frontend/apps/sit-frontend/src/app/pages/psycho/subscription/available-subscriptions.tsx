import {AvailableSubscription, getLocalizedSubscriptionName, SubscriptionType} from "../../../api/subscription/model";
import './subscription-card.scss';
import {useState} from "react";
import classNames from "classnames";
import {SuiButton} from "../../../sui/sui-button/sui-button";

export interface SubscriptionCardProps {
  readonly availableSubscriptions: Array<AvailableSubscription>,
  readonly onSelectConfirmed: (arg: SubscriptionType) => void
}

const getDescriptionByType = (type: SubscriptionType) => {
  if (type == 'FREE') {
    return 'Попробуйте все возможности платформы \n' +
      'Sex-IT совершенно бесплатно в течение\n' +
      '1 месяца';
  }

  if (type == 'BASIC') {
    return 'Платформа для организации вашего \n' +
      'рабочего процесса. Включает в себя профиль,\n' +
      'календарь консультаций и пакет \n' +
      'из 20 дистанционных консультаций на базе \n' +
      'возможностей платформы.';
  }

  return 'Платформа для организации вашего \n' +
    'рабочего процесса без ограничений.\n' +
    'Для тех, кто относится к делу серьезно.\n' +
    'Профиль, календарь консультаций, \n' +
    'дистанционные консультации \n' +
    'без ограничений, система “Друган”\n' +
    'и многое другое.';
};

const showConsultsLimit = (type: SubscriptionType) => {
  return type != 'PRO'
}

const getLocalizedPeriodByType = (type: SubscriptionType) => {
  if (type == 'FREE') {
    return '1 месяц';
  }

  return '1 год';
};

const getNumberOfConsults = (type: SubscriptionType) => {
  if (type == 'FREE') {
    return 10;
  }

  return 20;
}

const getLocalizedPriceByType = (type: SubscriptionType) => {
  if (type == 'FREE') {
    return 'Бесплатно';
  }

  if (type == 'BASIC') {
    return '8000 руб.';
  }

  return '16000 руб.';
}

export const AvailableSubscriptions = ({ availableSubscriptions, onSelectConfirmed }: SubscriptionCardProps) => {
  const [selectedType, setSelectedType] = useState<SubscriptionType | null>(null);

  return (
    <div className="available-subscriptions">
      <div className="subscription-card-container">
        {availableSubscriptions.map((sub, i) => (
            <div
              className={classNames("subscription-card", {"subscription-card-selected": sub.type === selectedType})}
              key={i}
              onClick={() => setSelectedType(sub.type)}
            >
              <div className="subscription-card__top-container">
                <h3 className="subscription-card__title">{getLocalizedSubscriptionName(sub.type)}</h3>
                <p className="subscription-card__text">{getDescriptionByType(sub.type)}</p>
              </div>
              <div className="subscription-card__bottom-container">
                <div style={{display: "flex", flexDirection: "column", gap: "4px", flex: "1"}}>
                  <span className="subscription-card__accent-text">Пероиод: {getLocalizedPeriodByType(sub.type)}</span>
                  {showConsultsLimit(sub.type) && (
                    <span
                      className="subscription-card__accent-text">Online консультации: {getNumberOfConsults(sub.type)}</span>
                  )}
                </div>
                <span className="subscription-card__accent-text">{getLocalizedPriceByType(sub.type)}</span>
              </div>
            </div>
          )
        )}
      </div>
      {selectedType && (
        <SuiButton onClick={() => onSelectConfirmed(selectedType)}>
          Оформить
        </SuiButton>
      )}
    </div>
  );
};
