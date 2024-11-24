import React, { useEffect } from 'react'
import {getSSOLoginUrl} from "./routes";

export const RouteGuard = () => {
  useEffect(() => {
    window.location.replace(getSSOLoginUrl())
  }, [])
  return <></>
}
