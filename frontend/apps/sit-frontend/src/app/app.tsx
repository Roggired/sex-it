import './app.scss';
import { store } from 'apps/sit-frontend/src/app/api/store';
import { ClientApplicationViewerPage } from 'apps/sit-frontend/src/app/pages/client/application-viewer/application-viewer';
import { ClientApplicationsPage } from 'apps/sit-frontend/src/app/pages/client/client-applications/client-applications';
import { PsychoCalendar } from 'apps/sit-frontend/src/app/pages/client/psycho-calendar/psycho-calendar';
import { PsychoCardPage } from 'apps/sit-frontend/src/app/pages/client/psycho-card/psycho-card';
import { PsychoListPage } from 'apps/sit-frontend/src/app/pages/client/psycho-list/psycho-list-page';
import { PsychoSlotPage } from 'apps/sit-frontend/src/app/pages/client/psycho-slot/psycho-slot';
import { ApplicationViewPage } from 'apps/sit-frontend/src/app/pages/psycho/application-view/application-view-page';
import { ApplicationsPage } from 'apps/sit-frontend/src/app/pages/psycho/applications/applications-page';
import { CreateSlotPage } from 'apps/sit-frontend/src/app/pages/psycho/create-slot/create-slot-page';
import { PsychoDayViewer } from 'apps/sit-frontend/src/app/pages/psycho/psycho-slot-viewer/psycho-day-viewer';
import { Provider } from 'react-redux';
import {BrowserRouter, createBrowserRouter, RouterProvider, useRoutes} from 'react-router-dom';
import { Header } from './widgets/header/header';
import { RootPage } from './pages/root-page';
import { PsychoCreateProfile } from './pages/psycho/create-profile/psycho-create-profile';
import { CalendarPage } from './pages/psycho/calendar-page/calendar-page';
import {baseRoutes} from "./auth/base-routes";
import {routes} from "./utils/routes";

const router = createBrowserRouter([
  {
    path: '/',
    element: <RootPage />,
  },
  {
    path: '/psycho/create-profile',
    element: (
      <>
        <Header />
        <PsychoCreateProfile />
      </>
    ),
  },
  {
    path: '/psycho/calendar',
    element: (
      <>
        <Header />
        <CalendarPage />
      </>
    ),
  },
  {
    path: '/psycho/create-slot',
    element: (
      <>
        <Header />
        <CreateSlotPage />
      </>
    ),
  },
  {
    path: `/psycho/day/:month/:day`,
    element: (
      <>
        <Header />
        <PsychoDayViewer />
      </>
    ),
  },
  {
    path: '/psycho/applications',
    element: (
      <>
        <Header />
        <ApplicationsPage />
      </>
    ),
  },
  {
    path: '/psycho/applications/:appId',
    element: (
      <>
        <Header />
        <ApplicationViewPage />
      </>
    ),
  },
  {
    path: '/client/psycho-list',
    element: (
      <>
        <Header />
        <PsychoListPage />
      </>
    ),
  },
  {
    path: '/client/psycho-card/:id',
    element: (
      <>
        <Header />
        <PsychoCardPage />
      </>
    ),
  },
  {
    path: '/client/psycho-calendar/:id',
    element: (
      <>
        <Header />
        <PsychoCalendar />
      </>
    ),
  },
  {
    path: '/client/psycho-slot/:month/:day',
    element: (
      <>
        <Header />
        <PsychoSlotPage />
      </>
    ),
  },
  {
    path: '/client/applications',
    element: (
      <>
        <Header />
        <ClientApplicationsPage />
      </>
    ),
  },
  {
    path: '/client/application/:id',
    element: (
      <>
        <Header />
        <ClientApplicationViewerPage />
      </>
    ),
  },
]);

const Routing = () => {
  return useRoutes(
    baseRoutes({
      homeRoute: routes.toRoot(),
      header: <Header />,
      footer: <></>,
      appRoutes: [
        {
          path: '',
          element: <RootPage />,
        },
      ]
    })
  )
}

export function App() {
  return (
    <Provider store={store}>
      <BrowserRouter>
        <Routing />
      </BrowserRouter>
    </Provider>
  );
}

export default App;
