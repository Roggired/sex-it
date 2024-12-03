import {AcceptedApplication} from "../../../api/applications/model";
import {applicationsApi} from "../../../api/applications/applications-api";
import {skipToken} from "@reduxjs/toolkit/query";
import {months} from "../../../utils/date-mapper";
import {SuiButton} from "../../../sui/sui-button/sui-button";
import {RatingWidget} from "../../shared/rating-widget/rating-widget";
import {FaChevronLeft, FaChevronRight} from "react-icons/fa6";
import {useState} from "react";
import {feedA} from "../../../api/feedbach/feedbackApi";

export const ClientFeed = ({appId, close, aapp} : {
  appId: number
  close: () => void
  aapp: AcceptedApplication
}) => {
  const {data} = applicationsApi.useGetAppByIdQuery(appId ?? skipToken);
  const [rat, setRat] = useState(3)
  const [text, setText] = useState('')

  const [rate] = feedA.useGiveFeedbackMutation()

  if (!data) {
    return
  }



  return <div className="application-view">
    <h1>
      {data.slot.dayId + 1} {months[data.slot.monthId]} 2024
    </h1>
    <b>{aapp.psycho.name}</b>
    <span>Цена: {aapp.psycho.price} руб.</span>
    <span>Анонимно: {aapp.anonType === 'ANON' ? 'Да' : 'Нет'}</span>
    <span>Дистанционно: {aapp.visitType === 'ONLINE' ? 'Да' : 'Нет'}</span>
    <div style={{display: "flex", flexDirection: 'column'}}>
      <b>Итоги:</b>
      <span>{data.notes}</span>
    </div>
    <div style={{display: 'flex', gap: '8px'}}>
      <span>Оцените:    </span>
      <FaChevronLeft onClick={() => setRat(rat === 0 ? rat : rat - 1)}/>
      <RatingWidget rating={rat} />
      <FaChevronRight onClick={() => setRat(rat === 5 ? rat : rat + 1)}/>
    </div>
    <span>Анонимный отзыв</span>
    <textarea
      style={{
        alignSelf: 'stretch',
        resize: 'none'
      }}
      value={text}
      onChange={e => setText(e.target.value)}
    />
    <SuiButton onClick={() => {
      rate({
        appId,
        rating: rat,
        text,
        }
      ).then(close)
    }}>Оценить</SuiButton>
  </div>;
}
