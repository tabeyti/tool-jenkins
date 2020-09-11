def call(String pattern = '.*') {
  def output = sh returnStdout:true, script: """\
    set -e
    ls -1 -d */ | cut -f1 -d'/'"""
  def allFolders = output.split('\n').collect { it }

  def folders = []
  allFolders.each {
    if (it ==~ pattern) {
      folders.add(it)
    }
  }
  return folders
}