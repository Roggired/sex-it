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
};
