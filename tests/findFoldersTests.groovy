/**
 * Validates generic find on temp dir.
 * @return [description]
 */
def findFoldersTests_Smoke_Exist() {
  node {
    def tempDir = tempDirName()
    createTempDir(tempDir)
    def folders = findFolders()
    deleteTempDir(tempDir)
    assertNotEqual(0, folders.size())
    assertTrue(folders.contains(tempDir))
  }
}

/**
 * Validates folder not found on dir that doesn't exist.
 * @return [description]
 */
def findFoldersTests_Smoke_DoesntExist() {
  node {
    def tempDir = tempDirName()
    def folders = findFolders()
    assertTrue(!folders.contains(tempDir))
  }
}

/**
 * Validates find on a folder pattern.
 * @return [description]
 */
def findFoldersTests_Pattern() {
  node {
    def pattern = ".*-PR.*"
    def tempDir = tempDirName() + pattern
    createTempDir(tempDir)
    def folders = findFolders(pattern)
    deleteTempDir(tempDir)
    assertTrue(folders.contains(tempDir))
  }
}

///////////////////////////////////////////////////////////////////////////////
// Helpers
///////////////////////////////////////////////////////////////////////////////

def tempDirName() {
  return UUID.randomUUID().toString().take(4)
}

def createTempDir(String tempDir) {
  sh "mkdir -p ${tempDir}"
}

def deleteTempDir(String tempDir) {
  sh "rm -rf ${tempDir}"
}

return this