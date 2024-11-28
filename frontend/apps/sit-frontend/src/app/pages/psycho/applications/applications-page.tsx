import './applications-page.scss';
import { applicationsApi } from 'apps/sit-frontend/src/app/api/applications/applications-api';
import { SlotPageChooser } from 'apps/sit-frontend/src/app/pages/shared/slot-page-chooser/slot-page-chooser';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';
import {useAtomValue} from "jotai/index";
import {userDataAtom} from "../../../auth/auth-cache";
import {skipToken} from "@reduxjs/toolkit/query";

export const ApplicationsPage = () => {
  const navigate = useNavigate();
  const { id } = useAtomValue(userDataAtom)
  const { data } = applicationsApi.useGetApplicationsQuery(id ?? skipToken);

  if (!data) {
    return <></>;
  }

  return (
    <div className="applications-page">
      <SlotPageChooser />
      {data.map((d) => {
        const date = new Date(d.creationTime);
        date.setHours(date.getHours() + 3);
        return (
          <ApplicationEntry
            key={d.id}
            creationDate={date.toLocaleString()}
            desc={d.description ?? ''}
            onClick={() => navigate(routes.toPsychoApplication(d.id))}
          />
        );
      })}
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
