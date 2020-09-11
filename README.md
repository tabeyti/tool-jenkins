# Jenkins Tools

A Jenkins Shared Library housing handy steps for Pipeline.

## Setup
Run `setup.sh` before using this repo to set up the necessary `pre-commit` hook for git.

The `pre-commit` script added to your `.git/hooks` folder will automatically update/add `vars/README.md` whenever a commit is modifying any of the `vars/*.txt` files.

## Repo Structure
|Type|Directory|Description|
|-------|----------|----------|
|Library Steps|```./vars/**```|A collection of Pipeline steps to be used in pipeline scripts.|
|Library Step Tests|```./tests/**```|Integration tests for library steps.|
|Scripts|```./scripts/**```|Utility scripts for updating README docs and setting pre-commit hook.|

For detailed documentation on existing steps, such as call signatures and usage, see the steps [vars/README.md](vars/README.md).

#### Using Share Library
```java
// Imports steps
// The "tools" alias for this steps repo is set in Jenkins->Configure
@Library('tools')_
...
stagenode('my stage', 'master') { // stagenode is a library step
  ...
  sh 'python supercool.py -b --input data.txt
}
```

#### Testing Your Step
Tests are held in the `./tests` directory. To author tests, see the [tests/README.md](tests/README.md).
