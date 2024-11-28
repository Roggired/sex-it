import './psycho-list.scss';
import { psychoProfileApi } from 'apps/sit-frontend/src/app/api/psycho/psycho-profile-api';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { SuiInput } from 'apps/sit-frontend/src/app/sui/sui-input/sui-input';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';
import Alla from '../../../../assets/img.png';
import {useAtomValue} from "jotai/index";
import {userDataAtom} from "../../../auth/auth-cache";

export const PsychoListPage = () => {
  const { id } = useAtomValue(userDataAtom)

  const { data } = psychoProfileApi.useGetPsychoQuery({
    id: id,
    mode: 'CLIENT',
  }, {
    skip: !id
  });

  if (!data) {
    return <></>;
  }

  return (
    <div className="psycho-list">
      <div className="psycho-list__sidebar">
        <SuiInput label="ФИО:" />
        <SuiButton>Поиск</SuiButton>
      </div>
      <div className="psycho-list__main">
        <div className="psycho-list__list">
          {[data, data, data, data].map((value) => (
            <PsychoCard
              key={value.id}
              id={value.id}
              name={value.name}
              price={value.price}
              rating={value.rating ?? 0}
            />
          ))}
        </div>
        <div className="psycho-list__btns">
          <button>prev</button>1<button>next</button>
        </div>
      </div>
    </div>
  );
};

const PsychoCard = ({
  id,
  rating,
  name,
  price,
}: {
  readonly id: number;
  readonly name: string;
  readonly price: number;
  readonly rating: number;
}) => {
  const navigate = useNavigate();

  return (
    <div
      className="psycho-list__card"
      onClick={() => navigate(routes.toClientPsychoCard(id))}
    >
      <img src={Alla} />
      <h3>{name}</h3>
      <span>Цена за час: {price}</span>
      <span>Рейтинг: {rating}</span>
    </div>
  );
};
