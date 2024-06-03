import { PropsWithChildren } from 'react';
import './page.scss';
import classNames from 'classnames';

export const Page = ({
  children,
  center = false,
}: PropsWithChildren<{
  readonly center?: boolean;
}>) => {
  return (
    <div
      className={classNames('page', {
        center,
      })}
    >
      {children}
    </div>
  );
};
