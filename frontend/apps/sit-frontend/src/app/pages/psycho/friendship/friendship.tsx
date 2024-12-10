import { referralProgramApi } from 'apps/sit-frontend/src/app/api/refer/refer-api';
import { useState } from 'react';
import { SuiButton } from '../../../sui/sui-button/sui-button';
import { SuiLoader } from '../../../sui/sui-loader/sui-loder';

export const FriendshipStatusForPsychoPage = () => {
  const [currFilter, setCurrFilter] = useState<
    'PENDING' | 'ACCEPTED' | 'REJECTED'
  >('PENDING');

  // Используем query для получения данных о статусе дружбы с психологом
  const { data: friends } = referralProgramApi.useGetFriendFriendshipForPsychoQuery({
    pageNumber: 0,
    pageSize: 10000,
  });

  return (
    <div className="client-applications">
      <div className="client-applications__breadcrumbs">
        <span className={currFilter === 'PENDING' ? 'chosen' : ''} onClick={() => setCurrFilter('PENDING')}>Ожидающие заявки</span>
        <span className={currFilter === 'ACCEPTED' ? 'chosen' : ''}
              onClick={() => setCurrFilter('ACCEPTED')}>/ Друзья</span>
        <span className={currFilter === 'REJECTED' ? 'chosen' : ''} onClick={() => setCurrFilter('REJECTED')}>/ Отклоненные заявки</span>
      </div>
      {
        currFilter === 'PENDING' && <div className="client-applications__apps">
          { friends && friends.content.filter((friend) => friend.status === 'CREATED').map((friend) => (
            <FriendCard
              key={friend.id}
              id={friend.id}
              friendName={friend.friendName}
              status={'PENDING'}
            />
          ))
          }
          { !friends && <SuiLoader/> }
        </div>
      }
      {
        currFilter === 'ACCEPTED' && <div className="client-applications__apps">
          {friends && friends.content.filter((friend) => friend.status === 'ACCEPTED').map((friend) => (
            <FriendCard
              key={friend.id}
              id={friend.id}
              friendName={friend.friendName}
              status={'ACCEPTED'}
            />
          ))
          }
          { !friends && <SuiLoader/> }
        </div>
      }
      {
        currFilter === 'REJECTED' && <div className="client-applications__apps">
          { friends && friends.content.filter((friend) => friend.status === 'REJECTED').map((friend) => (
            <FriendCard
              key={friend.id}
              id={friend.id}
              friendName={friend.friendName}
              status={'REJECTED'}
            />
          ))
          }
          { !friends && <SuiLoader/> }
        </div>
      }
    </div>
  );
};

const FriendCard = ({
  friendName,
  id,
  status,
}: {
  readonly id: number;
  readonly friendName: string;
  readonly status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
}) => {
  const [changeStatus] = referralProgramApi.useUpdateFriendshipMutation()

  return (
    <div className="client-applications__apps__entry">
      <div>
        <b>{friendName}</b>
      </div>
      {
        status === 'PENDING' && (
          <div style={{display: 'flex', flexDirection: 'row', gap: '16px'}}>
            <SuiButton onClick={() => changeStatus({ psychoId: id, status: 'ACCEPTED' })}>
              Принять
            </SuiButton>
            <SuiButton onClick={() => changeStatus({ psychoId: id, status: 'REJECTED' })} buttonType='secondary'>
              Отклонить
            </SuiButton>
          </div>
        )
      }
    </div>
  );
};
