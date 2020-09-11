/**
 * Retrieves a FileWrapper object for the most recently modified
 * log in the provided directory and archives it.
 * NOTE: Assumes execution is at the Job workspace root.
 * @param  logDir The relative path to the directory with logs.
 * @param  globPattern The glob pattern used to find the latest log.
 * @param  archiveLog Whether to archive the log or just return it.
 * @return        The relative path string to the archived log. Null if no 
 *                log could be found.
 */
def call(String logDir, String globPattern = '*-ERRORS.log', Boolean archiveLog = true) {
  def log = null
  dir(logDir) {
    def files = findFiles glob: globPattern
    log = files.find { f -> new Date(f.lastModified) > new Date(currentBuild.startTimeInMillis) }
  }

  if (null == log) {
    println "WARNING: Could not locate recent log file in: $logDir"
    return null
  }

  def logFilePath = "${logDir}/${log.name}"  
  if (archiveLog) {
    println "Archiving: ${logFilePath}"
    archive logFilePath
  }
  return logFilePath
}