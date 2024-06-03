import './applications-page.scss';
import { SlotPageChooser } from 'apps/sit-frontend/src/app/pages/shared/slot-page-chooser/slot-page-chooser';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';

export const ApplicationsPage = () => {
  const navigate = useNavigate();

  return (
    <div className="applications-page">
      <SlotPageChooser />
      <ApplicationEntry
        creationDate="121312"
        desc="Я лоххххх"
        onClick={() => navigate(routes.toPsychoApplication(1))}
      />
    </div>
  );
};

const ApplicationEntry = ({
  name,
  desc,
  creationDate,
  onClick,
}: {
  readonly name?: string;
  readonly creationDate: string;
  readonly desc: string;
  readonly onClick: () => void;
}) => {
  return (
    <div className="applications-page__entry" onClick={onClick}>
      <b>
        Заявка от {name ? `пользователя ${name}` : 'анонимного пользователя'}
      </b>
      <span>Отправлена: {creationDate}</span>
      <span>{desc}</span>
    </div>
  );
};
