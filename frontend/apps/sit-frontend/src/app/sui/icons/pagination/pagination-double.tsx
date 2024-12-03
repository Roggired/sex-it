import classNames from "classnames";
import "./pagination-icon.scss"

export interface PaginationDoubleProps {
  readonly enabled: boolean
  readonly isRightRotated?: boolean
  readonly onClick: () => void
}

export const PaginationDouble = ({enabled, onClick, isRightRotated}: PaginationDoubleProps) => {
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
              d="M11.7496 5.263C12.2671 5.67701 12.3511 6.43216 11.937 6.94968L7.73675 12.2L11.937 17.4504C12.3511 17.9679 12.2671 18.7231 11.7496 19.1371C11.2321 19.5511 10.477 19.4672 10.063 18.9497L5.26296 12.9497C4.91235 12.5114 4.91235 11.8887 5.26296 11.4504L10.063 5.45041C10.477 4.9329 11.2321 4.84899 11.7496 5.263Z"
              fill="#808080"/>
        <path fillRule="evenodd" clipRule="evenodd"
              d="M18.7496 5.263C19.2671 5.67701 19.3511 6.43216 18.937 6.94968L14.7367 12.2L18.937 17.4504C19.3511 17.9679 19.2671 18.7231 18.7496 19.1371C18.2321 19.5511 17.477 19.4672 17.063 18.9497L12.263 12.9497C11.9123 12.5114 11.9123 11.8887 12.263 11.4504L17.063 5.45041C17.477 4.9329 18.2321 4.84899 18.7496 5.263Z"
              fill="#808080"/>
      </g>
    </svg>
  )
}
