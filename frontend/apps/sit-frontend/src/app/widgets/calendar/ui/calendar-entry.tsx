import classNames from 'classnames';
import { MonthEntry } from '../calendar-container';
import { CalendarSlot } from './calendar-slot';

export type CalendarEntryProps = {
  readonly monthEntry: MonthEntry;
  readonly isCurrentDay: boolean;
  readonly onDayClick?: (day: number, month: number) => void;
  readonly onSlotClick?: (slotId: number) => void;
};

export const CalendarEntry = ({
  monthEntry: { month, day, isCurrentMonth },
  isCurrentDay,
  onDayClick,
  onSlotClick,
}: CalendarEntryProps) => {
  return (
    <div
      onClick={() => onDayClick?.(day, month)}
      className={classNames('calendar__entry', {
        inactive: !isCurrentMonth,
      })}
    >
      <div className="calendar__entry__date">
        <span>{isCurrentDay && 'Сегодня'}</span>
        <span>{day}</span>
      </div>
      <CalendarSlot onSlotClick={() => onSlotClick?.(1)} />
      <CalendarSlot onSlotClick={() => onSlotClick?.(1)} />
      <div className="calendar__entry__more">Еще 2 слота</div>
    </div>
  );
};
