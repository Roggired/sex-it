import './psycho-slot.scss';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';

export const PsychoSlotPage = () => {
  const navigate = useNavigate();

  return (
    <div className="psycho-slot">
      <h1>06 Май 2024</h1>
      <b>Алла Сергеевна</b>
      <span>Цена: 2000 руб.</span>
      <span>Первая консультация бесплатна: да</span>
      <div>
        Анонимно: <input type="checkbox" />
      </div>
      <select>
        <option value={1}>11-00</option>
        <option value={2}>12-00</option>
      </select>
      <span>Описание проблемы:</span>
      <textarea rows={10}></textarea>
      <div className="psycho-slot__btns">
        <SuiButton>Записаться</SuiButton>
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
