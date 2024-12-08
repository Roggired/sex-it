import './admin-page.scss'
import {adminApi} from "../../api/admin/adminApi";
import {useEffect, useState} from "react";
import {SuiInput} from "../../sui/sui-input/sui-input";
import {SuiButton} from "../../sui/sui-button/sui-button";

export const AdminPage = () => {
  return <div className="admin-page">
    <h2>Статистика психологов и подписок</h2>
    <Stat1 />
    <h2>Статистика online консультаций</h2>
    <Stat2 />
  </div>
}

const Stat1 = () => {
  const {data, refetch} = adminApi.useGetSubStatsQuery();

  useEffect(() => {
    refetch()
  }, []);

  if (!data) {
    return
  }

  return <div style={{display: 'flex', flexDirection: 'column', alignSelf: 'flex-start', gap:'8px'}}>
    <b>Всего психологов: {data.totalPsychos}</b>
    <span>Пробная подписка: {data.freeSubscriptions}</span>
    <span>Базовая подписка: {data.basicSubscriptions}</span>
    <span>Premium подписка {data.proSubscriptions}</span>
  </div>;
}

const Stat2 = () => {
  const [start, setStart] = useState<string | undefined>(undefined)
  const [end, setEnd] = useState<string | undefined>(undefined)

  const [getData, { data } ] = adminApi.useGetConsultStatsMutation()

  return <>
    <div style={{display:'flex', gap:'16px', alignItems: 'flex-end'}} >
      <SuiInput value={start} onChange={e => setStart(e.target.value)} label="Период с" type="date" />
      <SuiInput value={end} onChange={e => setEnd(e.target.value)} label="Период до" type="date" />
      <SuiButton onClick={() => {
        if (!end || !start) {
          alert("Введите оба периода")
          return
        }

        getData({
          from: new Date(start).toISOString(),
          to: new Date(end).toISOString()
        })
      }}>Показать</SuiButton>
    </div>
    {data && <span style={{alignSelf: 'start', marginTop: '16px'}}>Проведено консультаций в указанный период: {data.numberOfOnlinePerformed}</span>}
  </>
}
