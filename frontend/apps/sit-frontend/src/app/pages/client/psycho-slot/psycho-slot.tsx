import './psycho-slot.scss';
import {applicationsApi} from 'apps/sit-frontend/src/app/api/applications/applications-api';
import {psychoProfileApi} from 'apps/sit-frontend/src/app/api/psycho/psycho-profile-api';
import {slotApi} from 'apps/sit-frontend/src/app/api/slot/slot-api';
import {useGetNumberPathParam} from 'apps/sit-frontend/src/app/hooks/useGetNumberPathParam';
import {SuiButton} from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import {months} from 'apps/sit-frontend/src/app/utils/date-mapper';
import {routes} from 'apps/sit-frontend/src/app/utils/routes';
import {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {skipToken} from "@reduxjs/toolkit/query";

export const PsychoSlotPage = ({
  month, day, psychoId
                               }: {
  month: number
  day: number
  psychoId: number
}) => {
  const navigate = useNavigate();

  const [isAnon, setIsAnon] = useState(false);
  const [slotId, setSlotId] = useState(0);
  const [desc, setDesc] = useState('');
  const [isOnline, setIsOnline] = useState(false)

  const [createApp] = applicationsApi.useCreateApplicationMutation();

  const {data: p} = psychoProfileApi.useGetPsychoQuery(psychoId ? {
    id: psychoId,
    mode: 'CLIENT',
  } : skipToken);

  const {data} = slotApi.useGetSlotsByDayQuery(
    {
      psychoId: psychoId as number,
      dayId: (day as number) - 1,
      mode: 'CLIENT',
      yearId: 2024,
      monthId: month as number,
    },
    {
      skip: !day || !month || !psychoId,
    }
  );

  useEffect(() => {
    if (data && data.length) {
      const slot = data[0];
      setSlotId(slot.id);
    }
  }, [data]);

  if (!month || !day || !p) {
    return <></>;
  }

  const handleClick = () => {
    if (!slotId || !desc) {
      alert('Введите все поля!');
      return;
    }

    createApp({
      slotId,
      description: desc,
      visitType: isOnline ? 'ONLINE' : 'OFFLINE',
      anonType: isAnon ? 'ANON' : 'NE_ANON',
    })
      .unwrap()
      .then(() => psychoId && navigate(routes.toClientPsychoCalendar(psychoId)));
  };

  return (
    <div className="psycho-slot">
      <h1>
        {day} {months[month]} 2024
      </h1>
      <b>{p.name}</b>
      <span>Цена: {p.price} руб.</span>
      <span>Первая консультация бесплатна: {p.isFirstFree ? 'Da' : 'Net'}</span>
      <div>
        Анонимно:{' '}
        <input
          type="checkbox"
          checked={isAnon}
          onChange={(e) => setIsAnon(e.target.checked)}
        />
      </div>
      <div>
        Встреча онлайн?:{' '}
        <input
          type="checkbox"
          checked={isOnline}
          onChange={(e) => setIsOnline(e.target.checked)}
        />
      </div>
      <select onChange={(e) => setSlotId(+e.target.value)}>
        {(data ?? [])
          .filter((d) => d.status === 'EMPTY')
          .map((d) => (
            <option key={d.id} value={d.id}>
              {d.time}
            </option>
          ))}
      </select>
      <span>Описание проблемы:</span>
      <textarea
        rows={10}
        value={desc}
        onChange={(e) => setDesc(e.target.value)}
      ></textarea>
      <div className="psycho-slot__btns">
        <SuiButton onClick={handleClick}>Записаться</SuiButton>
        <SuiButton
          buttonType="secondary"
          onClick={() => navigate(routes.toBack())}
        >
          Закрыть
        </SuiButton>
      </div>
    </div>
  );
};
