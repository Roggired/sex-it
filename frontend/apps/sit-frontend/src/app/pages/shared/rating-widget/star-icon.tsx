import {PropsWithChildren} from "react";
import classNames from "classnames";
import "./star-icon.scss";

export const StarIcon = ({ isFilled = false }: PropsWithChildren<{ readonly isFilled?: boolean}>) => {
  return (
    <svg className={classNames("star-icon", { "star-icon-filled": isFilled })} width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path
        d="M12.5631 5.87349L10.1225 5.51884L9.03099 3.30727C8.60941 2.453 7.39036 2.45326 6.96893 3.30724L5.87744 5.51881L3.43682 5.87349C2.49402 6.01046 2.11769 7.16986 2.79962 7.83464L4.56567 9.55611L4.14877 11.9868C3.98775 12.9258 4.97402 13.642 5.81703 13.1989L7.99999 12.0512L10.183 13.1989C11.0248 13.6414 12.0122 12.9256 11.8512 11.9868L11.4343 9.55608L13.2004 7.83461C13.8825 7.1696 13.5055 6.01043 12.5631 5.87349Z"
        fill="#A6AEBF"/>
    </svg>
  )
}
