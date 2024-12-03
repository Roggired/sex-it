import {SlotStatus} from 'apps/sit-frontend/src/app/api/slot/model';
import {routes} from 'apps/sit-frontend/src/app/utils/routes';
import classNames from 'classnames';
import {useNavigate} from 'react-router-dom';
import {IoCloseSharp} from "react-icons/io5";
import {useState} from "react";
import {FaChevronDown, FaChevronUp} from "react-icons/fa";
import {SuiButton} from "../../../sui/sui-button/sui-button";
import {Modal} from "../../../sui/modal/sui-modal";
import {FeedbackPage} from "../feedback/feedback-page";
import app from "../../../app";

type DayViewerEntryProps = {
  readonly time: string;
  readonly isAnon: boolean;
  readonly isOffline: boolean;
  readonly status: SlotStatus;
  readonly desc?: string;
  readonly link?: string;
  readonly address?: string;
  readonly onDelete?: () => void;
  readonly appId?: number
};
export const DayViewerEntry = ({
                                 status,
                                 address,
                                 link,
                                 desc,
                                 isAnon,
                                 isOffline,
                                 time,
  appId,
                                 onDelete,
                               }: DayViewerEntryProps) => {
  const navigate = useNavigate();
  const [isOpened, setIsOpened] = useState(false)
  const isExpandable = desc || status === 'DONE'
  const [isModal, setIsModal] = useState(false)

  return (
    <div className="psycho-day-viewer__entry">
      <div className="psycho-day-viewer__entry__header">
        <b>{time.substring(0, time.lastIndexOf(":"))}</b>
        <div
          className={classNames('psycho-day-viewer__entry__header__block', {
            planned: status !== 'EMPTY',
          })}
        >
          {status === 'EMPTY' && (
            <div style={{display: 'flex', gap: '6px', alignItems: 'center'}}><span>
              Свободно
            </span>
              <IoCloseSharp
                className="psycho-day-viewer__entry__icon"
                style={{marginLeft: '16px', padding: '6px'}}
                onClick={onDelete}/></div>

          )}
          {status === 'NEED_REVIEW' && (
            <span
              style={{cursor: 'pointer'}}
              onClick={() => navigate(routes.toPsychoApplicationsPage())}
            >
              Просмотреть заявки
            </span>
          )}
          {(status === 'PLANNED' || status === 'DONE') && (
            <>
              <span>{isAnon ? 'Анонимная консультация' : 'Консультация'}</span>
              {isOffline ? (
                <span>{address}</span>
              ) : (
                <>
                  {status === 'DONE' && (
                    <span>
                      Проведено онлайн
                    </span>
                  )}
                  {status === 'PLANNED' && (
                    <span>
                      Онлайн:{' '}
                      <a style={{cursor: 'pointer'}} href={link}>
                        Подключиться
                      </a>
                    </span>
                  )}
                </>
              )}
            </>
          )}
        </div>

        {isExpandable &&
          <div onClick={() => setIsOpened(prevState => !prevState)}>
            {isOpened ? <FaChevronUp/> : <FaChevronDown/>}
          </div>
        }
      </div>
      {isExpandable && isOpened && (
        <>
          {desc && <div className="psycho-day-viewer__entry__main">
            <b>Описание:</b>
            <span>{desc}</span>
          </div>}
          {
            status === 'PLANNED' && <div className="psycho-day-viewer__entry__main">
              <SuiButton buttonType='secondary' onClick={() => setIsModal(true)}>Открыть консультацию</SuiButton>
            </div>
          }
          {appId && <Modal isOpen={isModal} onClose={() => setIsModal(false)}>
            <FeedbackPage appId={appId} close={() => setIsModal(false)}/>
          </Modal>}
        </>
      )}
    </div>
  );
};
