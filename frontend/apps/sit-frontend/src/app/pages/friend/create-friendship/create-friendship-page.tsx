import './friendship-status.scss';
import './create-friendship.scss';
import {referralProgramApi} from 'apps/sit-frontend/src/app/api/refer/refer-api';
import {useState} from 'react';
import {SuiInput} from 'apps/sit-frontend/src/app/sui/sui-input/sui-input';
import {SuiButton} from "../../../sui/sui-button/sui-button";
import {SuiLoader} from "../../../sui/sui-loader/sui-loder";

export const FriendshipPage = () => {
  const [search, setSearch] = useState('')
  const [finalSearch, setFinalSearch] = useState<string | undefined>(undefined)
  const [currFilter, setCurrFilter] = useState<
    'ADD_FRIEND' | 'CURRENT' | 'REJECTED'
  >('ADD_FRIEND');

  // TODO: добавить запрос на отмену заявки
  const { data: friendshipStatusData } = referralProgramApi.useGetFriendFriendshipQuery();

  const { data: availablePsychos } = referralProgramApi.useGetAvailablePsychoQuery({
    request: {
      name: finalSearch
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
        <SuiButton onClick={() => setFinalSearch(search)}>Поиск</SuiButton>
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
          { availablePsychos && friendshipStatusData && availablePsychos.content.length > 0 && friendshipStatusData.content.length > 0 && <div className="separator"></div> }
          { friendshipStatusData && friendshipStatusData.content.filter((el) => el.friendshipStatus === 'CREATED').map((el) => (
            <PsychoCard
              key={el.id}
              id={el.id}
              psycho={el.psychoName}
              state={'PENDING'}
            />
          ))
          }
          { !availablePsychos && !friendshipStatusData && <SuiLoader/> }
        </div>
      }
      {
        currFilter === 'CURRENT' && <div className="client-applications__apps">
          {friendshipStatusData && friendshipStatusData.content.filter((el) => el.friendshipStatus === 'ACCEPTED').map((el) => (
            <PsychoCard
              key={el.id}
              id={el.id}
              psycho={el.psychoName}
              state={'CURRENT'}
            />
          ))
          }
          {!availablePsychos && !friendshipStatusData && <SuiLoader/>}
        </div>
      }
      {
        currFilter === 'REJECTED' && <div className="client-applications__apps">
          {friendshipStatusData && friendshipStatusData.content.filter((el) => el.friendshipStatus === 'REJECTED').map((el) => (
            <PsychoCard
              key={el.id}
              id={el.id}
              psycho={el.psychoName}
              state={'REJECTED'}
            />
          ))
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
      .then((referralId) => {
        const link = `http://localhost:3000/sexit/client/psycho-card/${id}?referralId=${referralId}`
        copyTextToClipboard(link)
        alert("Реферальная ссылка скопирована")
      })
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

function fallbackCopyTextToClipboard(text: string) {
  const textArea = document.createElement("textarea");
  textArea.value = text;

  // Avoid scrolling to bottom
  textArea.style.top = "0";
  textArea.style.left = "0";
  textArea.style.position = "fixed";

  document.body.appendChild(textArea);
  textArea.focus();
  textArea.select();

  try {
    const successful = document.execCommand('copy');
    const msg = successful ? 'successful' : 'unsuccessful';
    console.log('Fallback: Copying text command was ' + msg);
  } catch (err) {
    console.error('Fallback: Oops, unable to copy', err);
  }

  document.body.removeChild(textArea);
}
function copyTextToClipboard(text: string) {
  if (!navigator.clipboard) {
    fallbackCopyTextToClipboard(text);
    return;
  }
  navigator.clipboard.writeText(text).then(function() {
    console.log('Async: Copying to clipboard was successful!');
  }, function(err) {
    console.error('Async: Could not copy text: ', err);
  });
}
