import './create-slot.scss';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { SuiInput } from 'apps/sit-frontend/src/app/sui/sui-input/sui-input';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export const CreateSlotPage = () => {
  const navigate = useNavigate();

  const [date, setDate] = useState(new Date().toLocaleDateString('en-CA'));
  const [time, setTime] = useState('');

  return (
    <div className="create-slot">
      <h1>Новый слот</h1>
      <SuiInput
        type="date"
        label="Дата"
        value={date}
        onChange={(e) => setDate(e.target.value)}
      />
      <SuiInput
        type="time"
        label="Время"
        value={time}
        onChange={(e) => setTime(e.target.value)}
      />
      <div className="create-slot__btns">
        <SuiButton onClick={() => navigate(routes.toPsychoCalendarPage())}>
          Создать
        </SuiButton>
        <SuiButton
          buttonType="secondary"
          onClick={() => navigate(routes.toBack())}
        >
          Закрыть
        </SuiButton>
      </div>
    </div>
  );
};
