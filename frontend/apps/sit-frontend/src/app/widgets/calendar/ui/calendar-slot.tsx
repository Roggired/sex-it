type CalendarSlotProps = {
  readonly onSlotClick?: () => void;
};

export const CalendarSlot = ({ onSlotClick }: CalendarSlotProps) => {
  return (
    <div className="calendar__slot" onClick={onSlotClick}>
      11-00 Запланировано
    </div>
  );
};
