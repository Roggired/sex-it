import './friendship-status.scss';
import './create-friendship.scss';
import { referralProgramApi } from 'apps/sit-frontend/src/app/api/refer/refer-api';
import { PsychoProfileForCatalogueView } from 'apps/sit-frontend/src/app/api/refer/model';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAtomValue } from 'jotai/index';
import { userDataAtom } from '../../../auth/auth-cache';
import { PaginationSingle } from '../../../sui/icons/pagination/pagination-single';
import { SuiInput } from 'apps/sit-frontend/src/app/sui/sui-input/sui-input';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';

export const FriendshipPage = () => {
  const [pageNumber, setPageNumber] = useState(0);
  const [selectedPsychoId, setSelectedPsychoId] = useState<number | null>(null);
  const [status, setStatus] = useState<{ psychoName: string; friendshipStatus: string } | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const { id } = useAtomValue(userDataAtom); // Получаем ID пользователя
  const { data: friendshipStatusData, isError, isLoading } = referralProgramApi.useGetFriendFriendshipQuery();

  const { data, isLoading: psychoLoading, error: psychoError } = referralProgramApi.useGetAvailablePsychoQuery({
    pageNumber,
    pageSize: 6,
  });

  const [createFriendship] = referralProgramApi.useCreateFriendshipMutation();

  const handleCreateFriendship = (psychoId: number) => {
    createFriendship({ psychoId });
  };

  useEffect(() => {
    if (isLoading) {
      setLoading(true);
    } else if (isError) {
      setError("Ошибка при получении статуса дружбы");
      setLoading(false);
    } else {
      setStatus(friendshipStatusData ?? null); // Если запрос успешен, сохраняем полученные данные в состояние
      setLoading(false);
    }
  }, [isLoading, isError, friendshipStatusData]);

  if (loading) {
    return <div>Загрузка...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  if (status) {
    return (
      <div className="friendship-status">
        <h1>Статус дружбы с психологом</h1>
        <div className="friendship-status__info">
          <p><b>Психолог: </b>{status.psychoName}</p>
          <p><b>Статус дружбы: </b>{status.friendshipStatus}</p>
        </div>
        <div className="friendship-status__btns">
          <button className="primary" onClick={() => navigate(routes.toBack())}>
            Назад
          </button>
          <button
            className="secondary"
          //  onClick={() => navigate(routes.toReferralProgram())}
          >
            Перейти к программе
          </button>
        </div>
      </div>
    );
  }

  if (psychoLoading) {
    return <div>Загрузка...</div>;
  }

  if (psychoError) {
    return <div>Произошла ошибка при загрузке психологов.</div>;
  }

  return (
    <div className="choose-psycho-page">
      <h1>Выбор психолога для дружбы</h1>
      <div className="choose-psycho-page__search">
        <SuiInput placeholder="Поиск психолога" />
      </div>

      <div className="choose-psycho-page__list">
        {data?.content.map((psycho) => (
          <PsychoCard
            key={psycho.id}
            psycho={psycho}
            onSelect={() => setSelectedPsychoId(psycho.id)}
            onSendFriendshipRequest={handleCreateFriendship}
            isSelected={selectedPsychoId === psycho.id}
          />
        ))}
      </div>

      <div className="choose-psycho-page__pagination">
        <PaginationSingle
          enabled={pageNumber > 0}
          onClick={() => setPageNumber(pageNumber - 1)}
        />
        <span>{pageNumber + 1}</span>
        <PaginationSingle
          enabled={pageNumber < (data?.totalPages ?? 0) - 1}
          onClick={() => setPageNumber(pageNumber + 1)}
          isRightRotated={true}
        />
      </div>
    </div>
  );
};

interface PsychoCardProps {
  psycho: PsychoProfileForCatalogueView;
  onSelect: () => void;
  onSendFriendshipRequest: (psychoId: number) => void;
  isSelected: boolean;
}

const PsychoCard = ({
  psycho,
  onSelect,
  onSendFriendshipRequest,
  isSelected,
}: PsychoCardProps) => {
  return (
    <div
      className={`psycho-card ${isSelected ? 'selected' : ''}`}
      onClick={onSelect}
    >
      <h3>{psycho.name}</h3>
      <p>Цена за час: {psycho.price} руб.</p>
      <p>Рейтинг: {psycho.rating ?? 'Не задан'}</p>
      <button className="primary" onClick={() => onSendFriendshipRequest(psycho.id)}>
        {isSelected ? 'Отправить запрос на дружбу' : 'Выбрать'}
      </button>
    </div>
  );
};
