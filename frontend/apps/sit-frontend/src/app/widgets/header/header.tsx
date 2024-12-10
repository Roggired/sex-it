import './header.scss';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';
import {useUserRoles} from "../../auth/role";

export const Header = () => {
  const navigate = useNavigate();
  const {isPsycho, isClient, isFriend} = useUserRoles();

  if (isPsycho) {
    return (
      <header className="header">
        <h3>SEX-IT</h3>
        <h4 onClick={() => navigate(routes.toCreatePsychoPage())}>Профиль</h4>
        <h4 onClick={() => navigate(routes.toPsychoCalendarPage())}>
          Консультации
        </h4>
        <h4 onClick={() => navigate(routes.toPsychoSubscriptionPage())}>
          Подписка
        </h4>
        <h4 onClick={() => navigate(routes.toCreateFriendshipPsychoPage())}>
          Друганы
        </h4>
        <h4 onClick={() => navigate(routes.toLogout())}>Выйти</h4>
      </header>
    );
  }

  if (isFriend) {
    return (
      <header className="header">
        <h3>SEX-IT</h3>
        <h4 onClick={() => navigate(routes.toCreateFriendPage())}>Профиль</h4>
        <h4 onClick={() => navigate(routes.toCreateFriendShipPage())}>
          Друзья
        </h4>
        {/*<h4 onClick={() => navigate(routes.toPsychoSubscriptionPage())}>*/}
        {/*  Подписка*/}
        {/*</h4>*/}
        <h4 onClick={() => navigate(routes.toLogout())}>Выйти</h4>
      </header>
    )
  }


  if (isClient) {
    return (
      <header className="header">
        <h3>SEX-IT</h3>
        <h4 onClick={() => navigate(routes.toClientPsychoList())}>
          Психологи
        </h4>
        <h4 onClick={() => navigate(routes.toClientApplications())}>
          Мои заявки
        </h4>
        <h4 onClick={() => navigate(routes.toLogout())}>Выйти</h4>
      </header>
    );
  }

  return <header className="header">
    <h3>SEX-IT</h3>
    <h4 onClick={() => navigate(routes.toLogout())}>Выйти</h4>
  </header>
};
