import './client-applications.scss';
import { applicationsApi } from 'apps/sit-frontend/src/app/api/applications/applications-api';
import { SlotStatus } from 'apps/sit-frontend/src/app/api/slot/model';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { SuiInput } from 'apps/sit-frontend/src/app/sui/sui-input/sui-input';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {Modal} from "../../../sui/modal/sui-modal";
import {ApplicationViewPage} from "../../psycho/application-view/application-view-page";
import {ClientAppView} from "../../psycho/application-view/client-app-view";
import {AcceptedApplication} from "../../../api/applications/model";
import {ClientFeed} from "../../psycho/feedback/client-feed";

export const ClientApplicationsPage = () => {

  const [search, setSearch] = useState('')
  const [currFilter, setCurrFilter] = useState<
    'NEED_REVIEW' | 'PLANNED' | 'REJECTED'
  >('NEED_REVIEW');

  const { data, refetch } = applicationsApi.useGetAcceptedApplicationsQuery({
    psychoName: search,
    appStatus: currFilter,
  });

  return (
    <div className="client-applications">
      <div className="client-applications__breadcrumbs">
        <span className={currFilter === 'NEED_REVIEW' ? 'chosen' : ''} onClick={() => setCurrFilter('NEED_REVIEW')}>Ожидают ответа</span>
        <span className={currFilter === 'PLANNED' ? 'chosen' : ''} onClick={() => setCurrFilter('PLANNED')}>/ Принятые</span>
        <span className={currFilter === 'REJECTED' ? 'chosen' : ''} onClick={() => setCurrFilter('REJECTED')}>/ Отклоненные</span>
      </div>
      <div className="client-applications__filter">
        <SuiInput value={search} onChange={e => setSearch(e.target.value)} placeholder="Поиск по психологу" />
        <SuiButton onClick={() => refetch()}>Поиск</SuiButton>
      </div>
      <div className="client-applications__apps">
        {data?.length ?
          data?.map((d) => (
            <ApplicationEntry
              key={d.id}
              id={d.id}
              psycho={d.psycho.name}
              time={d.slot.time}
              year={d.slot.yearId}
              month={d.slot.monthId}
              day={d.slot.dayId}
              price={d.psycho.price}
              status={d.status}
              aapp={d}
            />
          )) : <>Нет заявок</>}
      </div>
    </div>
  );
};

export const mapStatus = (status: SlotStatus): string => {
  if (status === 'NEED_REVIEW') {
    return 'Ожидает ответа'
  } else if (status === 'REJECTED') {
    return 'Отклонено'
  } else if (status === 'EMPTY') {
    return ''
  } else if (status === 'DONE') {
    return 'Проведено'
  } else {
    return 'Запланировано'
  }

}
const ApplicationEntry = ({
  time,
  year,
  month,
  day,
  psycho,
  price,
  id,
  aapp,
  status,
}: {
  readonly id: number;
  readonly psycho: string;
  readonly time: string;
  readonly year: number;
  readonly month: number;
  readonly day: number;
  readonly price?: number;
  readonly status: SlotStatus;
  aapp: AcceptedApplication,
}) => {

  const [isOpened, setIsOpened] = useState(false)
  const [isOpened2, setIsOpened2] = useState(false)

  return (
    <div className="client-applications__apps__entry">
      <div>
        <b>{psycho}</b>
        <span>Дата: {day < 10 ? '0' + day : day}.{month}.{year} {time}</span>
        <span>Цена: {price ?? 'бесплатно'}</span>
        {status && <span>Статус: {mapStatus(status)}</span>}
      </div>
      {
        status === 'PLANNED' && <>
          <SuiButton onClick={() => setIsOpened(true)}>
            Открыть
          </SuiButton>
          <Modal isOpen={isOpened} onClose={() => setIsOpened(false)}>
            <ClientAppView appId={id} aapp={aapp} />
          </Modal>
        </>
      }
      {
        status === 'DONE' && <>
          <SuiButton onClick={()=> setIsOpened2(true)}>Оценить</SuiButton>
          <Modal isOpen={isOpened2} onClose={() => setIsOpened2(false)}>
            <ClientFeed appId={id} aapp={aapp} close={() => setIsOpened2(false)} />
          </Modal>
        </>
      }
    </div>
  );
};
