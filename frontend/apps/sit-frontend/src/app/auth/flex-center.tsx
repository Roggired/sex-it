import React, {HTMLAttributes, PropsWithChildren} from 'react'

export const FlexCenter = ({
                             children,
                             ...rest
                           }: PropsWithChildren<HTMLAttributes<HTMLDivElement>>) => {
  return (
    <div
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
      }}
      {...rest}
    >
      {children}
    </div>
  )
}
