import { ButtonHTMLAttributes, PropsWithChildren } from 'react';
import classNames from 'classnames';
import './sui-button.scss';

type SuiButtonProps = {
  readonly buttonType?: 'primary' | 'secondary';
} & ButtonHTMLAttributes<HTMLButtonElement>;

export const SuiButton = ({
  children,
  onClick,
  buttonType = 'primary',
  className,
  ...rest
}: PropsWithChildren<SuiButtonProps>) => {
  return (
    <button
      onClick={onClick}
      className={classNames('sui-button', className, {
        secondary: buttonType === 'secondary',
      })}
      {...rest}
    >
      {children}
    </button>
  );
};
