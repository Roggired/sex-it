import './application-view-page.scss';
import {applicationsApi} from 'apps/sit-frontend/src/app/api/applications/applications-api';
import {psychoProfileApi} from 'apps/sit-frontend/src/app/api/psycho/psycho-profile-api';
import {SuiButton} from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import {useNavigate} from 'react-router-dom';
import {useGetPsychoProfile} from "../../../hooks/useGetPsychoProfile";
import {skipToken} from "@reduxjs/toolkit/query";
import {useEffect, useState} from "react";
import {Hearts} from "react-loader-spinner";

export const ApplicationViewPage = ({appId, close}: { readonly appId: number, close: () => void }) => {
  const navigate = useNavigate();
  const {id} = useGetPsychoProfile()

  //Нет ендпоинта для получения appпо id, костылю
  const {data} = applicationsApi.useGetApplicationsQuery(id ?? skipToken);

  const [approve, {isError, isLoading}] = applicationsApi.useAcceptApplicationMutation();
  const [reject] = applicationsApi.useRejectApplicationMutation();

  const [place, setPlace] = useState('')

  const {data: p} = psychoProfileApi.useGetPsychoQuery({
    id: id as number,
    mode: 'CLIENT',
  }, {
    skip: !id,
    refetchOnMountOrArgChange: true
  });

  useEffect(() => {
    if (isError) {
      alert("Подключите подписку для проведения онлайн консультаций")
    }
  }, [isError]);

  if (!appId || !id) {
    return <></>;
  }

  const application = data?.filter((d) => d.id === appId)?.[0];

  if (!application) {
    return
  }

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
      {application.friendName && <span>Заявка от другана: {application.friendName}</span>}
      <span>
        Отправлена: {new Date(application.creationTime).toLocaleString()}
      </span>
      <span>{application.description}</span>

      {application.visitType === 'OFFLINE' && <div style={{display: 'flex', flexDirection: 'column', gap: '16px'}}>
        <span>Обязательно введите место встречи:</span>
        <input value={place} onChange={(e) => setPlace(e.target.value)}/>
      </div>}

      <div className="application-view__btns">
        <SuiButton
          disabled={isLoading}
          onClick={() => {
            if (application.visitType === 'OFFLINE' && !place) {
              alert("Введите место встречи")
            } else {
              approve({appId, address: place}).then(() =>
                close()
              )
            }
          }
          }
        >
          {isLoading ? <Hearts
            height="16"
            width="32"
            color="#FFFFFF"
          /> : 'Принять'}
        </SuiButton>
        <SuiButton
          buttonType="secondary"
          onClick={() =>
            reject(appId).then(close
            )
          }
        >
          Отклонить
        </SuiButton>
        {/*<SuiButton
          buttonType="secondary"
          onClick={() => navigate(routes.toBack())}
        >
          Закрыть
        </SuiButton>*/}
      </div>
    </div>
  );
};
