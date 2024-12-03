import {useGetPsychoProfile} from "../../../hooks/useGetPsychoProfile";
import {applicationsApi} from "../../../api/applications/applications-api";
import './FeedbackPage.scss'
import {months} from "../../../utils/date-mapper";
import {useEffect, useState} from "react";
import {DayViewerEntry} from "../psycho-slot-viewer/day-viewer-entry";
import {SuiButton} from "../../../sui/sui-button/sui-button";

export const FeedbackPage = ({appId, close}: { appId: number, close: () => void }) => {
  const {id} = useGetPsychoProfile()
  const [note, setNote] = useState('')

  const {data: app} = applicationsApi.useGetAppByIdQuery(appId)

  const [updateNote] = applicationsApi.usePatchNoteMutation()
  const [finish] = applicationsApi.useFinishApplicationMutation()

  useEffect(() => {
    if (app) {
      setNote(app.notes ?? '')
    }
  }, [app]);

  if (!app) {
    return
  }

  console.log(app)

  return <div className="feed-page">
    <h1>{app.slot.dayId + 1} {months[app.slot.monthId]} 2024</h1>
    <DayViewerEntry time={app.slot.time} isAnon={app.anonType === 'ANON'}
                    isOffline={app.visitType === 'OFFLINE'} status={app.status}
                    link={app.link} address={app.address} />
    <div className="feed-page__r">
      <b>Описание:</b>
      <span>{app.description}</span>
    </div>
    <span>Заметка:</span>
    <textarea
      style={{
        resize: 'none',
        alignSelf: 'stretch'
      }}
      rows={10}
      value={note}
      onChange={(e) => setNote(e.target.value)}
    ></textarea>
    <div className="feed-page__r" style={{gap: '24px'}}>
      <SuiButton onClick={() => {
        updateNote({
          appId,
          note,
        })
      }}>Добавить заметку</SuiButton>
      <SuiButton onClick={() => {
        finish({
          appId,
          note,
        }).then(close)
      }}>Завершить</SuiButton>
    </div>
  </div>;
}
