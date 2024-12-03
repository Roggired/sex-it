import './create-slot.scss';
import {slotApi} from 'apps/sit-frontend/src/app/api/slot/slot-api';
import {SuiButton} from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import {SuiInput} from 'apps/sit-frontend/src/app/sui/sui-input/sui-input';
import {routes} from 'apps/sit-frontend/src/app/utils/routes';
import {useState} from 'react';
import {useNavigate} from 'react-router-dom';

export const CreateSlotPage = ({ onDone }: { readonly onDone: () => void }) => {
  const navigate = useNavigate();

  const [date, setDate] = useState(new Date().toLocaleDateString('en-CA'));
  const [time, setTime] = useState('');

  const [createSlot] = slotApi.useCreateSlotMutation();

  const handleClick = () => {
    if (!date || !time) return;

    const d = date.split('-');

    createSlot({
      time,
      yearId: +d[0],
      monthId: +d[1] - 1,
      dayId: +d[2] - 1,
    })
      .unwrap()
      .then(onDone)
      .catch(() => alert('Такой слот уже есть'));
  };

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
        <SuiButton onClick={handleClick}>Создать</SuiButton>
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
