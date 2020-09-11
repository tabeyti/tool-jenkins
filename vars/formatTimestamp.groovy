String call(long timeInMillis, String format, String timeZone = 'UTC') {

  if (null == format) {
    throw new Exception("formatTimeSteamp - format cannot be null.")
  }
  return formatTimeStampInternal(timeInMillis, format, timeZone)
}

/**
 * Formats the given timestamp, with the provided format.
 * @param  timeInMillis [description]
 * @param  format       [description]
 * @param  timeZone     [description]
 * @return              [description]
 */
@NonCPS
private static String formatTimeStampInternal(long timeInMillis, String format, String timeZone) {
  return new Date(timeInMillis).format(format, TimeZone.getTimeZone(timeZone))
}