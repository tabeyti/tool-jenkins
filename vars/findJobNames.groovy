def call(String pattern = null) {
  // We use 'collect' to ensure the list returned from @NonCPS
  // is a Pipeline safe list (e.g. ArrayList).
  return getJobsString(';', pattern).split(';').collect { it }
}

/**
 * Builds a list of job names. When encountered with folder
 * jobs, it will build the job name using the folder path.
 * @param  items List of jenkins jobs/projects
 * @param  path  Current folder path for this call.
 * @param  pattern Optional regex to filter on job names.
 * @return       A list of job name strings.
 */
@NonCPS
def getJobsRecursively(List items, String path, String pattern = null) {
  def jobs = []
  items.each { item ->
    // If this is a folder job type, recurse until we get the actual job.
    if (item instanceof com.cloudbees.hudson.plugins.folder.AbstractFolder) {
      if (path == "") {
        jobs.addAll(getJobsRecursively(item.items, item.name, pattern))
      }
      else {
        jobs.addAll(getJobsRecursively(item.items, "${path}_${item.name}", pattern))
      }
      return
    }

    // If item is not buildable, it's either disabled or a closed PR build and
    // we don't care about it.
    if (!item.isBuildable()) {
      return []
    }

    // If a pattern was specified and the job's name doesn't match it, return.
    if (null != pattern && !(item.name =~ pattern)) {
      return []
    }

    if (path == "") {
      jobs.add(item.name)
    }
    else {
      jobs.add("${path}_${item.name}")
    }
  }
  return jobs
}

/**
 * Returns a string of job names, using the provided delimiter
 * for separating job names.
 *
 * This is NonCPS because of 'Jenkins.instance' usage.
 * @param  delimiter [description]
 * @param  null      [description]
 * @return           [description]
 */
@NonCPS
def getJobsString(String delimiter, String pattern = null) {
  def foundItems =  getJobsRecursively(Jenkins.instance.items, "", pattern)
  return foundItems.join(delimiter)
}