import './application-view-page.scss';
import { useGetNumberPathParam } from 'apps/sit-frontend/src/app/hooks/useGetNumberPathParam';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';

export const ApplicationViewPage = () => {
  const navigate = useNavigate();
  const appId = useGetNumberPathParam('appId');

  if (!appId) {
    return <></>;
  }

  return (
    <div className="application-view">
      <h1>Просмотр заявки</h1>
      <b>Бесплатная консультация</b>
      <span>Заявка от анонимного пользователя</span>
      <span>Отправлена: 07.05.2024 19:00</span>
      <span>Я испытываю такие-то проблемы</span>
      <div className="application-view__btns">
        <SuiButton>Принять</SuiButton>
        <SuiButton buttonType="secondary">Отклонить</SuiButton>
        <SuiButton
          buttonType="secondary"
          onClick={() => navigate(routes.toBack())}
        >
          Закрыть
        </SuiButton>
      </div>
    </div>
  );
};
