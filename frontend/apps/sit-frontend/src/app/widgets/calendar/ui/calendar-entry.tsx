import classNames from 'classnames';
import { MonthEntry } from '../calendar-container';
import { CalendarSlot } from './calendar-slot';

export type CalendarEntryProps = {
  readonly monthEntry: MonthEntry;
  readonly isCurrentDay: boolean;
};

export const CalendarEntry = ({
  monthEntry: { day, isCurrentMonth },
  isCurrentDay,
}: CalendarEntryProps) => {
  return (
    <div
      className={classNames('calendar__entry', {
        inactive: !isCurrentMonth,
      })}
    >
      <div className="calendar__entry__date">
        <span>{isCurrentDay && 'Сегодня'}</span>
        <span>{day}</span>
      </div>
      <CalendarSlot />
      <CalendarSlot />
      <div className="calendar__entry__more">Еще 2 слота</div>
    </div>
  );
};
