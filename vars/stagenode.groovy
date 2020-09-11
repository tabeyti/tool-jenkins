import org.classic.*

/**
* Merges stage and node into one call and sends metrics about it.
* @param stageName name the stage
* @param nodeName which node to use
* @param closure what to execute
*/
def call(String stageName, String nodeName, Closure closure) {
    node(nodeName) {
        stage(stageName) {
            printnode()
            closure()
        }
    }
}
