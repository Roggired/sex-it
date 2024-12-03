import {SlotView} from 'apps/sit-frontend/src/app/api/slot/model';
import classNames from 'classnames';
import {MonthEntry} from '../calendar-container';
import {CalendarSlot} from './calendar-slot';
import {Modal} from "../../../sui/modal/sui-modal";
import {useState} from "react";
import {PsychoDayViewer} from "../../../pages/psycho/psycho-slot-viewer/psycho-day-viewer";

export type CalendarEntryProps = {
  readonly monthEntry: MonthEntry;
  readonly isCurrentDay: boolean;
  readonly onDayClick?: (day: number, month: number) => void;
  readonly onSlotClick?: (day: number, month: number) => void;
  readonly slots: Array<SlotView>;
};

export const CalendarEntry = ({
                                monthEntry: {month, day, isCurrentMonth, isHoliday},
                                isCurrentDay,
                                onDayClick,
                                onSlotClick,
                                slots,
                              }: CalendarEntryProps) => {
  const totalSlots = slots.length;
  const [isOpened, setIsOpened] = useState(false)
  return (
    <div
      onClick={() => {
        if (slots.length) {
          setIsOpened(true)
        }
      }}
      className={classNames('calendar__entry', {
        inactive: !isCurrentMonth,
        holiday: isHoliday
      })}
    >
      <div className="calendar__entry__date">
        <span>{isCurrentDay && 'Сегодня'}</span>
        <span>{day}</span>
      </div>
      {slots.slice(0, 2).map((s) => (
        <CalendarSlot
          key={s.id}
          onSlotClick={() => onSlotClick?.(day, month)}
          slotView={s}
        />
      ))}
      {totalSlots > 2 && (
        <div className="calendar__entry__more">Еще {totalSlots - 2} слота</div>
      )}
      {/* Тут ярик закостылил верстку */}
      {totalSlots === 1 && (
        <>
          <div style={{height: 20}}></div>
          <div style={{height: 20}}></div>
        </>
      )}
      {totalSlots === 2 && (
        <>
          <div style={{height: 20}}></div>
        </>
      )}
      <Modal isOpen={isOpened} onClose={() => setIsOpened(false)}>
        <PsychoDayViewer day={day} month={month}/>
      </Modal>
    </div>
  );
};
