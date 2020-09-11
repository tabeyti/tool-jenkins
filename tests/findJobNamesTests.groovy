/**
 * Smoke test
 * @return [description]
 */
def findJobNamesTests_Smoke_Exist() {
  node {
    def allJobs = findJobNames()
    assertNotEqual(0, allJobs.size())

    // Search for this job within the results
    def found = false
    allJobs.each { j ->
      if (j.contains(env.JOB_BASE_NAME)) {
        found = true
      }
    }
    assertTrue(found, "Could not locate job base name ${env.JOB_BASE_NAME}")
  }
}

return this