import './client-applications.scss';
import { applicationsApi } from 'apps/sit-frontend/src/app/api/applications/applications-api';
import { SlotStatus } from 'apps/sit-frontend/src/app/api/slot/model';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { SuiInput } from 'apps/sit-frontend/src/app/sui/sui-input/sui-input';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export const ClientApplicationsPage = () => {
  const [currFilter, setCurrFilter] = useState<
    'WAIT' | 'APPROVED' | 'REJECTED'
  >('WAIT');

  const { data } = applicationsApi.useGetAcceptedApplicationsQuery('');

  if (!data) {
    return <></>;
  }

  return (
    <div className="client-applications">
      {/*<div className="client-applications__breadcrumbs">
        <span onClick={() => setCurrFilter('WAIT')}>Ожидают ответа</span>
        <span onClick={() => setCurrFilter('APPROVED')}>/ Принятые</span>
        <span onClick={() => setCurrFilter('REJECTED')}>/ Отклоненные</span>
      </div>*/}
      <div className="client-applications__filter">
        <SuiInput placeholder="Поиск по психологу" />
        <SuiButton>Поиск</SuiButton>
      </div>
      <div className="client-applications__apps">
        {data.map((d) => (
          <ApplicationEntry
            key={d.id}
            id={d.id}
            psycho={d.psycho.name}
            date={d.slot.time}
            price={d.psycho.price}
            status="PLANNED"
          />
        ))}
      </div>
    </div>
  );
};

const ApplicationEntry = ({
  date,
  psycho,
  price,
  id,
  status,
}: {
  readonly id: number;
  readonly psycho: string;
  readonly date: string;
  readonly price?: number;
  readonly status?: SlotStatus;
}) => {
  const navigate = useNavigate();

  return (
    <div className="client-applications__apps__entry">
      <div>
        <b>{psycho}</b>
        <span>Дата: {date}</span>
        <span>Цена: {price ?? 'бесплатно'}</span>
        {status && <span>Статус: {status}</span>}
      </div>
      {status ? (
        <SuiButton onClick={() => navigate(routes.toClientApplication(id))}>
          Открыть
        </SuiButton>
      ) : (
        <SuiButton>Отозвать</SuiButton>
      )}
    </div>
  );
};
