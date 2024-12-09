import React, {useEffect} from 'react'
import {useNavigate, useSearchParams} from 'react-router-dom'
import {Authorization, saveAuthorization} from "./auth-cache";
import {authService} from "../api/auth-service";
import {FlexCenter} from "./flex-center";
import {parseRoleFromJWT, Role} from "./role";
import {routes} from "../utils/routes";

/**
 * Страница, куда редиректимся после успешной авторизации в SSO
 */
export const TokenPage = ({ redirectRoute }: { redirectRoute: string }) => {
  const [searchParams, _] = useSearchParams()
  const navigate = useNavigate()

  const [tokenExchangeTrigger, { isLoading: isExchangingInProgress }] =
    authService.useTokenExchangeMutation()

  useEffect(() => {
    const code = searchParams.get('code')
    const state = searchParams.get('state')

    if (code && !isExchangingInProgress) {
      tokenExchangeTrigger({
        state: state,
        authorizationCode: code,
        // redirectUri: `${process.env['NX_FRONTEND_URL']}/sso/token`,
        redirectUri: `http://localhost:3000/sso/token`,
      })
        .unwrap()
        .then((data: Authorization) => {
          saveAuthorization(data)
          const role = parseRoleFromJWT(data.accessToken)
          if (role?.[0] === Role.PSYCHO) {
            navigate(routes.toPsychoCalendarPage())
          } else if (role?.[0] === Role.CLIENT) {
            navigate(routes.toClientPsychoList())
          } else if (role?.[0] === Role.ADMIN) {
            navigate(routes.toAdmin())
          } else if (role?.[0] === Role.PSYCHO_FRIEND) {
            navigate(routes.toCreateFriendPage())
          } else {
            navigate(routes.toRoot())
          }
        })
    }
  }, [searchParams])

  return (
    isExchangingInProgress && (
      <FlexCenter>
      </FlexCenter>
    )
  )
}
