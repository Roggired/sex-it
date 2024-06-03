import { useNavigate } from 'react-router-dom';
import { routes } from '../../../utils/routes';

export const SlotPageChooser = () => {
  const navigate = useNavigate();

  const style = {
    cursor: 'pointer',
  };

  return (
    <div>
      <span
        onClick={() => navigate(routes.toPsychoCalendarPage())}
        style={style}
      >
        Календарь{' '}
      </span>
      /
      <span
        onClick={() => navigate(routes.toPsychoApplicationsPage())}
        style={style}
      >
        {' '}
        Заявки
      </span>
    </div>
  );
};
