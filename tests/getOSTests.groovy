/**
 * Validates a proper OS string for mac node.
 * @return [description]
 */
def getOSTests_Mac() {
  node ('Mac || mac') {
    assertEqual('mac', getOS())
  }
}

/**
 * Validates a proper OS string for linux node.
 * @return [description]
 */
def getOSTests_Linux() {
  node ('cloud-builder') {
    assertEqual('linux', getOS())
  }
}

/**
 * Validates a proper OS string for Windows node.
 * @return [description]
 */
def getOSTests_Windows() {
  node ('win-small') {
    assertEqual('windows', getOS())
  }
}

/**
 * Validates a failure case.
 * @return [description]
 */
def getOSTests_Failure() {
  node('win-small') {
    assertNotEqual('mac', getOS())
  }
}

return this
