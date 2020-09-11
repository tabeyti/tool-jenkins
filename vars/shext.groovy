def call(Map args) {

    // Grab additional args then remove them from map
    // so it can be passed to 'sh'
    def retries = args.retries ?: 1
    def retryOn = args.retryOnStatusCode
    args.remove('retries')
    args.remove('retryOnStatusCode')

    // Set return status to true so we don't default
    // throw an exception during shell execution
    def hasReturnStatus = args.returnStatus ?: false
    args.returnStatus = true

    def lastStatusCode = 0
    def retryCount = 1

    println "retries: $retries"
    retry(retries) {
        lastStatusCode = sh args

        // If no errors or we are on the last iteration of retries, leave
        if (0 == lastStatusCode || retryCount == retries) { return }

        // If retyOn was given and we don't match, leave
        if (null != retryOn && lastStatusCode != retryOn) { println "YAY"; return }

        retryCount++
        err message: "Status code: $lastStatusCode", 
            label: "Failed shell $args.label",
            statusCode: lastStatusCode
    }

    if (hasReturnStatus) {
        return lastStatusCode
    }

    if (lastStatusCode != 0) {
        err message: "Status code: $lastStatusCode",
            label: "Failed shell $args.label",
            statusCode: lastStatusCode
    }
}

// TODO: Should/could be moved into it's own step
def err(Map args) {
    def message = args.message ?: 'error signal'
    def statusCode = args.statusCode ?: 1
    def label = args.label ?: 'error signal'
    sh  script: "echo $message && exit $statusCode",
        label: label
}