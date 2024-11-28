import React, { ReactNode } from 'react'
import { Navigate, Outlet, RouteObject } from 'react-router-dom'
import {Realm} from "./realms";
import {TokenPage} from "./token-page";
import {ForbiddenPage} from "./forbidden-page";
import {hasRealm, isAuthorized} from "./auth-cache";
import {FlexCenter} from "./flex-center";
import {RouteGuard} from "./route-guard";
import {LogoutPage} from "./logout-page";
import {BasePage} from "./base-page";

export type NDRouteObject = RouteObject & {
  readonly realmGuard?: Realm
}

type BaseRoutesProps = {
  readonly homeRoute: string
  readonly appRoutes: NDRouteObject[]
  readonly header: ReactNode
  readonly footer: ReactNode
}

export const baseRoutes = ({
                             homeRoute,
                             appRoutes,
                             header,
                             footer,
                           }: BaseRoutesProps): RouteObject[] => [
  {
    path: '/',
    element: (
      <FlexCenter>
        <Outlet />
      </FlexCenter>
    ),
    children: [
      {
        path: '/',
        element: <RouteGuard />,
      },
      {
        path: '/sso/token',
        element: <TokenPage redirectRoute={homeRoute} />,
      },
      {
        path: '/sso/logout',
        element: <LogoutPage />,
      },
    ],
  },
  {
    path: '/sexit',
    element: isAuthorized() ? (
      <BasePage header={header} footer={footer}>
        <Outlet />
      </BasePage>
    ) : (
      <RouteGuard />
    ),
    children: appRoutes.map((route) => ({
      ...route,
      element: route.realmGuard ? (
        hasRealm(route.realmGuard) ? (
          route.element
        ) : (
          <Navigate to="/forbidden" />
        )
      ) : (
        route.element
      ),
    })),
  },
  {
    path: '/forbidden',
    element: <ForbiddenPage />,
  },
  {
    path: '*',
    element: <div>Error page</div>,
  },
]

export const logoutHeaderOption = {
  url: () => '/sso/logout',
  isAbsolute: false,
  text: 'Выйти',
}

export const personalDataHeaderOption = {
  url: () => process.env['NX_KEYCLOACK_URL'] ?? '',
  isAbsolute: true,
  useNewWindow: true,
  text: 'Персональные данные',
}
