import './application-view-page.scss';
import { applicationsApi } from 'apps/sit-frontend/src/app/api/applications/applications-api';
import { psychoProfileApi } from 'apps/sit-frontend/src/app/api/psycho/psycho-profile-api';
import app from 'apps/sit-frontend/src/app/app';
import { useGetNumberPathParam } from 'apps/sit-frontend/src/app/hooks/useGetNumberPathParam';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';
import {useAtomValue} from "jotai/index";
import {userDataAtom} from "../../../auth/auth-cache";

export const ApplicationViewPage = () => {
  const navigate = useNavigate();
  const appId = useGetNumberPathParam('appId');
  const { id } = useAtomValue(userDataAtom)

  //Нет ендпоинта для получения appпо id, костылю
  const { data } = applicationsApi.useGetApplicationsQuery(id);

  const [approve] = applicationsApi.useAcceptApplicationMutation();
  const [reject] = applicationsApi.useRejectApplicationMutation();

  const { data: p } = psychoProfileApi.useGetPsychoQuery({
    id: id,
    mode: 'CLIENT',
  });

  if (!appId || !data) {
    return <></>;
  }
  const application = data.filter((d) => d.id === appId)[0];

  return (
    <div className="application-view">
      <h1>Просмотр заявки</h1>
      <b>
        {p?.isFirstFree ? 'Бесплатная консультация' : 'Платная консультация'}
      </b>
      <span>
        {application.anonType === 'ANON'
          ? 'Заявка от анонимного пользователя'
          : `Заявка от ${application.clientName}`}
      </span>
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
