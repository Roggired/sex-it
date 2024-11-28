import { psychoProfileApi } from 'apps/sit-frontend/src/app/api/psycho/psycho-profile-api';
import { useEffect, useState } from 'react';
import { Page } from '../../shared/page/page';
import './psycho-create-profile.scss';
import Alla from '../../../../assets/img.png';
import { SuiInput } from '../../../sui/sui-input/sui-input';
import { SuiButton } from '../../../sui/sui-button/sui-button';
import { useNavigate } from 'react-router-dom';
import { routes } from '../../../utils/routes';
import {useAtomValue} from "jotai/index";
import {userDataAtom} from "../../../auth/auth-cache";

export const PsychoCreateProfile = () => {
  const navigate = useNavigate();
  const [name, setName] = useState('Алла Сергеевна');
  const [email, setEmail] = useState('test@mail.ru');
  const [price, setPrice] = useState(2000);
  const [isFree, setIsFree] = useState(false);
  const [desc, setDesc] = useState('Я крутая');
  const { id } = useAtomValue(userDataAtom)

  const [updateProfile] = psychoProfileApi.useCreateOrUpdatePsychoMutation();

  const { data } = psychoProfileApi.useGetPsychoQuery(
    {
      id: id,
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
          <img src={Alla} alt="" />
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
              updateProfile({
                price,
                email,
                name,
                id,
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
