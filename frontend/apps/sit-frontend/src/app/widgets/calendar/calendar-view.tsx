import './calendar.scss';
import { months } from 'apps/sit-frontend/src/app/utils/date-mapper';
import { CalendarEntry } from './ui/calendar-entry';
import { MonthEntry } from './calendar-container';

const days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

type CalendarViewProps = {
  readonly currentMonth: number;
  readonly currentDay: number;
  readonly monthEntries: Array<MonthEntry>;
  readonly onNextMonthClick: () => void;
  readonly onPrevMonthClick: () => void;
  readonly onDayClick?: (day: number, month: number) => void;
};

export const CalendarView = ({
  currentMonth,
  currentDay,
  monthEntries,
  onNextMonthClick,
  onPrevMonthClick,
  onDayClick,
}: CalendarViewProps) => {
  return (
    <div className="calendar">
      <div className="calendar__header">
        <button onClick={onPrevMonthClick}>PREV</button>
        {months[currentMonth]}
        <button onClick={onNextMonthClick}>NEXT</button>
      </div>
      <div className="calendar__body">
        {days.map((day) => (
          <span key={day}>{day}</span>
        ))}
        {monthEntries.map((monthEntry) => {
          return (
            <CalendarEntry
              onDayClick={onDayClick}
              key={`${monthEntry.month}-${monthEntry.day}`}
              monthEntry={monthEntry}
              isCurrentDay={
                monthEntry.day === currentDay &&
                monthEntry.month === currentMonth
              }
            />
          );
        })}
      </div>
    </div>
  );
};
