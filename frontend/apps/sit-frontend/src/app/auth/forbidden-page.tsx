import { useEffect, useState } from 'react'
import { getSSOLogoutUrl } from './routes'
import {clearTokens} from "./auth-cache";
import {FlexCenter} from "./flex-center";

export const ForbiddenPage = () => {
  const [remainedTime, setRemainedTime] = useState(5)

  useEffect(() => {
    clearTokens()

    const interval = setInterval(
      () => setRemainedTime((prev) => prev - 1),
      1000
    )

    return () => clearInterval(interval)
  }, [])

  useEffect(() => {
    if (remainedTime === 0) {
      window.location.replace(getSSOLogoutUrl())
    }
  }, [remainedTime])

  return (
    <FlexCenter>
      <span>
        Не хватает прав для просмотра страницы. Перенаправление на экран
        авторизации через: {remainedTime}
      </span>
    </FlexCenter>
  )
}
