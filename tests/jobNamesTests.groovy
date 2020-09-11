/**
 * Validates bare bones call with no args.
 * @return [description]
 */
def jobNamesTests_Smoke() {
  node('master') {
    def list = jobNames()
    assertTrue(list.size() > 0, 'Received no jobs.')
  }
}

/**
 * Validates pattern usage. We use a pattern related to PR
 * jobs as these tests will only run during a PR job.....
 * .......this is going to break down the line, isn't it?
 * @return [description]
 */
def jobNamesTests_Pattern_Smoke() {
  node('master') {
    def pattern = '.*PR-.*'
    def list = jobNames(pattern)
    assertTrue(list.size() > 0, 'Received no jobs.')
    assertTrue(list.every { it ==~ pattern }, "Jobs returned not matching pattern ${pattern}")
  }
}

/**
 * Validates the folder joining string for Jenkins folder
 * type jobs.
 * @return [description]
 */
def jobNamesTests_FolderJoinString_Smoke() {
  node('master') {
    def folderJoinString = '&&&'
    def list = jobNames(null, folderJoinString)
    assertTrue(list.size() > 0, 'Received no jobs.')
    assertTrue(list.any { it.contains(folderJoinString) }, "No folder job names contain join string '${folderJoinString}'.")
  }
}

return this