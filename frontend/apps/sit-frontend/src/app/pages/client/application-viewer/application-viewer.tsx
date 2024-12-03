import './application-viewer.scss';
import { applicationsApi } from 'apps/sit-frontend/src/app/api/applications/applications-api';
import { psychoProfileApi } from 'apps/sit-frontend/src/app/api/psycho/psycho-profile-api';
import { useGetNumberPathParam } from 'apps/sit-frontend/src/app/hooks/useGetNumberPathParam';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { months } from 'apps/sit-frontend/src/app/utils/date-mapper';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';
import {useAtomValue} from "jotai/index";
import {userDataAtom} from "../../../auth/auth-cache";
import {skipToken} from "@reduxjs/toolkit/query";

export const ClientApplicationViewerPage = () => {
  const navigate = useNavigate();
  const appId = useGetNumberPathParam('id');
  const { id } = useAtomValue(userDataAtom)

  // //Нет ендпоинта для получения appпо id, костылю
  // const { data } = applicationsApi.useGetAcceptedApplicationsQuery('');

  const { data: p } = psychoProfileApi.useGetPsychoQuery(id ? {
    id: id,
    mode: 'CLIENT',
  } : skipToken);

  /*if (!appId || !data) {
    return <></>;
  }
  const application = data.filter((d) => d.id === appId)[0];
  console.log(application);
  return (
    <div className="application-viewer">
      <h1>
        {application.slot.dayId + 1} {months[application.slot.monthId]} 2024{' '}
        {application.slot.time}
      </h1>
      <b>{p?.name ?? ''}</b>
      <span>Цена: {p?.price} руб.</span>
      <span>
        Первая консультация бесплатна: {p?.isFirstFree ? 'Da' : 'Net'}
      </span>
      <span>Анонимно: {application.anonType === 'ANON'}</span>
      {/!*<span>Дистанционная консультация</span>*!/}
      <div>
        <SuiButton onClick={() => window.open(application?.link, '_blank')}>
          Подключиться
        </SuiButton>
        <SuiButton
          buttonType="secondary"
          onClick={() => navigate(routes.toBack())}
        >
          Закрыть
        </SuiButton>
      </div>
    </div>
  );*/
};
