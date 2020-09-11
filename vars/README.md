# Custom Pipeline Steps
## `archivelog`
Finds the most recent log in a directory and archives it.
#### Usage:
```groovy
archivelog('/path/to/logs', '*-ERRORS.log', false)
```

#### logDir
The relative path to the directory with logs.
  
#### globPattern
  
The glob pattern used to find the latest log. (Optional, Default: '*-ERRORS.log')

#### archiveLog
Whether to archive the log or just return it. (Optional, Default: true)

## `argstr`
Converts dynamic amount of string arguments into a space separated string. Used for formatting shell arguments.
#### Usage:
```groovy
sh 'python myscript.py ' + argstr(
  '-b $BUILD_FLAG',
  '-c $CONFIG',
  '-p $PUBLISH_FLAG'
)
```

## `assertEqual`
An assertion method for testing. Evaluates if the two passed objects are equal. If not, it will throw a Java Exception. Takes an optional error message to display in the exception.
#### Usage:
```groovy
def myString = "pickles"
assertEqual("pickles", myString)
```
```groovy
def myString = "pickles"
assertEqual("pickles", myString, "myString should be pickles!")
```

## `assertNotEqual`
An assertion method for testing. Evaluates if the two passed objects are not equal. If the objects are equal, it will throw a Java Exception. Takes an optional error message to display in the exception.
#### Usage:
```groovy
def myString = "yams"
assertNotEqual("potatos", myString)
```
```groovy
def myString = "yams"
assertNotEqual("potatos", myString, "myString cannot be potatos.")
```

## `assertTrue`
An assertion method validating a boolean value/epxression as true. If not, it will throw a Java Exception. Takes an optional error message to display in the exception.
#### Usage:
```groovy
assertTrue(someValue == 23)
```
```groovy
assertTrue(someService.isActive())
```
```groovy
assertTrue(os == "windows", "Os must be windows")
```

## `buildUser`
Returns a string of the user who kicked off the current build. Requires the context (global var) of 'currentBuild'.
#### Usage:
```groovy
println buildUser() // $ tpain
```


## `clearQueue`
Clears builds that are the same as the calling one from the build queue.
#### Usage:
```groovy
clearQueue()
```


## `cred`
Wrapper around withCredentials for username/password. Overloaded so a user can specify vars for USERNAME and PASSWORD, or use the default environmental variables 'USERNAME' and 'PASSWORD'.
```groovy
cred('some-id') { 
  sh "python -u $USERNAME -p $PASSWORD" 
}
```
```groovy
cred('some-id', 'uname', 'pword') { 
  sh "python -u $uname -p $pword" 
}
```

## `credaws`
Wrapper around withCredentials step for AmazonWebServicesCredentialsBinding. Overloaded so a user can specify vars for AWS_SECRET_ACCESS_KEY and AWS_SECRET_ACCESS_KEY, or use the default environmental variables 'AWS_ACCESS_KEY_ID' and 'AWS_SECRET_ACCESS_KEY'.
```groovy
credaws('some-id') {  sh "python --id $AWS_ACCESS_KEY_ID --key $AWS_SECRET_ACCESS_KEY" }
```
```groovy
credaws('some-id', 'awsId', 'awsSecret') {  sh "python --id $awsId --key $awsSecret" }
```

## `credsecret`
Wrapper around withCredentials step for secret text. Overloaded so a user can specify a var for SECRET, or use the default environmental variable 'SECRET'.
```groovy
credsecret('some-id') { sh "python --api_key $SECRET" }
```
```groovy
credsecret('some-id', 'hushHush') { sh "python --api_key $hushHush" }
```

## `escapeShellArg`
Escapes special characters for string needed in a shell command.
```groovy
// input: i like "things" with '$stuff' in it
def input = params.COMMENT
// input: i like \"things\" with \'\$stuff\' in it
input = escapeShellString(input)
sh "echo $input" // prints i like "things" with '$stuff' in it
```


## `findFolders`
Returns a list of folder names from the current working directory.
#### Usage:
```groovy
def folders = findFolders() // retrieves all folders in current dir
```
```groovy
def folders = findFolders 'tools.*' // locates folders starting with 'tools'
```
#### pattern
Optional regex pattern for filtering on folder names. Default is all the things: .*  

## `findJobNames`
Returns a list of job names on the Jenkins master.
#### Usage:
```groovy
def jobs = findJobsNames() // retrieves all Jenkins job names.
```
```groovy
def folders = findJobNames('tools-*') // locates all jobs starting with 'tools-'
```
#### pattern
Optional regex pattern for filtering on job names. Default is all the things: .*  

## `formatTimestamp`
Returns a formatted timestamp string using the give timesteamp in millis and the provided format string.
#### Usage:
```groovy
formatTimeStamp(timeInMillis, "yyyy-MM-dd'T'HH:mm:ss.SSSZZ", 'UTC')
```

#### timeInMillis</code>
Timestamp in milliseconds.

#### format</code>
A string date format (see https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html).
  
#### timeZone</code>
Optional timezone string. Default is 'UTC'.


## `getOS`
Returns the OS of the node/agent/slave this call is invoked in. There are only three possible values to be returned: 'windows', 'mac', and 'linux'.
#### Usage:
```groovy
node ('win-small') {
  def os = getOS()
  println os // displays 'windows'
}
```


