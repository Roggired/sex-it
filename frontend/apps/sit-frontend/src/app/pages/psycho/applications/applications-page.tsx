import './applications-page.scss';
import {applicationsApi} from 'apps/sit-frontend/src/app/api/applications/applications-api';
import {SlotPageChooser} from 'apps/sit-frontend/src/app/pages/shared/slot-page-chooser/slot-page-chooser';
import {skipToken} from "@reduxjs/toolkit/query";
import {useGetPsychoProfile} from "../../../hooks/useGetPsychoProfile";
import {Fragment, useState} from "react";
import {Modal} from "../../../sui/modal/sui-modal";
import {ApplicationViewPage} from "../application-view/application-view-page";

export const ApplicationsPage = () => {
  const {id} = useGetPsychoProfile()
  const {data} = applicationsApi.useGetApplicationsQuery(id ?? skipToken);
  const [isOpened, setIsOpened] = useState(false)

  if (!data) {
    return <></>;
  }

  return (
    <div className="applications-page">
      <SlotPageChooser/>
      {data.map((d) => {
        const date = new Date(d.creationTime);
        date.setHours(date.getHours() + 3);
        return (
          <Fragment key={d.id}>
            <ApplicationEntry
              key={d.id}
              creationDate={date.toLocaleString()}
              desc={d.description ?? ''}
              onClick={() => setIsOpened(true)}
              frName={d.friendName}
            />
            <Modal isOpen={isOpened} onClose={() => setIsOpened(false)}>
              <ApplicationViewPage appId={d.id} close={() => setIsOpened(false)}/>
            </Modal>
          </Fragment>
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
  frName
                          }: {
  readonly name?: string;
  readonly creationDate: string;
  readonly desc: string;
  readonly onClick: () => void;
  frName?: string
}) => {
  return (
    <div className="applications-page__entry" onClick={onClick}>
      <b>
        Заявка от {name ? `пользователя ${name}` : 'анонимного пользователя'}
      </b>
      {frName && <span>От другана: {frName}</span>}
      <span>Отправлена: {creationDate}</span>
      <span>{desc}</span>
    </div>
  );
};
