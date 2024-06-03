import './app.scss';
import { store } from 'apps/sit-frontend/src/app/api/store';
import { PsychoListPage } from 'apps/sit-frontend/src/app/pages/client/psycho-list/psycho-list-page';
import { ApplicationViewPage } from 'apps/sit-frontend/src/app/pages/psycho/application-view/application-view-page';
import { ApplicationsPage } from 'apps/sit-frontend/src/app/pages/psycho/applications/applications-page';
import { CreateSlotPage } from 'apps/sit-frontend/src/app/pages/psycho/create-slot/create-slot-page';
import { PsychoDayViewer } from 'apps/sit-frontend/src/app/pages/psycho/psycho-slot-viewer/psycho-day-viewer';
import { Provider } from 'react-redux';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { Header } from './widgets/header/header';
import { RootPage } from './pages/root-page';
import { PsychoCreateProfile } from './pages/psycho/create-profile/psycho-create-profile';
import { CalendarPage } from './pages/psycho/calendar-page/calendar-page';

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
]);

export function App() {
  return (
    <Provider store={store}>
      <div className="app">
        <RouterProvider router={router} />
      </div>
    </Provider>
  );
}

export default App;
