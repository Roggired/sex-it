import { useSetAtom } from 'jotai';
import { client, psycho, userAtom } from '../state/user-atom';
import { Page } from './shared/page/page';
import './root-page.scss';
import { useNavigate } from 'react-router-dom';
import { routes } from '../utils/routes';
import { SuiButton } from '../sui/sui-button/sui-button';

export const RootPage = () => {
  const setUser = useSetAtom(userAtom);
  const navigate = useNavigate();

  return (
    <Page center>
      <div className="root-page">
        <SuiButton
          onClick={() => {
            setUser(client);
            navigate(routes.toClientPsychoList());
          }}
        >
          Продолжить как Клиент
        </SuiButton>
        <SuiButton
          onClick={() => {
            setUser(psycho);
            navigate(routes.toCreatePsychoPage());
          }}
        >
          Продолжить как Психолог
        </SuiButton>
      </div>
    </Page>
  );
};
