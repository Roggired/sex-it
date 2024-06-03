import { CalendarContainer } from '../../../widgets/calendar/calendar-container';
import { Page } from '../../shared/page/page';
import './calendar-page.scss';
import { SlotPageChooser } from '../../shared/slot-page-chooser/slot-page-chooser';

export const CalendarPage = () => {
  return (
    <Page>
      <div className="calendar-page">
        <SlotPageChooser />
        <CalendarContainer />
      </div>
    </Page>
  );
};
