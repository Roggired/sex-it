
// export const SSO_URL = process.env['NX_SSO_URL']
export const SSO_URL = 'http://localhost:31505/api/v1/sso/'

export const getSSOLoginUrl = () =>
  `${SSO_URL}authorize?redirectUri=${window.location.origin}/sso/token`

export const getSSOLogoutUrl = () =>
  `${SSO_URL}logout?redirectUri=${window.location.origin}`
