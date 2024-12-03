import { SlotView } from 'apps/sit-frontend/src/app/api/slot/model';
import classNames from 'classnames';
import {Modal} from "../../../sui/modal/sui-modal";
import {useState} from "react";
import {PsychoSlotPage} from "../../../pages/client/psycho-slot/psycho-slot";
import {useGetNumberPathParam} from "../../../hooks/useGetNumberPathParam";
import {months} from "../../../utils/date-mapper";

type CalendarSlotProps = {
  readonly onSlotClick?: () => void;
  readonly slotView: SlotView;
  month: number
  day: number
};

export const CalendarSlot = ({ onSlotClick, slotView, month, day}: CalendarSlotProps) => {
  const s = slotView.time.split(':').splice(0, 2).join(':');
  const [open, setOpen] = useState(false)
  const psychoId = useGetNumberPathParam('psychoId')

  return (
    <div
      className={classNames('calendar__slot', {
        empty: slotView.status === 'EMPTY',
        need: slotView.status === 'NEED_REVIEW',
        done: slotView.status === 'DONE',
      })}
      onClick={() => setOpen(true)}
    >
      {s} {slotView.status === 'EMPTY' && 'Свободно'}
      {slotView.status === 'PLANNED' && 'Запланировано'}
      {slotView.status === 'DONE' && 'Проведено'}
      {slotView.status === 'NEED_REVIEW' && 'Есть заявки'}
      {psychoId && <Modal isOpen={open} onClose={() => setOpen(false)}>
        <PsychoSlotPage month={month} day={day} psychoId={psychoId} />
      </Modal>}
    </div>
  );
};
