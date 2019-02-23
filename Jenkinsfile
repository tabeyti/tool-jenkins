@Library('tools')_

bocs {
  failureRecipients 'tabeyti@gmail.com'
  task('windowsTask') {
    label 'windows10-1'
    shell 'python build.py -t ddd.log'
    retry 2
  }
}