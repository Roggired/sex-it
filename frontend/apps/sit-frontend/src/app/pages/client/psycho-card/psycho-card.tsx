import './psycho-card.scss';
import { useGetNumberPathParam } from 'apps/sit-frontend/src/app/hooks/useGetNumberPathParam';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';
import Alla from '../../../../assets/img.png';

export const PsychoCardPage = () => {
  const id = useGetNumberPathParam('id');
  const navigate = useNavigate();

  if (!id) {
    return <></>;
  }

  return (
    <div className="psycho-card">
      <div className="psycho-card__header">
        <img src={Alla} />
        <div>
          <div className="psycho-card__entry">
            <b>ФИО</b>
            <span>Алла Сергеевна</span>
          </div>
          <div className="psycho-card__entry">
            <b>Email</b>
            <span>alla.sergeevna@yandex.ru</span>
          </div>
          <div className="psycho-card__entry">
            <b>Цена консультации (руб. в час):</b>
            <span>2000</span>
          </div>
          <span>Бесплатная 1-ая консультация: да</span>
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
      <div className="psycho-card__entry">
        <b>Рейтинг</b>
        <span>4.4</span>
      </div>
      <div className="psycho-card__entry">
        <b>О себе</b>
        <span>Я излечу вашу неуверенность в себе!</span>
      </div>
      <b>Анонимные отзывы</b>
      <div className="psycho-card__entry">
        <span>02.05.2024</span>
        <span>Алла Сергеевна, Вы просто класс! Спасибо Вам огромное! ))</span>
      </div>
    </div>
  );
};
