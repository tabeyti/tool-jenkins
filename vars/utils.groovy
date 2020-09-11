import groovy.json.JsonSlurperClassic

def parseJson(jsonString) {
    def slurper = new JsonSlurperClassic()
    def result = slurper.parseText(jsonString)
    slurper = null
    return result
}

/**
 * Property/method that retrieves the current folder name.
 * NOTE: Must have a node context to work
 * @return The name of the current folder as a string.
 */
def getFolderName() {
    def path = pwd()
    if (path.contains('@')) {
        path = path.substring(0, path.lastIndexOf('@'))
    }
    def array = path.replace('\\', '/').split("/")
    return array[array.length - 1];
}


/**
 * Internal NonCPS method for setting a node in an offline status.
 * @param  nodeName The node name (case sensitive)
 * @param  message  The offline message
 */
@NonCPS
def markNodeOfflineInternal(String nodeName, String message) {
    def node = Jenkins.instance.getNode(nodeName)
    if (null == node) { return false }

    computer = node.toComputer()
    if (null == computer) { return false }

    computer.setTemporarilyOffline(true)
    computer.doChangeOfflineCause(message)
    computer = null
    node = null
    return true
}

/**
 * Public CPS method to be called for setting a node offline. Utilizes
 * system/NonCPS method for setting the status.
 * @param  nodeName The node name (case sensitive)
 * @param  message  The offline message
 */
def markNodeOffline(String nodeName, String message) {
    def result = markNodeOfflineInternal(nodeName, message)
    if (!result) {
        throw new Exception("Could not mark $nodeName OFFLINE")
    }
    sh script: "echo Marked $nodeName OFFLINE", label: "$nodeName OFFLINE"
}[]