import './header.scss';
import { userAtom } from 'apps/sit-frontend/src/app/state/user-atom';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useAtomValue } from 'jotai';
import { useNavigate } from 'react-router-dom';

export const Header = () => {
  const navigate = useNavigate();
  const user = useAtomValue(userAtom);

  if (user.type === 'CLIENT') {
    return (
      <div className="header">
        <h3>SEX-IT</h3>
        <h4 onClick={() => navigate(routes.toClientPsychoList())}>
          Психологи
        </h4>
        <h4 onClick={() => navigate(routes.toClientApplications())}>
          Мои заявки
        </h4>
        <h4 onClick={() => navigate(routes.toRoot())}>Выйти</h4>
      </div>
    );
  }

  return (
    <div className="header">
      <h3>SEX-IT</h3>
      <h4 onClick={() => navigate(routes.toCreatePsychoPage())}>Профиль</h4>
      <h4 onClick={() => navigate(routes.toPsychoCalendarPage())}>
        Консультации
      </h4>
      <h4 onClick={() => navigate(routes.toRoot())}>Выйти</h4>
    </div>
  );
};
