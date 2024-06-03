import './psycho-calendar.scss';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { CalendarContainer } from 'apps/sit-frontend/src/app/widgets/calendar/calendar-container';
import { useNavigate } from 'react-router-dom';

export const PsychoCalendar = () => {
  const navigate = useNavigate();

  return (
    <div className="psycho-calendar">
      <CalendarContainer
        onSlotClick={(slotId) => navigate(routes.toClientPsychoSlot(slotId))}
      />
    </div>
  );
};
