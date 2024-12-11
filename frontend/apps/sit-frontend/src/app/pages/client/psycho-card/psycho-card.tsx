import './psycho-card.scss';
import { psychoProfileApi } from 'apps/sit-frontend/src/app/api/psycho/psycho-profile-api';
import { useGetNumberPathParam } from 'apps/sit-frontend/src/app/hooks/useGetNumberPathParam';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';
import Alla from '../../../../assets/img.png';
import {skipToken} from "@reduxjs/toolkit/query";
import {RatingWidget} from "../../shared/rating-widget/rating-widget";
import {feedbackApi} from "../../../api/feedback/feedback-api";
import {SuiLoader} from "../../../sui/sui-loader/sui-loder";
import {getRuStringFromDate} from "../../../utils/dates";
import {Page} from "../../shared/page/page";
import { useEffect } from 'react';
import { useSetAtom } from 'jotai';
import { referralIdAtom } from '../../../features/referral/referral-application';

export const PsychoCardPage = () => {
  const [params, _] = useSearchParams();
  const setReferralId = useSetAtom(referralIdAtom)

  const id = useGetNumberPathParam('id');
  const navigate = useNavigate();

  const { data } = psychoProfileApi.useGetPsychoQuery( id ? {
    id: id,
    mode: 'CLIENT',
  } : skipToken);

  const { data: feedbacks } = feedbackApi.useGetLastTenFeedbacksByPsychoQuery(id ? {
    psychoId: id
  } : skipToken)

  useEffect(() => {
    const referralId = params.get('referralId');
    if (referralId) {
      setReferralId(Number(referralId))
    }
  }, []);

  if (!id || !data || !feedbacks) {
    return <Page center={true}><SuiLoader/></Page>;
  }

  return (
    <Page>
      <div className="psycho-card">
        <div className="psycho-card__header">
          <img src={Alla}/>
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
        {feedbacks.map(feedback => (
          <div className="psycho-card__entry" key={feedback.id}>
            <div className="psycho-card__entry-horizontal">
              <span>{getRuStringFromDate(new Date(feedback.creationTime))}</span>
              <RatingWidget rating={feedback.rating}/>
            </div>
            <span>{feedback.text}</span>
          </div>
        ))}
        {feedbacks.length <= 0 && (
          <span>Пока отзывов нет</span>
        )}
      </div>
    </Page>
  );
};
