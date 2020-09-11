def call(String[] nodes, Closure closure) {
    def parTasks = [:]
    nodes.each { n ->
        parTasks[n] = {
            stage(n) {
                node(n) {
                    closure()
                }
            }
        }
    }
    parallel parTasks
}


def call(List nodes, Closure closure) {
    def parTasks = [:]
    nodes.each { n ->
        parTasks[n] = {
            stage(n) {
                node(n) {
                    closure()
                }
            }
        }
    }
    parallel parTasks
}

def call(String regex, Closure closure) {
    def nodes = nodeNames(regex)
    call(nodes, closure)
}