import './app.scss';
import {store} from 'apps/sit-frontend/src/app/api/store';
import {ApplicationsPage} from 'apps/sit-frontend/src/app/pages/psycho/applications/applications-page';
import {Provider} from 'react-redux';
import {BrowserRouter, RouteObject, useRoutes} from 'react-router-dom';
import {Header} from './widgets/header/header';
import {PsychoCreateProfile} from './pages/psycho/create-profile/psycho-create-profile';
import {CalendarPage} from './pages/psycho/calendar-page/calendar-page';
import {baseRoutes} from "./auth/base-routes";
import {routes} from "./utils/routes";
import {PsychoListPage} from "./pages/client/psycho-list/psycho-list-page";
import {PsychoCardPage} from "./pages/client/psycho-card/psycho-card";
import {PsychoCalendar} from "./pages/client/psycho-calendar/psycho-calendar";
import {ClientApplicationsPage} from "./pages/client/client-applications/client-applications";
import {SubscriptionPage} from "./pages/psycho/subscription/subscription-page";
import {AdminPage} from './pages/admin/AdminPage';
import {FriendCreateProfile} from './pages/friend/create-profile/friend-create-profile';
import {FriendshipPage} from './pages/friend/create-friendship/create-friendship-page';
import {FriendshipStatusForPsychoPage} from './pages/psycho/friendship/friendship';
import {FriendLinks} from "./pages/friend/links/friend-links";


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
    path: 'psycho/applications',
    element: (
      <>
        <ApplicationsPage/>
      </>
    ),
  },
  {
    path: 'psycho/subscription',
    element: (
      <>
        <SubscriptionPage/>
      </>
    ),
  },
{
    path: 'psycho/my-friend',
    element: (
      <>
        <FriendshipStatusForPsychoPage/>
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
    path: 'client/psycho-calendar/:psychoId',
    element: (
      <>
        <PsychoCalendar/>
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
]
const friendRoutes: RouteObject[] = [
  {
        path: 'friend/create-profile',
        element: (
          <>
            <FriendCreateProfile/>
          </>
        ),
      },
        {
          path: 'friend/create-friendship',
          element: (
            <>
              <FriendshipPage/>
            </>
          ),
        },
      {
        path: 'friend/friendship',
        element: (
          <>
            <FriendshipPage/>
          </>
          ),
        },
  {
    path: 'friend/links',
    element: <FriendLinks />
  }
  ]

const adminRoutes: Array<RouteObject> = [
  {
    path: 'admin',
    element: <AdminPage />
  }
]

const Routing = () => {
  return useRoutes(
    baseRoutes({
      homeRoute: routes.toRoot(),
      header: <Header/>,
      footer: <></>,
      appRoutes: [
        ...psychoRoutes,
        ...clientRoutes,
        ...adminRoutes,
        ...friendRoutes
      ],
    })
  )
}

export function App() {
  return (
    <Provider store={store}>
      <BrowserRouter basename={process.env.NX_REACT_ROUTER_BASE_URL}>
        <Routing/>
      </BrowserRouter>
    </Provider>
  );
}

export default App;
