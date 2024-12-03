import classNames from "classnames";
import "./pagination-icon.scss"

export interface PaginationSingleProps {
  readonly enabled: boolean
  readonly isRightRotated?: boolean
  readonly onClick: () => void
}

export const PaginationSingle = ({enabled, onClick, isRightRotated}: PaginationSingleProps) => {
  return (
    <svg className={
      classNames("pagination-icon", {
        "pagination-icon-disabled": !enabled,
        "pagination-icon-rotated": isRightRotated
      })
    }
         width="24"
         height="24"
         viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" onClick={onClick}>
      <g opacity="0.4">
        <path fillRule="evenodd" clipRule="evenodd"
              d="M15.1496 5.06299C15.6671 5.477 15.751 6.23215 15.337 6.74967L11.1367 12L15.337 17.2504C15.751 17.7679 15.6671 18.5231 15.1496 18.9371C14.6321 19.3511 13.877 19.2672 13.4629 18.7497L8.66295 12.7497C8.31234 12.3114 8.31234 11.6887 8.66295 11.2504L13.4629 5.2504C13.877 4.73288 14.6321 4.64898 15.1496 5.06299Z"
              fill="#808080"/>
      </g>
    </svg>
  )
}
