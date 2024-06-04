import './psycho-day-viewer.scss';
import { slotApi } from 'apps/sit-frontend/src/app/api/slot/slot-api';
import { useGetNumberPathParam } from 'apps/sit-frontend/src/app/hooks/useGetNumberPathParam';
import { DayViewerEntry } from 'apps/sit-frontend/src/app/pages/psycho/psycho-slot-viewer/day-viewer-entry';
import { psycho } from 'apps/sit-frontend/src/app/state/user-atom';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { months } from 'apps/sit-frontend/src/app/utils/date-mapper';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';

export const PsychoDayViewer = () => {
  const month = useGetNumberPathParam('month');
  const day = useGetNumberPathParam('day');
  const navigate = useNavigate();

  const { data } = slotApi.useGetSlotsByDayQuery(
    {
      psychoId: psycho.id,
      dayId: (day as number) - 1,
      mode: 'PSYCHO',
      yearId: 2024,
      monthId: month as number,
    },
    {
      skip: !day || !month,
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

      <SuiButton
        buttonType="secondary"
        onClick={() => navigate(routes.toBack())}
      >
        Закрыть
      </SuiButton>
    </div>
  );
};
