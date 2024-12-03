import './psycho-calendar.scss';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { CalendarContainer } from 'apps/sit-frontend/src/app/widgets/calendar/calendar-container';
import { useNavigate } from 'react-router-dom';
import {useGetNumberPathParam} from "../../../hooks/useGetNumberPathParam";

export const PsychoCalendar = () => {
  const navigate = useNavigate();
  const psychoId = useGetNumberPathParam('psychoId')

  if (!psychoId) {
    return
  }

  return (
    <div className="psycho-calendar">
      <CalendarContainer
        psychoId={psychoId}
        slotMode="CLIENT"
        onSlotClick={(day, month) => {}
        }
      />
    </div>
  );
};
