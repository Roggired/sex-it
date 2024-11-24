import { psycho } from 'apps/sit-frontend/src/app/state/user-atom';
import { SuiButton } from 'apps/sit-frontend/src/app/sui/sui-button/sui-button';
import { routes } from 'apps/sit-frontend/src/app/utils/routes';
import { useNavigate } from 'react-router-dom';
import { CalendarContainer } from '../../../widgets/calendar/calendar-container';
import { Page } from '../../shared/page/page';
import './calendar-page.scss';
import { SlotPageChooser } from '../../shared/slot-page-chooser/slot-page-chooser';
import {useState} from "react";
import {SuiModal} from "../../../sui/modal/sui-modal";

export const CalendarPage = () => {
  const navigate = useNavigate();
  const [isOpened, setIsOpened] = useState(false)

  return (
    <Page>
      <div className="calendar-page">
        <SlotPageChooser />
        <CalendarContainer
          psychoId={psycho.id}
          slotMode="PSYCHO"
          onDayClick={(day, month) =>
            navigate(routes.toPsychoDayViewer(month, day))
          }
        />
        <SuiButton
          // onClick={() => navigate(routes.toPsychoCreateSlot())}
          onClick={() => setIsOpened(prev => !prev)}
          className="calendar-page__fab"
        >
          Новый слот
        </SuiButton>
        <SuiModal isOpen={isOpened} handleClose={() => {}}>
          <h1>HELLOOOOO</h1>
        </SuiModal>
      </div>
    </Page>
  );
};
