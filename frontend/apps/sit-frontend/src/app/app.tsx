import './app.scss';
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
    element: <PsychoCreateProfile />,
  },
  {
    path: '/psycho/calendar',
    element: <CalendarPage />,
  },
]);

export function App() {
  return (
    <div className="app">
      <Header />
      <RouterProvider router={router} />
    </div>
  );
}

export default App;
