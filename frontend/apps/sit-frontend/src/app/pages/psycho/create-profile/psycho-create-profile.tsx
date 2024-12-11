import {psychoProfileApi} from 'apps/sit-frontend/src/app/api/psycho/psycho-profile-api';
import {useEffect, useState} from 'react';
import {Page} from '../../shared/page/page';
import './psycho-create-profile.scss';
import Alla from '../../../../assets/img.png';
import {SuiInput} from '../../../sui/sui-input/sui-input';
import {SuiButton} from '../../../sui/sui-button/sui-button';
import {useNavigate} from 'react-router-dom';
import {routes} from '../../../utils/routes';
import {useGetPsychoProfile} from "../../../hooks/useGetPsychoProfile";
import * as EmailValidator from "email-validator";

export const PsychoCreateProfile = () => {
  const navigate = useNavigate();
  const [name, setName] = useState('Алла Сергеевна');
  const [email, setEmail] = useState('test@mail.ru');
  const [price, setPrice] = useState(2000);
  const [isFree, setIsFree] = useState(false);
  const [desc, setDesc] = useState('Я крутая');
  const {id} = useGetPsychoProfile()

  const [updateProfile] = psychoProfileApi.useCreateOrUpdatePsychoMutation();

  const {data} = psychoProfileApi.useGetPsychoQuery(
    {
      id: id as number,
      mode: 'CLIENT',
    },
    {
      skip: !id,
      refetchOnMountOrArgChange: true,
    }
  );

  useEffect(() => {
    if (data) {
      setName(data.name);
      setEmail(data.email);
      setPrice(data.price);
      setIsFree(data.isFirstFree);
      setDesc(data.bio);
    }
  }, [data]);

  if (!data) {
    return <></>;
  }

  return (
    <Page>
      <div className="psycho-create-profile">
        <div className="psycho-create-profile__header">
          <img src={Alla} alt=""/>
          <div className="psycho-create-profile__main">
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
            <div className="psycho-create-profile__main__inputs">
              <SuiInput
                label="Цена консультации (руб. в час)"
                type="number"
                value={price}
                onChange={(e) => setPrice(+e.target.value)}
              />
              <div className="psycho-create-profile__main__inputs__checkbox">
                <span>Бесплатная 1-ая кон-ция</span>
                <input
                  type="checkbox"
                  checked={isFree}
                  onChange={(e) => setIsFree((prevState) => !prevState)}
                />
              </div>
            </div>
          </div>
        </div>
        <div className="psycho-create-profile__desc">
          <span>О себе</span>
          <textarea
            rows={10}
            value={desc}
            onChange={(e) => setDesc(e.target.value)}
          ></textarea>
        </div>
        <div className="psycho-create-profile__buttons">
          <SuiButton
            onClick={() => {
              if (price <= 0) {
                alert("Цена должна быть больше 0");
                return
              }

              if (!email) {
                alert("Введите email")
                return;
              }

              if (!EmailValidator.validate(email)) {
                alert("Некорректный email")
                return;
              }

              if (!name) {
                alert("Введите имя");
                return;
              }

              if (!desc) {
                alert("введите описание");
                return;
              }

              updateProfile({
                price,
                email,
                name,
                id: id as number,
                bio: desc,
                isFirstFree: isFree,
              })
                .unwrap()
                .then(() => navigate(routes.toPsychoCalendarPage()));
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
