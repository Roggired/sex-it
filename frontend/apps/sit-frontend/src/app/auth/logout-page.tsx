import React, { useEffect } from 'react'
import { getSSOLogoutUrl } from './routes'
import {clearTokens} from "./auth-cache";

export const LogoutPage = () => {
  useEffect(() => {
    clearTokens()
    window.location.href = getSSOLogoutUrl()
  }, [])
  return <></>
}
