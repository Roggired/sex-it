import React, { useEffect } from 'react'
import {getSSOLoginUrl} from "./routes";

export const RouteGuard = () => {
  useEffect(() => {
    if (window.location.href.includes("referralId")) {
      window.localStorage.setItem("LINK", window.location.href)
    }
    window.location.replace(getSSOLoginUrl());
  }, [])
  return <></>
}
