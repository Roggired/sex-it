import { MoonLoader } from 'react-spinners'

export type SuiLoaderProps = {
  readonly size?: 'BIG'
  readonly className?: string
}

const mapper = {
  BIG: 50,
}

export const SuiLoader = ({ size = 'BIG', className }: SuiLoaderProps) => {
  return (
    <MoonLoader className={className} color="#C50000" size={mapper[size]} />
  )
}
