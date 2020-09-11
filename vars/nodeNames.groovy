def call(String pattern = '.*') {
  // We 'collect' to ensure the list returned from a
  //  @NonCPS is a Jenkins Pipeline safe list (e.g. ArrayList)
  return getNodesString(';', pattern).split(';').collect { it }
}



/**
 * Returns a delimited list of nodes as a String.
 * @param  delimiter The delimiter string.
 * @param  pattern   Regex pattern to match node names against.
 * @return           A string of node names.
 */
@NonCPS
def getNodesString(String delimiter, String pattern) {
  def nodeList = ['master']
  for (Node node in Jenkins.instance.nodes) {
    if (!node.toComputer().online) { continue }

    if (node.name ==~ pattern) {
      nodeList.add(node.name)
    }
  }
  return nodeList.join(delimiter)
}
