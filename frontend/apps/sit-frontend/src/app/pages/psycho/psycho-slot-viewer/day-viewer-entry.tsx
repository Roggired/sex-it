import { SlotStatus } from 'apps/sit-frontend/src/app/api/slot/model';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import classNames from 'classnames';
import { useNavigate } from 'react-router-dom';

type DayViewerEntryProps = {
  readonly time: string;
  readonly isAnon: boolean;
  readonly isOffline: boolean;
  readonly status: SlotStatus;
  readonly desc?: string;
  readonly link?: string;
  readonly address?: string;
  readonly onDelete?: () => void;
};
export const DayViewerEntry = ({
  status,
  address,
  link,
  desc,
  isAnon,
  isOffline,
  time,
  onDelete,
}: DayViewerEntryProps) => {
  const navigate = useNavigate();
  return (
    <div className="psycho-day-viewer__entry">
      <div className="psycho-day-viewer__entry__header">
        <b>{time}</b>
        <div
          className={classNames('psycho-day-viewer__entry__header__block', {
            planned: status !== 'EMPTY',
          })}
        >
          {status === 'EMPTY' && (
            <span>
              Свободно
              <SuiButton
                style={{ marginLeft: '16px', padding: '6px' }}
                onClick={onDelete}
                buttonType="secondary"
              >
                Удалить
              </SuiButton>
            </span>
          )}
          {status === 'NEED_REVIEW' && (
            <span
              style={{ cursor: 'pointer' }}
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
                <span>
                  Онлайн:{' '}
                  <a style={{ cursor: 'pointer' }} href={link}>
                    Подключиться
                  </a>
                </span>
              )}
            </>
          )}
        </div>
      </div>
      {desc && (
        <div className="psycho-day-viewer__entry__main">
          <b>Описание:</b>
          <span>{desc}</span>
        </div>
      )}
    </div>
  );
};
