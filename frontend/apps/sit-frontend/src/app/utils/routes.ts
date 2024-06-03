export const routes = {
  toRoot: () => '/',
  toBack: () => -1,

  toCreatePsychoPage: () => '/psycho/create-profile',
  toPsychoCalendarPage: () => '/psycho/calendar',
  toPsychoApplicationsPage: () => '/psycho/applications',
  toPsychoCreateSlot: () => '/psycho/create-slot',
  toPsychoDayViewer: (month: number, day: number) =>
    `/psycho/day/${month}/${day}`,
  toPsychoApplication: (appId: number) => `/psycho/applications/${appId}`,

  toClientPsychoList: () => '/client/psycho-list',
  toClientPsychoCard: (id: number) => `/client/psycho-card/${id}`,
  toClientPsychoCalendar: (id: number) => `/client/psycho-calendar/${id}`,
  toClientPsychoSlot: (id: number) => `/client/psycho-slot/${id}`,
  toClientApplications: () => `/client/applications`,
  toClientApplication: (id: number) => `/client/application/${id}`,
};
