import {applicationsApi} from "../../../api/applications/applications-api";
import {skipToken} from "@reduxjs/toolkit/query";
import {months} from "../../../utils/date-mapper";
import {AcceptedApplication} from "../../../api/applications/model";
import {SuiButton} from "../../../sui/sui-button/sui-button";

export const ClientAppView = ({appId, aapp}: {appId: number, aapp: AcceptedApplication}) => {
  const {data} = applicationsApi.useGetAppByIdQuery(appId ?? skipToken);

  if (!data) {
    return
  }

  return <div className="application-view">
    <h1>
      {data.slot.dayId + 1} {months[data.slot.monthId]} 2024
    </h1>
    <b>{aapp.psycho.name}</b>
    <span>Цена: {aapp.psycho.price} руб.</span>
    <span>Анонимно: {aapp.anonType === 'ANON' ? 'Да' : 'Нет'}</span>
    <span>Дистанционно: {aapp.visitType === 'ONLINE' ? 'Да' : 'Нет'}</span>
    {
      aapp.link && <SuiButton><a style={{cursor: 'pointer', color: 'white'}} href={aapp.link}>
        Подключиться
      </a></SuiButton>
    }
  </div>;
}
