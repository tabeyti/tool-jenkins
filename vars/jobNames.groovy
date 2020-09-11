@Library('tools')_
import hudson.model.*;
import hudson.util.*;

def call(String pattern = null, String folderJoinString = '/') {
  // We 'collect' to ensure the list returned from a @NonCPS
  // method is a Jenkins Pipeline safe list (e.g. ArrayList)
  def delimiter = '~'
  return getJobsString(delimiter, pattern, folderJoinString).split(delimiter).collect { it }
}

/**
 * Returns a string of job names, using the provided delimiter
 * for separating job names.
 *
 * @NonCPS because of the need for passing Jenkins.instance.items.
 * @param  delimiter Delimiter for joining the job name list.
 * @param  pattern   Regex pattern, used for filtering jobs.
 * @return           A delimited string of jobs names.
 */
@NonCPS
def getJobsString(String delimiter, String pattern, String folderJoinString) {
  def foundItems =  getJobsRecursively(Jenkins.instance.items, '', pattern, folderJoinString)
  return foundItems.join(delimiter)
}

/**
 * Builds a list of job names. When encountered with folder jobs,
 * it will build the job name as the folder path using the folder join
 * string provided.
 *
 * @NonCPS because it iterates over internal Jenkins types.
 * @param  items List of jenkins jobs/projects
 * @param  path  Current folder path for this call.
 * @param  pattern Optional regex to whitelist jobs to add.
 * @return       A list of job name strings.
 */
@NonCPS
def getJobsRecursively(ArrayList items, String path, String pattern, String folderJoinString) {
  def jobs = []
  items.each { item ->
    // If this is a folder job type, recurse until we get the actual job.
    if (item instanceof com.cloudbees.hudson.plugins.folder.AbstractFolder) {
      if (path == "") {
        jobs.addAll(getJobsRecursively(item.items, item.name, pattern, folderJoinString))
      }
      else {
        jobs.addAll(getJobsRecursively(item.items, "${path}${folderJoinString}${item.name}", pattern, folderJoinString))
      }
      return
    }

    // If a pattern was specified and the job name doesn't match it, return.
    if (null != pattern && !(item.name =~ pattern)) {
      return []
    }

    if (path == '') {
      return jobs.add(item.name)
    }
    else {
      return jobs.add("${path}${folderJoinString}${item.name}")
    }
  }
  return jobs
}