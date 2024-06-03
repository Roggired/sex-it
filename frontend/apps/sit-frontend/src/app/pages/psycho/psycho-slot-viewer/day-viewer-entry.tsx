import { SlotStatus } from 'apps/sit-frontend/src/app/api/slot/model';
import classNames from 'classnames';

type DayViewerEntryProps = {
  readonly time: string;
  readonly isAnon: boolean;
  readonly isOffline: boolean;
  readonly status: SlotStatus;
  readonly desc?: string;
  readonly link?: string;
  readonly address?: string;
};
export const DayViewerEntry = ({
  status,
  address,
  link,
  desc,
  isAnon,
  isOffline,
  time,
}: DayViewerEntryProps) => {
  return (
    <div className="psycho-day-viewer__entry">
      <div className="psycho-day-viewer__entry__header">
        <b>{time}</b>
        <div
          className={classNames('psycho-day-viewer__entry__header__block', {
            planned: status === 'PLANNED',
          })}
        >
          {status === 'EMPTY' && 'Свободно'}
          {status === 'PLANNED' && (
            <>
              <span>{isAnon ? 'Анонимная консультация' : 'Консультация'}</span>
              {isOffline ? (
                <span>{address}</span>
              ) : (
                <span>
                  Онлайн: <a href={link}>Подключиться</a>
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
