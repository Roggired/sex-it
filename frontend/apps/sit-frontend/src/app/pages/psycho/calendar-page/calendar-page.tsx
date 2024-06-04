import { psycho } from 'apps/sit-frontend/src/app/state/user-atom';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';
import { CalendarContainer } from '../../../widgets/calendar/calendar-container';
import { Page } from '../../shared/page/page';
import './calendar-page.scss';
import { SlotPageChooser } from '../../shared/slot-page-chooser/slot-page-chooser';

export const CalendarPage = () => {
  const navigate = useNavigate();

  return (
    <Page>
      <div className="calendar-page">
        <SlotPageChooser />
        <CalendarContainer
          psychoId={psycho.id}
          slotMode="PSYCHO"
          onDayClick={(day, month) =>
            navigate(routes.toPsychoDayViewer(month, day))
          }
        />
        <SuiButton
          onClick={() => navigate(routes.toPsychoCreateSlot())}
          className="calendar-page__fab"
        >
          Новый слот
        </SuiButton>
      </div>
    </Page>
  );
};
