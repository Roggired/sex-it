import { PropsWithChildren } from 'react';
import classNames from 'classnames';
import './sui-button.scss';

type SuiButtonProps = {
  readonly buttonType?: 'primary' | 'secondary';
  readonly onClick: () => void;
};

export const SuiButton = ({
  children,
  onClick,
  buttonType = 'primary',
}: PropsWithChildren<SuiButtonProps>) => {
  return (
    <button
      onClick={onClick}
      className={classNames('sui-button', {
        secondary: buttonType === 'secondary',
      })}
    >
      {children}
    </button>
  );
};
