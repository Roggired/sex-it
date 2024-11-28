import './app.scss';
import {store} from 'apps/sit-frontend/src/app/api/store';
import {ApplicationViewPage} from 'apps/sit-frontend/src/app/pages/psycho/application-view/application-view-page';
import {ApplicationsPage} from 'apps/sit-frontend/src/app/pages/psycho/applications/applications-page';
import {CreateSlotPage} from 'apps/sit-frontend/src/app/pages/psycho/create-slot/create-slot-page';
import {PsychoDayViewer} from 'apps/sit-frontend/src/app/pages/psycho/psycho-slot-viewer/psycho-day-viewer';
import {Provider} from 'react-redux';
import {BrowserRouter, createBrowserRouter, RouteObject, RouterProvider, useRoutes} from 'react-router-dom';
import {Header} from './widgets/header/header';
import {PsychoCreateProfile} from './pages/psycho/create-profile/psycho-create-profile';
import {CalendarPage} from './pages/psycho/calendar-page/calendar-page';
import {baseRoutes} from "./auth/base-routes";
import {routes} from "./utils/routes";
import {useUserRoles} from "./auth/role";
import {PsychoListPage} from "./pages/client/psycho-list/psycho-list-page";
import {PsychoCardPage} from "./pages/client/psycho-card/psycho-card";
import {PsychoCalendar} from "./pages/client/psycho-calendar/psycho-calendar";
import {PsychoSlotPage} from "./pages/client/psycho-slot/psycho-slot";
import {ClientApplicationsPage} from "./pages/client/client-applications/client-applications";
import {ClientApplicationViewerPage} from "./pages/client/application-viewer/application-viewer";

const psychoRoutes: RouteObject[] = [
  {
    path: 'psycho/create-profile',
    element: (
      <>
        <PsychoCreateProfile/>
      </>
    ),
  },
  {
    path: 'psycho/calendar',
    element: (
      <>
        <CalendarPage/>
      </>
    ),
  },
  {
    path: 'psycho/create-slot',
    element: (
      <>
        <CreateSlotPage/>
      </>
    ),
  },
  {
    path: `psycho/day/:month/:day`,
    element: (
      <>
        <PsychoDayViewer/>
      </>
    ),
  },
  {
    path: 'psycho/applications',
    element: (
      <>
        <ApplicationsPage/>
      </>
    ),
  },
  {
    path: 'psycho/applications/:appId',
    element: (
      <>
        <ApplicationViewPage/>
      </>
    ),
  },
]

const clientRoutes: RouteObject[] = [
  {
    path: 'client/psycho-list',
    element: (
      <>
        <PsychoListPage/>
      </>
    ),
  },
  {
    path: 'client/psycho-card/:id',
    element: (
      <>
        <PsychoCardPage/>
      </>
    ),
  },
  {
    path: 'client/psycho-calendar/:id',
    element: (
      <>
        <PsychoCalendar/>
      </>
    ),
  },
  {
    path: 'client/psycho-slot/:month/:day',
    element: (
      <>
        <PsychoSlotPage/>
      </>
    ),
  },
  {
    path: 'client/applications',
    element: (
      <>
        <ClientApplicationsPage/>
      </>
    ),
  },
  {
    path: 'client/application/:id',
    element: (
      <>
        <ClientApplicationViewerPage/>
      </>
    ),
  },
]

const Routing = () => {
  const {isPsycho, isClient} = useUserRoles()

  return useRoutes(
    baseRoutes({
      homeRoute: routes.toRoot(),
      header: <Header/>,
      footer: <></>,
      appRoutes: isPsycho ? psychoRoutes : clientRoutes,
    })
  )
}

export function App() {
  return (
    <Provider store={store}>
      <BrowserRouter>
        <Routing/>
      </BrowserRouter>
    </Provider>
  );
}

export default App;
