import './psycho-day-viewer.scss';
import {slotApi} from 'apps/sit-frontend/src/app/api/slot/slot-api';
import {DayViewerEntry} from 'apps/sit-frontend/src/app/pages/psycho/psycho-slot-viewer/day-viewer-entry';
import {SuiButton} from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import {months} from 'apps/sit-frontend/src/app/utils/date-mapper';
import {routes} from 'apps/sit-frontend/src/app/utils/routes';
import {useNavigate} from 'react-router-dom';
import {useGetPsychoProfile} from "../../../hooks/useGetPsychoProfile";
import {useGetNumberPathParam} from "../../../hooks/useGetNumberPathParam";

export const PsychoDayViewer = ({day, month}: {
  month: number
  day: number
}) => {
  const {id} = useGetPsychoProfile()

  const {data} = slotApi.useGetSlotsByDayQuery(
    {
      psychoId: id as number,
      dayId: (day as number) - 1,
      mode: 'PSYCHO',
      yearId: 2024,
      monthId: month as number,
    },
    {
      refetchOnMountOrArgChange: true,
      skip: !day || !month || !id,
    }
  );

  const [deleteSlot] = slotApi.useRemoveSlotMutation();

  if (!month || !day) {
    return <></>;
  }

  return (
    <div className="psycho-day-viewer">
      <h1>
        {day} {months[month]} 2024
      </h1>
      <span>Слоты:</span>
      {(data ?? []).map((d) => (
        <DayViewerEntry
          key={d.id}
          appId={d.applicationId}
          time={d.time}
          isAnon={d.anonType === 'ANON'}
          isOffline={d.visitType === 'OFFLINE'}
          status={d.status}
          desc={d.description}
          link={d.link}
          address={d.address}
          onDelete={() => deleteSlot(d.id)}
        />
      ))}

     {/* <SuiButton
        buttonType="secondary"
        onClick={() => navigate(routes.toBack())}
      >
        Закрыть
      </SuiButton>*/}
    </div>
  );
};
