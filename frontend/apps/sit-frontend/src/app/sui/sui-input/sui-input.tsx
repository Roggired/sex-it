import { InputHTMLAttributes, useId } from 'react';
import './sui-input.scss';

type SuiInput = {
  readonly label?: string;
} & InputHTMLAttributes<HTMLInputElement>;

export const SuiInput = ({ label, ...rest }: SuiInput) => {
  const id = useId();
  return (
    <div className="sui-input">
      {label && <label htmlFor={id}>{label}</label>}
      <input id={id} {...rest} />
    </div>
  );
};
