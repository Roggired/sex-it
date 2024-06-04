import './application-view-page.scss';
import { applicationsApi } from 'apps/sit-frontend/src/app/api/applications/applications-api';
import app from 'apps/sit-frontend/src/app/app';
import { useGetNumberPathParam } from 'apps/sit-frontend/src/app/hooks/useGetNumberPathParam';
import { psycho } from 'apps/sit-frontend/src/app/state/user-atom';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';

export const ApplicationViewPage = () => {
  const navigate = useNavigate();
  const appId = useGetNumberPathParam('appId');

  //Нет ендпоинта для получения appпо id, костылю
  const { data } = applicationsApi.useGetApplicationsQuery(psycho.id);

  const [approve] = applicationsApi.useAcceptApplicationMutation();
  const [reject] = applicationsApi.useRejectApplicationMutation();

  if (!appId || !data) {
    return <></>;
  }
  const application = data.filter((d) => d.id === appId)[0];

  return (
    <div className="application-view">
      <h1>Просмотр заявки</h1>
      {/*<b>Бесплатная консультация</b>*/}
      {/*<span>Заявка от анонимного пользователя</span>*/}
      <span>
        Отправлена: {new Date(application.creationTime).toLocaleString()}
      </span>
      <span>{application.description}</span>
      <div className="application-view__btns">
        <SuiButton
          onClick={() =>
            approve(appId).then(() =>
              navigate(routes.toPsychoApplicationsPage())
            )
          }
        >
          Принять
        </SuiButton>
        <SuiButton
          buttonType="secondary"
          onClick={() =>
            reject(appId).then(() =>
              navigate(routes.toPsychoApplicationsPage())
            )
          }
        >
          Отклонить
        </SuiButton>
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
