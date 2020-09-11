def call() {
  def out = sh returnStdout:true, script: 'echo $OSTYPE'
  if (out.contains("msys")) {
    return "windows"
  }
  if (out.contains("darwin")) {
    return "mac"
  }
  if (out.contains("linux")) {
    return "linux"
  }
  return out
}