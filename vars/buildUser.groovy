def call() {
  return getBuildUser()
}

/**
 * Internal method to retrieve the user information.
 * If there is no user-information, we assume
 * it's an automated job and return 'auto'.
 *
 * NOTE: We don't put this in the 'call' body as it is
 * NonCPS.
 * @return User ID string. E.G. tabeyti
 */
@NonCPS
def getBuildUser() {
  def userCause = currentBuild.rawBuild.getCause(Cause.UserIdCause)
  if (null != userCause) {
    return userCause.getUserId()
  }
  return 'auto'
}