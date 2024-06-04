import './applications-page.scss';
import { applicationsApi } from 'apps/sit-frontend/src/app/api/applications/applications-api';
import { SlotPageChooser } from 'apps/sit-frontend/src/app/pages/shared/slot-page-chooser/slot-page-chooser';
import { psycho } from 'apps/sit-frontend/src/app/state/user-atom';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';

export const ApplicationsPage = () => {
  const navigate = useNavigate();
  const { data } = applicationsApi.useGetApplicationsQuery(psycho.id);

  if (!data) {
    return <></>;
  }

  return (
    <div className="applications-page">
      <SlotPageChooser />
      {data.map((d) => (
        <ApplicationEntry
          key={d.id}
          creationDate={new Date(d.creationTime).toLocaleString()}
          desc={d.description ?? ''}
          onClick={() => navigate(routes.toPsychoApplication(d.id))}
        />
      ))}
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
