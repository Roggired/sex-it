import {useState} from "react";
import {linksApi, LinkView} from "../../../api/links/linksApi";

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
        <List data={accepted?.content}/> : <List data={paid?.content}/>
    }
  </div>
}

const List = ({data}: { data?: LinkView[] }) => {
  if (!data) return

  return <></>
}
