import './friend-create-profile.scss';
import { useEffect, useState } from 'react';
import { Page } from '../../shared/page/page';
import './friend-create-profile.scss'; // Новый стиль для "Друга"
import { SuiInput } from '../../../sui/sui-input/sui-input';
import { SuiButton } from '../../../sui/sui-button/sui-button';
import { useNavigate } from 'react-router-dom';
import { routes } from '../../../utils/routes';
import { friendProfileApi } from 'apps/sit-frontend/src/app/api/friend/friend-profile-api'; // API для Друга

export const FriendCreateProfile = () => {
  const navigate = useNavigate();
  const [name, setName] = useState('Алексей Егошин');
  const [email, setEmail] = useState('friend@mail.ru');
  const [percent, setPercent] = useState(100); // Поле для процента

  const [updateProfile] = friendProfileApi.useCreateFriendMutation(); // Мутация для обновления/создания профиля Друга

  useEffect(() => {
    // Здесь можно загрузить начальные данные, если такие есть, например, из локального хранилища
    // Но т.к. id не используется, предположим, что профиль создается с нуля
  }, []);

  return (
    <Page>
      <div className="friend-create-profile">
        <div className="friend-create-profile__header">
          <div className="friend-create-profile__main">
            <SuiInput
              label="Имя"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
            <SuiInput
              label="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <SuiInput
              label="Процент"
              type="number"
              value={percent}
              onChange={(e) => setPercent(+e.target.value)} // Изменение процента
            />
          </div>
        </div>
        <div className="friend-create-profile__buttons">
          <SuiButton
            onClick={() => {
              updateProfile({
                body: { // Обернуть данные в поле `body`
                                 name,
                                 email,
                                 percent, // Передача процента
                               },
              })
                .unwrap()
               // .then(() => navigate(routes.toFriendProfilePage())); // Перенаправление на страницу профиля Друга
            }}
          >
            Сохранить
          </SuiButton>
          <SuiButton
            onClick={() => navigate(routes.toBack())}
            buttonType="secondary"
          >
            Отменить
          </SuiButton>
        </div>
      </div>
    </Page>
  );
};
