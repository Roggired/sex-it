import './psycho-calendar.scss';
import { psycho } from 'apps/sit-frontend/src/app/state/user-atom';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { CalendarContainer } from 'apps/sit-frontend/src/app/widgets/calendar/calendar-container';
import { useNavigate } from 'react-router-dom';

export const PsychoCalendar = () => {
  const navigate = useNavigate();

  return (
    <div className="psycho-calendar">
      <CalendarContainer
        psychoId={1}
        slotMode="CLIENT"
        onSlotClick={(day, month) =>
          navigate(routes.toClientPsychoSlot(month, day))
        }
      />
    </div>
  );
};
