/**
 * Validates the calling of trace for the logger class.
 * @return [description]
 */
def loggerTests_Trace_Smoke() {
  logger.trace('test message1')
  logger.trace('test message2', 'testModule')
}

/**
 * Validates the calling of debug for the logger class.
 * @return [description]
 */
def loggerTests_Debug_Smoke() {
  logger.debug('test message1')
  logger.debug('test message2', 'testModule')
}

/**
 * Validates the calling of info for the logger class.
 * @return [description]
 */
def loggerTests_Info_Smoke() {
  logger.info('test message1')
  logger.info('test message2', 'testModule')
}

/**
 * Validates the calling of warn for the logger class.
 * @return [description]
 */
def loggerTests_Warn_Smoke() {
  logger.warn('test message1')
  logger.warn('test message2', 'testModule')
}

/**
 * Validates the calling of error for the logger class.
 * @return [description]
 */
def loggerTests_Error_Smoke() {
  logger.error('test message1')
  logger.error('test message2', 'testModule')
}

return this