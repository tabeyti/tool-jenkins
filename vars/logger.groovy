def trace(String message, String module = null) {
  def pre = prefix('TRACE', module)
  println "${pre} - ${message}"
}

def debug(String message, String module = null) {
  def pre = prefix('DEBUG', module)
  println "${pre} - ${message}"
}

def info(String message, String module = null) {
  def pre = prefix('INFO', module)
  println "${pre} - ${message}"
}

def warn(String message, String module = null) {
  def pre = prefix('WARN', module)
  println "${pre} - ${message}"
}

def error(String message, String module = null) {
  def pre = prefix('ERROR', module)
  println "${pre} - ${message}"
}

/**
 * Helper method for generating the log-line prefix string.
 * @param  level  Targeted log level string.
 * @param  module Optional module name string.
 * @return        Prefix string.
 */
def prefix(String level, String module = null) {
  def timestamp = new Date().format("HH:mm:ss").toString()
  def prefix = "[${level}] ${timestamp}"
  if (null != module) {
    prefix += " >> ${module}"
  }
  return prefix
}