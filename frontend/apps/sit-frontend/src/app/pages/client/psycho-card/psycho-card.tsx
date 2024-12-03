import './psycho-card.scss';
import { psychoProfileApi } from 'apps/sit-frontend/src/app/api/psycho/psycho-profile-api';
import { useGetNumberPathParam } from 'apps/sit-frontend/src/app/hooks/useGetNumberPathParam';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';
import Alla from '../../../../assets/img.png';
import {skipToken} from "@reduxjs/toolkit/query";
import {RatingWidget} from "../../shared/rating-widget/rating-widget";

export const PsychoCardPage = () => {
  const id = useGetNumberPathParam('id');
  const navigate = useNavigate();

  const { data } = psychoProfileApi.useGetPsychoQuery( id ? {
    id: id,
    mode: 'CLIENT',
  } : skipToken);

  if (!id || !data) {
    return <></>;
  }

  return (
    <div className="psycho-card">
      <div className="psycho-card__header">
        <img src={Alla} />
        <div>
          <div className="psycho-card__entry">
            <b>ФИО</b>
            <span>{data.name}</span>
          </div>
          <div className="psycho-card__entry">
            <b>Email</b>
            <span>{data.email}</span>
          </div>
          <div className="psycho-card__entry">
            <b>Цена консультации (руб. в час):</b>
            <span>{data.price}</span>
          </div>
          <span>
            Бесплатная 1-ая консультация: {data.isFirstFree ? 'да' : 'нет'}
          </span>
        </div>
        <div>
          <SuiButton
            onClick={() => navigate(routes.toClientPsychoCalendar(id))}
          >
            Открыть календарь
          </SuiButton>
          <SuiButton
            onClick={() => navigate(routes.toBack())}
            buttonType="secondary"
          >
            Назад
          </SuiButton>
        </div>
      </div>
      <div className="psycho-card__entry psycho-card__entry-horizontal">
        <b>Рейтинг:</b>
        <RatingWidget rating={data.rating}/>
      </div>
      <div className="psycho-card__entry">
        <b>О себе:</b>
        <span>{data.bio}</span>
      </div>
      <b>Анонимные отзывы:</b>
      <div className="psycho-card__entry">
        <span>02.05.2024</span>
        <span>Алла Сергеевна, Вы просто класс! Спасибо Вам огромное! ))</span>
      </div>
    </div>
  );
};
