import './psycho-list.scss';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { SuiInput } from 'apps/sit-frontend/src/app/sui/sui-input/sui-input';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';
import Alla from '../../../../assets/img.png';

export const PsychoListPage = () => {
  return (
    <div className="psycho-list">
      <div className="psycho-list__sidebar">
        <SuiInput label="ФИО:" />
        <SuiButton>Поиск</SuiButton>
      </div>
      <div className="psycho-list__main">
        <div className="psycho-list__list">
          {[1, 2, 3, 4].map((value) => (
            <PsychoCard
              key={value}
              id={value}
              name="ALla"
              price={111}
              rating={2.4}
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
      <h1>{name}</h1>
      <span>Цена за час: {price}</span>
      <span>Рейтинг: ${rating}</span>
    </div>
  );
};
