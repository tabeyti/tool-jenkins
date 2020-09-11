/**
 * Validates retry by writing out a file, appending a symbol on each retry,
 * then verifying what was written matches expected.
 */
def shextTests_retry_smoke() {
    def final file = 'out'
    def final retries = 3
    def final symbol = '+'
    def final expected = symbol * retries

    node('master') {
        // Remove test file if it exists
        sh 'rm out || true'
        try {
            shext   label: "test-step",
                    retries: retries,
                    script: "`echo -n 1 >> $file` && exit 1"

        } catch (Exception e) {

            def content = readFile('out').trim()
            assertTrue(content == expected, "File content doesn't match expected ($expected) value: $content")
            return
        }
        assertTrue(false, "Should not have reached here.")
    }
}

/**
 * Validates retry on status code by writing content to a file
 * on the 2nd retry and then validating that content exists.
 * @return
 */
def shextTests_retry_onStatusCode() {
    def final file = 'out'
    def final key = 'somekeymadebymeteeheehee'
    def final exitCode = 123

    node('master') {
        // Remove test file if it exists
        sh 'rm out || true'
        try {            
            shext   label: "test-step",
                    retries: 2,
                    retryOnStatusCode: exitCode,
                    script: """
                        # If file doesn't exist, create it and exit with failure
                        if [ ! -f "$file" ]; then
                            echo "" > $file
                            exit $exitCode
                        # Otherwise, write key to it to validate later, and exist with failure
                        else
                            echo $key >> $file
                            exit 1
                        fi
                    """
        } catch (Exception e) {
            def content = readFile('out').trim()
            assertTrue(content == key, "File content doesn't match expected ($key) value: $content")
            return
        }
        assertTrue(false, "Should not have reached here.")
    }
}

/**
 * Validates retry on status code with the incorrect status
 * @return
 */
def shextTests_negative_retry_onStatusCode() {
    def final file = 'out'
    def final key = '-'
    def final exitCode = 123
    def final expectedExitCode = 124

    node('master') {
        // Remove test file if it exists
        sh 'rm out || true'
        try {
            shext   label: "test-step",
                    retries: 2,
                    retryOnExitCode: expectedExitCode,
                    script: """
                        # If file doesn't exist, create it and exit with failure
                        if [ ! -f "$file" ]; then
                            echo "" > $file
                            exit $exitCode
                        # Otherwise, write key to it to validate later, and exist with failure
                        else
                            echo $key >> $file
                            exit 1
                        fi
                    """
        } catch (Exception e) {
            def content = readFile('out').trim()
            assertTrue(content != key, "File content doesn't match expected ($key) value: $content")
            return
        }
        assertTrue(false, "Should not have reached here.")
    }
}

/**
 * Validates retries with status code returning to the user
 * @return
 */
def shextTests_retry_withStatusCode() {
    def final file = 'out'
    def final key = '-'
    def final retries = 2
    def final exitCode = 123
    def final expectedValue = key*retries

    node('master') {
        // Remove test file if it exists
        sh 'rm out || true'

        def status = shext  label: "test-step",
                            retries: retries,
                            returnStatus: true,
                            script: """
                                # Write/append to file for validation later
                                echo -n $key >> $file
                                cat $file || true
                                exit $exitCode
                            """
        def content = readFile('out').trim()
        assertTrue(content == expectedValue, "File content doesn't match expected ($expectedValue) value: $content")
        assertTrue(status == exitCode, "Exit code $status did not match expected $exitCode")
    }
}

return true