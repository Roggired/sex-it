import React, {type PropsWithChildren, ReactNode} from 'react'
import './base-page.scss'

export type PageTemplateProps = {
  readonly header?: ReactNode
  readonly footer?: ReactNode
}

export const BasePage = ({
                           children,
                           header,
                           footer,
                         }: PropsWithChildren<PageTemplateProps>) => {
  return (
    <div className="page-container">
      {header}
      {children}
      <div id="modal-root"></div>
      {footer}
    </div>
  )
}
