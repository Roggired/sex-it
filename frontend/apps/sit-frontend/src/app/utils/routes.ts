export const routes = {
  toRoot: () => '/sexit',
  toBack: () => -1,
  toLogout: () => '/sso/logout',

  toCreatePsychoPage: () => '/sexit/psycho/create-profile',
  toPsychoCalendarPage: () => '/sexit/psycho/calendar',
  toPsychoSubscriptionPage: () => '/sexit/psycho/subscription',
  toPsychoApplicationsPage: () => '/sexit/psycho/applications',
  toPsychoDayViewer: (month: number, day: number) =>
    `/sexit/psycho/day/${month}/${day}`,
  toPsychoApplication: (appId: number) => `/sexit/psycho/applications/${appId}`,

  toClientPsychoList: () => '/sexit/client/psycho-list',
  toClientPsychoCard: (id: number) => `/sexit/client/psycho-card/${id}`,
  toClientPsychoCalendar: (id: number) => `/sexit/client/psycho-calendar/${id}`,
  toClientPsychoSlot: (month: number, day: number) =>
    `/sexit/client/psycho-slot/${month}/${day}`,
  toClientApplications: () => `/sexit/client/applications`,
  toClientApplication: (id: number) => `/sexit/client/application/${id}`,
};
