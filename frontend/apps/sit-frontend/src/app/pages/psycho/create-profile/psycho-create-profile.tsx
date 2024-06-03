import { Page } from '../../shared/page/page';
import './psycho-create-profile.scss';
import Alla from '../../../../assets/img.png';
import { SuiInput } from '../../../sui/sui-input/sui-input';
import { SuiButton } from '../../../sui/sui-button/sui-button';
import { useNavigate } from 'react-router-dom';
import { routes } from '../../../utils/routes';

export const PsychoCreateProfile = () => {
  const navigate = useNavigate();

  return (
    <Page>
      <div className="psycho-create-profile">
        <div className="psycho-create-profile__header">
          <img src={Alla} alt="" />
          <div className="psycho-create-profile__main">
            <SuiInput label="Имя" />
            <SuiInput label="Email" />
            <div className="psycho-create-profile__main__inputs">
              <SuiInput label="Цена консультации (руб. в час):" />
              <div className="psycho-create-profile__main__inputs__checkbox">
                <span>Бесплатная 1-ая кон-ция</span>
                <input type="checkbox" />
              </div>
            </div>
          </div>
        </div>
        <div className="psycho-create-profile__desc">
          <span>О себе</span>
          <textarea rows={10}></textarea>
        </div>
        <div className="psycho-create-profile__buttons">
          <SuiButton onClick={() => navigate(routes.toPsychoCalendarPage())}>
            Сохранить
          </SuiButton>
          <SuiButton
            onClick={() => navigate(routes.toRoot())}
            buttonType="secondary"
          >
            Отменить
          </SuiButton>
        </div>
      </div>
    </Page>
  );
};
