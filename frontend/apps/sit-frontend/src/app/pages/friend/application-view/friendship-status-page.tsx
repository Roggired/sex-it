import './friendship-status.scss';
import { referralProgramApi } from 'apps/sit-frontend/src/app/api/refer/refer-api';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAtomValue } from 'jotai/index';
import { userDataAtom } from '../../../auth/auth-cache';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';

export const FriendshipStatusPage = () => {
  const navigate = useNavigate();
  const { id } = useAtomValue(userDataAtom); // Получаем ID пользователя (если нужно)

  const [status, setStatus] = useState<{ psychoName: string; friendshipStatus: string } | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Используем query для получения данных о статусе дружбы с психологом
  const { data, isError, isLoading } = referralProgramApi.useGetFriendFriendshipQuery();

  useEffect(() => {
    if (isLoading) {
      setLoading(true);
    } else if (isError) {
      setError("Ошибка при получении статуса дружбы");
      setLoading(false);
    } else {
      setStatus(data ?? null); // Если запрос успешен, сохраняем полученные данные в состояние
      setLoading(false);
    }
  }, [isLoading, isError, data]);

  if (loading) {
    return <div className="loading">Загрузка...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  if (!status) {
    return <div>Статус дружбы не найден.</div>;
  }

  return (
    <div className="friendship-status">
      <h1>Статус дружбы с психологом</h1>
      <div className="friendship-status__info">
        <p><b>Психолог: </b>{status.psychoName}</p>
        <p><b>Статус дружбы: </b>{status.friendshipStatus}</p>
      </div>
      <div className="friendship-status__btns">
        <button
          className="primary"
         // onClick={() => navigate(routes.toBack())}
        >
          Назад
        </button>
        <button
          className="secondary"
         // onClick={() => navigate(routes.toReferralProgram())}
        >
          Перейти к программе
        </button>
      </div>
    </div>
  );
};
