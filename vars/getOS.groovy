def call() {
  def out = sh returnStdout:true, script: 'uname'
  out = out.toLowerCase()
  if (out.contains("msys")) {
    return "windows"
  }
  if (out.contains("darwin")) {
    return "mac"
  }
  if (out.contains("linux")) {
    return "linux"
  }
  return null
}