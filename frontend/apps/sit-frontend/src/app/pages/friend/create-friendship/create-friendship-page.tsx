import './friendship-status.scss';
import './create-friendship.scss';
import {referralProgramApi} from 'apps/sit-frontend/src/app/api/refer/refer-api';
import {useState} from 'react';
import {SuiInput} from 'apps/sit-frontend/src/app/sui/sui-input/sui-input';
import {SuiButton} from "../../../sui/sui-button/sui-button";
import {SuiLoader} from "../../../sui/sui-loader/sui-loder";

export const FriendshipPage = () => {
  const [search, setSearch] = useState('')
  const [currFilter, setCurrFilter] = useState<
    'ADD_FRIEND' | 'CURRENT' | 'REJECTED'
  >('ADD_FRIEND');

  // TODO: вернуть всех текущий друзей-психологов
  // TODO: добавить идентификатор психолога в ответ
  // TODO: добавить запрос на отмену заявки
  const { data: friendshipStatusData } = referralProgramApi.useGetFriendFriendshipQuery();

  // TODO: добавить параметр для поиска по имени психолога
  const { data: availablePsychos } = referralProgramApi.useGetAvailablePsychoQuery({
    request: {
      name: undefined
      },
    pageNumber: 0,
    pageSize: 10000,
  });

  return (
    <div className="client-applications">
      <div className="client-applications__breadcrumbs">
        <span className={currFilter === 'ADD_FRIEND' ? 'chosen' : ''} onClick={() => setCurrFilter('ADD_FRIEND')}>Добавить друга</span>
        <span className={currFilter === 'CURRENT' ? 'chosen' : ''}
              onClick={() => setCurrFilter('CURRENT')}>/ Друзья</span>
        <span className={currFilter === 'REJECTED' ? 'chosen' : ''} onClick={() => setCurrFilter('REJECTED')}>/ Отклоненные заявки</span>
      </div>
      <div className="client-applications__filter">
        <SuiInput value={search} onChange={e => setSearch(e.target.value)} placeholder="Поиск по психологу"/>
        <SuiButton onClick={() => {}}>Поиск</SuiButton>
      </div>
      {
        currFilter === 'ADD_FRIEND' && <div className="client-applications__apps">
          { availablePsychos && availablePsychos.content.map((psycho) => (
              <PsychoCard
                key={psycho.id}
                id={psycho.id}
                psycho={psycho.name}
                state={'ADD_FRIEND'}
              />
            ))
          }
          { availablePsychos && friendshipStatusData && availablePsychos.content.length > 0 && <div className="separator"></div> }
          { friendshipStatusData && friendshipStatusData.friendshipStatus === 'CREATED' && <PsychoCard
              key={friendshipStatusData.psychoName}
              id={1}
              psycho={friendshipStatusData.psychoName}
              state={'PENDING'}
            />
          }
          { !availablePsychos && !friendshipStatusData && <SuiLoader/> }
        </div>
      }
      {
        currFilter === 'CURRENT' && <div className="client-applications__apps">
          {friendshipStatusData && friendshipStatusData.friendshipStatus === 'ACCEPTED' && <PsychoCard
            key={friendshipStatusData.psychoName}
            id={1}
            psycho={friendshipStatusData.psychoName}
            state={'CURRENT'}
          />
          }
          {!availablePsychos && !friendshipStatusData && <SuiLoader/>}
        </div>
      }
      {
        currFilter === 'REJECTED' && <div className="client-applications__apps">
          {friendshipStatusData && friendshipStatusData.friendshipStatus === 'REJECTED' && <PsychoCard
            key={friendshipStatusData.psychoName}
            id={1}
            psycho={friendshipStatusData.psychoName}
            state={'REJECTED'}
          />
          }
          {!availablePsychos && !friendshipStatusData && <SuiLoader/>}
        </div>
      }
    </div>
  );
};

const PsychoCard = ({
  psycho,
  id,
  state,
}: {
  readonly id: number;
  readonly psycho: string;
  readonly state: 'ADD_FRIEND' | 'CURRENT' | 'REJECTED' | 'PENDING';
}) => {
  const [createFriendship] = referralProgramApi.useCreateFriendshipMutation()
  const [createReferralProgram] = referralProgramApi.useCreateReferralProgramMutation()
  // TODO: add cancel invite request

  const onGetReferralLink = () => {
    createReferralProgram(id)
      .then((result) => result.data)
      .then((referralId) => alert(`http://localhost:3000/sexit/client/psycho-card/${id}?referralId=${referralId}`))
  }

  return (
    <div className="client-applications__apps__entry">
      <div>
        <b>{psycho}</b>
      </div>
      {
        state === 'ADD_FRIEND' && <SuiButton onClick={() => createFriendship({
          psychoId: id
        })}>
          Добавить
        </SuiButton>
      }
      {
        state === 'PENDING' && <SuiButton onClick={() => {}} buttonType='secondary'>
          Отменить запрос
        </SuiButton>
      }
      {
        state === 'CURRENT' && <SuiButton onClick={onGetReferralLink}>
          Получить ссылку
        </SuiButton>
      }
      {
        state === 'REJECTED' && <>
        </>
      }
    </div>
  );
};
