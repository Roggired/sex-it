import { CalendarView } from './calendar-view';
import { useMemo, useState } from 'react';

const getStartDateOfMonth = (date: Date) =>
  new Date(date.getFullYear(), date.getMonth(), 0);
const getEndDateOfMonth = (date: Date) =>
  new Date(date.getFullYear(), date.getMonth() + 1, 0);

export type MonthEntry = {
  readonly month: number;
  readonly day: number;
  readonly isCurrentMonth: boolean;
};

type CalendarContainerProps = {
  readonly onDayClick?: (day: number, month: number) => void;
};

export const CalendarContainer = ({ onDayClick }: CalendarContainerProps) => {
  const [currentDate, setCurrentDate] = useState(new Date());

  const endDateInMonth = getEndDateOfMonth(currentDate);
  const endDateOfPreviousMonth = getStartDateOfMonth(currentDate);
  const startDate = endDateOfPreviousMonth.getDay();
  const endDate = endDateInMonth.getDay();
  const daysInMonth = endDateInMonth.getDate();
  const daysInPreviousMonth = endDateOfPreviousMonth.getDate();

  const dates: Array<MonthEntry> = useMemo(() => {
    const tmp: Array<MonthEntry> = [];

    for (let i = startDate - 1; i > -1; i--) {
      tmp.push({
        month: currentDate.getMonth() - 1,
        day: daysInPreviousMonth - i,
        isCurrentMonth: false,
      });
    }

    for (let i = 0; i < daysInMonth; i++) {
      tmp.push({
        month: currentDate.getMonth(),
        day: i + 1,
        isCurrentMonth: true,
      });
    }

    if (endDate !== 0) {
      for (let i = 0; i < 7 - endDate; i++) {
        tmp.push({
          month: currentDate.getMonth() + 1,
          day: i + 1,
          isCurrentMonth: false,
        });
      }
    }

    return tmp;
  }, [currentDate.getMonth(), currentDate.getDate()]);

  return (
    <CalendarView
      monthEntries={dates}
      onDayClick={onDayClick}
      currentDay={currentDate.getDate()}
      currentMonth={currentDate.getMonth()}
      currentYear={currentDate.getFullYear()}
      onNextMonthClick={() =>
        setCurrentDate(
          (date) => new Date(date.getFullYear(), date.getMonth() + 1)
        )
      }
      onPrevMonthClick={() =>
        setCurrentDate(
          (date) => new Date(date.getFullYear(), date.getMonth() - 1)
        )
      }
    />
  );
};
