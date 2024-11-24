import {useLocation, useNavigate} from 'react-router-dom';
import { routes } from '../../../utils/routes';
import './slot-page-chooser.scss'

export const SlotPageChooser = () => {
  const navigate = useNavigate();
  const {pathname} = useLocation()

  const style = {
    cursor: 'pointer',
  };

  return (
    <div>
      <span
        className={pathname.endsWith("calendar") ? 'slot-page-chooser' : ''}
        onClick={() => navigate(routes.toPsychoCalendarPage())}
        style={style}
      >
        Календарь{' '}
      </span>
      /
      <span
        className={pathname.endsWith("applications") ? 'slot-page-chooser' : ''}
        onClick={() => navigate(routes.toPsychoApplicationsPage())}
        style={style}
      >
        {' '}
        Заявки
      </span>
    </div>
  );
};
