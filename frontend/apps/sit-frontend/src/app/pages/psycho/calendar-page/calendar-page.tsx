import {routes} from 'apps/sit-frontend/src/app/utils/routes';
import {useNavigate} from 'react-router-dom';
import {CalendarContainer} from '../../../widgets/calendar/calendar-container';
import {Page} from '../../shared/page/page';
import './calendar-page.scss';
import {SlotPageChooser} from '../../shared/slot-page-chooser/slot-page-chooser';
import {useState} from "react";
import {Modal} from "../../../sui/modal/sui-modal";
import {useGetPsychoProfile} from "../../../hooks/useGetPsychoProfile";
import {IoMdAdd} from "react-icons/io";
import {CreateSlotPage} from "../create-slot/create-slot-page";

export const CalendarPage = () => {
  const navigate = useNavigate();
  const [isOpened, setIsOpened] = useState(false)
  const {id} = useGetPsychoProfile()

  if (!id) {
    return
  }

  return (
    <Page>
      <div className="calendar-page">
        <SlotPageChooser/>
        <CalendarContainer
          psychoId={id}
          slotMode="PSYCHO"
          onDayClick={(day, month) =>
            navigate(routes.toPsychoDayViewer(month, day))
          }
        />
      </div>
      <IoMdAdd onClick={() => setIsOpened(prev => !prev)} className="calendar-page__fab"/>
      <Modal isOpen={isOpened} onClose={() => setIsOpened(false)}>
        <CreateSlotPage onDone={() => setIsOpened(false)}/>
      </Modal>
    </Page>
  );
};
