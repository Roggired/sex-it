import './psycho-day-viewer.scss';
import { useGetNumberPathParam } from 'apps/sit-frontend/src/app/hooks/useGetNumberPathParam';
import { DayViewerEntry } from 'apps/sit-frontend/src/app/pages/psycho/psycho-slot-viewer/day-viewer-entry';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { months } from 'apps/sit-frontend/src/app/utils/date-mapper';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';

export const PsychoDayViewer = () => {
  const month = useGetNumberPathParam('month');
  const day = useGetNumberPathParam('day');
  const navigate = useNavigate();

  if (!month || !day) {
    return <></>;
  }

  return (
    <div className="psycho-day-viewer">
      <h1>
        {day} {months[month]} 2024
      </h1>
      <span>Слоты:</span>
      <DayViewerEntry
        time="11;22"
        isAnon
        isOffline={false}
        status="PLANNED"
        desc="lorem20"
      />
      <SuiButton
        buttonType="secondary"
        onClick={() => navigate(routes.toBack())}
      >
        Закрыть
      </SuiButton>
    </div>
  );
};
