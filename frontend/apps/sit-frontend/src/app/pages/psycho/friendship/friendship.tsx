import { referralProgramApi } from 'apps/sit-frontend/src/app/api/refer/refer-api';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAtomValue } from 'jotai/index';
import { userDataAtom } from '../../../auth/auth-cache';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';

export const FriendshipStatusForPsychoPage = () => {
  const navigate = useNavigate();
  const { id } = useAtomValue(userDataAtom); // Получаем ID пользователя (если нужно)

//   const [status, setStatus] = useState<{ psychoName: string; friendshipStatus: string } | null>(null);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState<string | null>(null);

  // Используем query для получения данных о статусе дружбы с психологом
    const { data: friend } = referralProgramApi.useGetFriendFriendshipForPsychoQuery({
      pageNumber: 0,
      pageSize: 10000,
    });

//   useEffect(() => {
//     if (loading) {
//       setLoading(true);
//     } else if (error) {
//       setError("Ошибка при получении статуса дружбы");
//       setLoading(false);
//     } else {
//       setStatus(data ?? null); // Если запрос успешен, сохраняем полученные данные в состояние
//       setLoading(false);
//     }
//   }, [isLoading, isError, data]);
//
//   if (loading) {
//     return <div className="loading">Загрузка...</div>;
//   }
//
//   if (error) {
//     return <div className="error-message">{error}</div>;
//   }
//
//   if (!status) {
//     return <div>Статус дружбы не найден.</div>;
//   }

  return (
    <div className="friendship-status">
      <h1>Статус дружбы с психологом</h1>
      <div className="friendship-status__info">
      { availablePsychos && availablePsychos.content.map((friend) => (
                    <FriendCard
                      key={friend.id}
                      id={friend.id}
                      friend={friend.name}
                      status={friend.status}
                    />
                  ))
                }
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

const FriendCard = ({
  friend,
  id,
  percent,
  status,
}: {
  readonly id: number;
  readonly friend: string;
  readonly percent: number
  readonly status: string
}) => {

  return (
    <div className="client-applications__apps__entry">
      <div>
        <b>{friend}</b>
      </div>
    </div>
  );
};
