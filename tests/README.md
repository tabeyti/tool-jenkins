# Shared Library Tests

Integrations tests validating the shared library steps/classes. The tests are invoked via the top level Jenkinsfile (currently Jenkinsfile.pr).

## Authoring Format
To allow our top level Jenkinsfile to discover and invoke our tests, there is a structure the test file and containing methods require:

|Target|Format|
|-------|-------|
|File Name|[step_name]Tests.groovy|
|Test Method|[step_name]Tests_[what_this_is_testing]|

### Example
Let's use an imaginary pipeline step called **getOS**, which returns a string of the OS it's called on:
```shell
/vars/getOS.groovy
```

The associated test file should look like this:
```shell
/tests/getOSTests.groovy
```

Let's now say we are wanting to test that the step returns 'windows' when called under a Windows agent. Our method would look something like this:
```groovy
def getOSTests_Windows() {

  node('SOME_WINDOWS_LABEL') {

    def os = getOS()

    assertEqual("windows", os) // assertEqual is a pre-defined step in /vars
  }
}
return this
```
Here we called the method we are wanting test and utilized one of the assert methods to validate its return value.

**IMPORTANT!!**

Notice the ```return this``` below the test method. **Every test file should have ```return this``` at the end of the file.** If this is missing, no instance of the script is passed back to the test harness.
