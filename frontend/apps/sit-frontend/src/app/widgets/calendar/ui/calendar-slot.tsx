import { SlotView } from 'apps/sit-frontend/src/app/api/slot/model';
import classNames from 'classnames';

type CalendarSlotProps = {
  readonly onSlotClick?: () => void;
  readonly slotView: SlotView;
};

export const CalendarSlot = ({ onSlotClick, slotView }: CalendarSlotProps) => {
  const s = slotView.time.split(':').splice(0, 2).join(':');

  return (
    <div
      className={classNames('calendar__slot', {
        empty: slotView.status === 'EMPTY',
        need: slotView.status === 'NEED_REVIEW',
        done: slotView.status === 'DONE',
      })}
      onClick={onSlotClick}
    >
      {s} {slotView.status === 'EMPTY' && 'Свободно'}
      {slotView.status === 'PLANNED' && 'Запланировано'}
      {slotView.status === 'DONE' && 'Проведено'}
      {slotView.status === 'NEED_REVIEW' && 'Есть заявки'}
    </div>
  );
};
