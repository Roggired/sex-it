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
        <span>SEX-IT</span>
        <span onClick={() => navigate(routes.toClientPsychoList())}>
          Психологи
        </span>
        <span onClick={() => navigate(routes.toClientApplications())}>
          Мои заявки
        </span>
        <span onClick={() => navigate(routes.toRoot())}>Выйти</span>
      </div>
    );
  }

  return (
    <div className="header">
      <span>SEX-IT</span>
      <span onClick={() => navigate(routes.toCreatePsychoPage())}>Профиль</span>
      <span onClick={() => navigate(routes.toPsychoCalendarPage())}>
        Консультации
      </span>
      <span onClick={() => navigate(routes.toRoot())}>Выйти</span>
    </div>
  );
};
