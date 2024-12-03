import { InputHTMLAttributes, useId } from 'react';
import './sui-input.scss';
import classNames from "classnames";

type SuiInput = {
  readonly label?: string;
  readonly containerClass?: string;
} & InputHTMLAttributes<HTMLInputElement>;

export const SuiInput = ({ label, containerClass, ...rest }: SuiInput) => {
  const id = useId();
  return (
    <div className={classNames("sui-input", containerClass)}>
      {label && <label htmlFor={id}>{label}:</label>}
      <input id={id} {...rest} />
    </div>
  );
};
