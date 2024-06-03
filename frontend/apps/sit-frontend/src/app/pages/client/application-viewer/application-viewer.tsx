import './application-viewer.scss';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';

export const ClientApplicationViewerPage = () => {
  const navigate = useNavigate();

  return (
    <div className="application-viewer">
      <h1>06 Май 2024 11:00</h1>
      <b>Алла Сергеевна</b>
      <span>Цена: 2000 руб.</span>
      <span>Первая консультация бесплатна: да</span>
      <span>Анонимно: да</span>
      <span>Дистанционная консультация</span>
      <div>
        <SuiButton>Подключиться</SuiButton>
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
