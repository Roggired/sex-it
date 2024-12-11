import {useState} from "react";
import {linksApi, LinkView} from "../../../api/links/linksApi";
import {SuiButton} from "../../../sui/sui-button/sui-button";

export const FriendLinks = () => {
  const [currFilter, setCurrFilter] = useState<
    'ACTIVE' | 'ACCEPTED' | 'PAYED'
  >('ACTIVE');

  const {data: active} = linksApi.useGetMyLinksQuery()
  const {data: accepted} = linksApi.useGetMyLinksAcceptedQuery()
  const {data: paid} = linksApi.useGetMyLinksPaidQuery()

  return <div className="client-applications">
    <div className="client-applications__breadcrumbs">
      <span className={currFilter === 'ACTIVE' ? 'chosen' : ''} onClick={() => setCurrFilter('ACTIVE')}>Активные</span>
      <span className={currFilter === 'ACCEPTED' ? 'chosen' : ''}
            onClick={() => setCurrFilter('ACCEPTED')}>/ Примененные</span>
      <span className={currFilter === 'PAYED' ? 'chosen' : ''}
            onClick={() => setCurrFilter('PAYED')}>/ Оплаченные</span>
    </div>
    {
      currFilter === 'ACTIVE' ? <List data={active?.content}/> : currFilter === 'ACCEPTED' ?
        <List data={accepted?.content} button/> : <List data={paid?.content}/>
    }
  </div>
}

const List = ({data, button = false}: {
  data?: LinkView[]
  button?: boolean
}) => {
  const arr = data ?? []
  const [pay] = linksApi.usePayByLinkMutation()

  const count = arr.reduce((accumulator, value) => {
    return {
      ...accumulator,
      [value.name]: {
        // @ts-expect-error
        count: (accumulator[value.name]?.count || 0) + 1,
        id: value.id
      },

    };
  }, {});

  console.log(count)

  return (Object.keys(count)).map(d => <div
    style={{border: '1px solid black', display: 'flex', justifyContent: 'space-between', padding: '10px', borderRadius: '4px', alignItems: 'center'}}>
    {/*// @ts-ignore*/}
    <span>{d} x{count[d].count}</span>
    {/*// @ts-ignore*/}
    {button && <SuiButton onClick={() => pay(count[d].id)}>Оплачено</SuiButton>}
  </div>)
}
