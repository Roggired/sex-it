export const getRuStringFromDate = (date: Date, isMinute = true) => {
  return isMinute
    ? `${date.toLocaleDateString('ru-RU')} ${date.toLocaleTimeString('ru-RU', {
      hour: '2-digit',
      minute: '2-digit',
    })}`
    : `${date.toLocaleDateString('ru-RU')} ${date.toLocaleTimeString('ru-RU', {
      hour: '2-digit',
    })}`.slice(0, -2)
}

export const getUTCStringFromRuString = (ruDateString: string) => {
  const date = ruDateString.split(' ')[0].split('.')
  return `${date[2]}-${date[1]}-${date[0]} ${ruDateString.split(' ')[1]}`
}

export const getUTCNewDateFromRuString = (ruDateString: string) =>
  new Date(getUTCStringFromRuString(ruDateString))

export const convertUTCDateToLocalDate = (date: Date) => {
  return new Date(date.getTime() - date.getTimezoneOffset() * 60 * 1000)
}

export const stringToDateFormat = (data: string) => {
  return convertUTCDateToLocalDate(new Date(data)).toLocaleDateString('ru-RU')
}
