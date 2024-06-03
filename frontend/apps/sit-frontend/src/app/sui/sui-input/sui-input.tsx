import { useId } from 'react';
import './sui-input.scss';

type SuiInput = {
  readonly label: string;
};

export const SuiInput = ({ label }: SuiInput) => {
  const id = useId();
  return (
    <div className="sui-input">
      <label htmlFor={id}>{label}</label>
      <input id={id} type="text" />
    </div>
  );
};