## `gitbranches`
Retrieves all git branches for the provided repo address as a list of strings.


## `isAdmin`
Returns whether the user who invoked the job is 'admin' or not. Admin list held within step source (isAdmin.groovy).
#### Usage:
```groovy
if (isAdmin()) { /* admin stuff */ }
```

## `jobNames`
Returns a list of job names from the Jenkins master.
#### Usage:
```groovy
println jobNames() // ['SomeJob', 'SomeOtherJob', ...]
println jobNames('Classic.*')  // ['ClassicMerge', 'Classic_Auto_Deploy', ...]
println jobNames('PR.*', '/')  // ['Some/Folder/Job/PR-16', '']
```

#### pattern
An optional regex pattern to filter on job names.

#### folderJoinString
An optional string which will be used when concatenating folder job-names.
```
Folder style job:       Classic -> game-starcraft -> master
After join string '_':  Classic_game-starcraft_master   
```
  


## `logger`
Static class for printing consistent and formatted log messages. The 'logger' class contains methods of each level for writing out the appropriate level-based message.
#### Usage:
```groovy
logger.trace(...)
logger.debug(...)
logger.info(...)
logger.warn(...)
logger.error(...)
```
#### Examples:
```groovy
logger.info('Some custom info message') // [INFO] 14:09:01 - Some custom info message
```
```groovy
logger.warn('Some custom warning message', 'myMethod') // [WARN] 14:09:01 - myMethod - Some custom warning message
```

#### message</code></dt>
The message to print.
  
#### module
Optional module string, such as the calling method, to be 
formatted to the log output.


## `nodeNames`
Returns a list of node names from the Jenkins master.
#### Usage:
```groovy
println nodeNames() // ['NODE1', 'NODE2', ...]
println nodeNames('USCGML-.*') // ['USCGML-BUILD001', 'USCGML-BUILD002', ...]
```

#### pattern
Optional regex pattern for filtering on job names. 
Default is all the things: `'.*'`
  


## `nodes`
Used like Pipeline step `node`, but takes in either a list OR a regex string of nodes to run on and will invoke the closure on all nodes in parallel.
__EXAMPLES:__
```groovy
// Across all small builders, checkout this repo
nodes('USCGMW-BLDSM.*') {
    git 'git@somerepo.com'
}
// For these two nodes, delete `peel.log` from the `banana` workspace
nodes(['USCGMW-BUILD157', 'USCGMW-BUILD157']) {
    ws('workspace/banana') {
        sh 'rm peel.log'
    }
}
```

## `on`
Utility step for performing an action "on" one or more targeted nodes. Currently, the only available action is `sh`.
__EXAMPLES:__
```groovy
// In batches of 10, for all small builders, delete this directory
on  regex: 'USCGMW-BLDSM0.*',
    sh: 'rm -rf D:/jenkins/workspace/test',
    batchSize: 10
// For these two builders, checkout and build a repo
on  nodes: ['USCGMW-BUILD157', 'USCGMW-BUILD157'],
    closure: {
        git 'git@somerepo.com'
        sh 'python build.py --config Debug'
    }
    '''
```
### `regex`
Regex of the nodes you want to target. Must either use this property or the `nodes` property.
### `nodes`
Explicit list of node names to target. Must either use this property or the `regex` property.
### `sh`
The shell command to invoke on all the targeted nodes.
### `closure`
A `groovy` closure to invoke on each node.
### `batchSize`
(Optional) Value indicating the batch size for execution (default is everything). A batch itself will be ran in parallel, while each batch will be ran in serial. Used for performance.

## `printnode`
A step that prints the node you are running on. Useful for blueocean view or custom build page view.
**NOTE:** This step requires a node context to work.
#### Usage:
```groovy
printnode()
```


## `setStageResult`
Sets the stage result, without modifying the overall build status, by wrapping `catchError`.
```groovy
setStageResult 'NOT_BUILT'
setStageResult 'FAILURE'
```

## `shext`
Extends the `sh` pipeline step, adding retries and retry condition as a part of the arg map.
#### Usage:
```groovy
shext   retries: 3,            // Will retry twice (not including first failed run)
        retryOnStatusCode: 127,
        label: 'Build client',
        script: 'python build.py -a x86_64 -c Debug'
        
```
      

#### `retries`
The number of "attempts" the script execution will make. If 3 is specified, it will re-run __twice__, and only on a failure.

#### `retryOnStatusCode`
The target exit code to retry on. If unspecified, the call will retry on non-zero.


## `stagenode`
stagenode functions
#### stagenode
Merges stage and node into one call and sends metrics about it.
Args:
    stageName name the stage
    nodeName which node to use
    closure what to execute
``` groovy
def targetNodeName = 'win-small'
timestamps {
    stagenode('When in Rome', targetNodeName) {
        assertTrue(env.NODE_NAME == targetNodeName)
        sh 'hostname && echo NODE_NAME'
    }
}
```


## `utils`
Support functions
#### utils.parseJson
Returns an object of the passed JSON string
Args:
    jsonString - A string representation of a JSON object
``` groovy
def myJsonString = '{ "home": " is where the hearth is"}'
def jsonData = utils.parseJson(myJsonString)
print jsonData.home
```

