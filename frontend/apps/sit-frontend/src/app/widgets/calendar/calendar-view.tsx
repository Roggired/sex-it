import './calendar.scss';
import { months } from 'apps/sit-frontend/src/app/utils/date-mapper';
import { CalendarEntry } from './ui/calendar-entry';
import { MonthEntry } from './calendar-container';
import {FaChevronLeft, FaChevronRight} from "react-icons/fa6";

const days = ['ПН', 'ВТ', 'СР', 'ЧТ', 'ПТ', 'СБ', 'ВС'];

type CalendarViewProps = {
  readonly currentMonth: number;
  readonly currentDay: number;
  readonly currentYear: number;
  readonly monthEntries: Array<MonthEntry>;
  readonly onNextMonthClick: () => void;
  readonly onPrevMonthClick: () => void;
  readonly onDayClick?: (day: number, month: number) => void;
  readonly onSlotClick?: (day: number, month: number) => void;
};

export const CalendarView = ({
  currentMonth,
  currentDay,
  onSlotClick,
  currentYear,
  monthEntries,
  onNextMonthClick,
  onPrevMonthClick,
  onDayClick,
}: CalendarViewProps) => {
  const realDate = new Date();

  return (
    <div className="calendar">
      <div className="calendar__header">
        <FaChevronLeft onClick={onPrevMonthClick} />
        {months[currentMonth]}, {currentYear}
        <FaChevronRight onClick={onNextMonthClick} />
      </div>
      <div className="calendar__body">
        {days.map((day) => (
          <span key={day} className="calendar__day_name">{day}</span>
        ))}
        {monthEntries.map((monthEntry) => {
          return (
            <CalendarEntry
              onSlotClick={onSlotClick}
              onDayClick={onDayClick}
              key={`${monthEntry.month}-${monthEntry.day}`}
              monthEntry={monthEntry}
              slots={monthEntry.slots}
              isCurrentDay={
                monthEntry.day === realDate.getDate() &&
                monthEntry.month === realDate.getMonth()
              }
            />
          );
        })}
      </div>
    </div>
  );
};
