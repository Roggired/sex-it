import './calendar.scss';
import { CalendarEntry } from './ui/calendar-entry';
import { MonthEntry } from './calendar-container';

const months = [
  'Jan',
  'Feb',
  'Mar',
  'Apr',
  'May',
  'Jun',
  'Jul',
  'Aug',
  'Sep',
  'Oct',
  'Nov',
  'Dec',
];
const days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

type CalendarViewProps = {
  readonly currentMonth: number;
  readonly currentDay: number;
  readonly monthEntries: Array<MonthEntry>;
  readonly onNextMonthClick: () => void;
  readonly onPrevMonthClick: () => void;
};

export const CalendarView = ({
  currentMonth,
  currentDay,
  monthEntries,
  onNextMonthClick,
  onPrevMonthClick,
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
        {monthEntries.map((monthEntry) => (
          <CalendarEntry
            key={`${monthEntry.month}-${monthEntry.day}`}
            monthEntry={monthEntry}
            isCurrentDay={monthEntry.day === currentDay}
          />
        ))}
      </div>
    </div>
  );
};
