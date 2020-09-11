/**
 * Smoke test validating we are receiving a list nodes.
 * @return [description]
 */
def nodeNamesTests_Smoke() {
  node('master') {
    def nodes = nodeNames()
    assertTrue(nodes != null)
    assertTrue(nodes.size() > 0)
  }
}

/**
 * Smoke test for regex filtering on node names.
 * @return [description]
 */
def nodeNamesTests_RegexSmoke() {
  node('master') {
    def allNodes = nodeNames()
    assertTrue(allNodes != null)
    assertTrue(allNodes.size() > 0)

    def someNodes = nodeNames('USCGMW-.*')
    assertTrue(someNodes != null)
    assertTrue(someNodes.size() > 0)
    assertTrue(someNodes.size() < allNodes.size())
  }
}

/**
 * Validates step usage outside node scope.
 * @return [description]
 */
def nodeNamesTests_OutsideNodeScope() {
  def nodes = nodeNames()
  assertTrue(nodes != null)
  assertTrue(nodes.size() > 0)
}

return this