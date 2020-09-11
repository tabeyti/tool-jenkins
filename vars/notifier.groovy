import org.classic.*

def call(Map args) {
  if (null == args.recips) {
    throw new Exception("notifier - Must provide a space separated string of recipients.")
  }

  def subject = "${currentBuild.currentResult}: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}"

  args.startDateTimeStr = new Date(currentBuild.startTimeInMillis).toString()
  args.durationString = currentBuild.durationString
  args.buildUrl = env.BUILD_URL
  args.job = env.JOB_NAME
  args.log = null // for now
  args.params = params
  args.result = currentBuild.currentResult

  def body = generateEmailBody(args)

  emailext subject: subject,
      mimeType: 'text/html',
      to: args.recips,
      body: body
}

/**
 * Generates the html for an email.
 */
def generateEmailBody(Map args) {
  def jobName = args.job
  def buildUrl = args.buildUrl
  def startDateTimeStr = args.startDateTimeStr
  def durationString = args.durationString
  def failedTasksMap = args.failures
  def logText = args.log
  def params = args.params
  def result = args.result

  def eh = new EmailHtml()
  def emailHtml = eh.EMAIL_HTML_STYLE

  def title = ""
  def color = ""
  if (result == 'FAILURE') {
    title = 'Failure(s)'
    color = 'red'
  } else if (result == 'ABORTED') {
    title = 'Aborted'
    color = 'blue'
  } else {
    title = 'Success'
    color = 'green'
  }

  emailHtml += eh.createJobHeaderTableHtml(
    title,
    color,
    jobName,
    buildUrl,
    result,
    startDateTimeStr,
    durationString)

  // Display failed tasks if any
  if ('FAILURE' == result && null != failedTasksMap && !failedTasksMap.isEmpty()) {
    def failureList = []
    failedTasksMap.each { t -> failureList.add([t.key, t.value]) }
    emailHtml += eh.createMultiColumnTableHtml(["Task", "Exception"], failureList) + "<br>"
  }

  // Display build parameters if any
  if (params != null && !params.isEmpty()) {
    def paramList = []
    params.each { p -> paramList.add([p.key, p.value]) }
    emailHtml += eh.createMultiColumnTableHtml(['Build Param', 'Value'], paramList) + '<br>'
  }

  if ('FAILURE' == result) {
    emailHtml += eh.createSingleColumnTableHtml("Parsed Errors", null)
    def parsedErrors = '''
        <pre>${BUILD_LOG_REGEX, regex=" error |WindowsError:|(E|e)rror:(?!  Texture not found)", linesBefore=0, linesAfter=2, showTruncatedLines=false, escapeHtml=true}</pre>
      '''
    // Only display the first ~4000 characters (50 lines, 80 characters each)
    emailHtml += parsedErrors.take(4000) + "<br>"
  }

  // Display changes if any
  commitInfo = showChangeLogs().trim()
  if (commitInfo) {
    emailHtml += eh.createSingleColumnTableHtml("Related Change Sets (if available)", null)
    emailHtml += "<p>${commitInfo}</p>"
    emailHtml += "<br>"
  }

  // Display log text if any
  if (null != logText) {
    emailHtml += eh.createSingleColumnTableHtml("Log", null)
    emailHtml += "<pre class='console-output'>${logText}</pre>"
  }

  return emailHtml
}

/**
 * Retrieves html formatted changes for the current build.
 * @return An html formatted list of changes.
 */
@NonCPS
def showChangeLogs() {
  commitInfo =""
  def changeLogSets = currentBuild.rawBuild.changeSets
  for (int i = 0; i < changeLogSets.size(); i++) {
    def entries = changeLogSets[i].items
    for (int j = 0; j < entries.length; j++) {
      def entry = entries[j]
      if (j == 0 || entries[j] != entries[j-1]){
        commitInfo += """<p><u>Revision:</u> ${entry.commitId} by [${entry.author}] on ${new Date(entry.timestamp)}<br/>
        <span style="padding-left: 20px;">- commit message: ${entry.msg}</span></p>
        """
      }
    }
  }
  return commitInfo
}