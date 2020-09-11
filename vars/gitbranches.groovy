def call(repoAddress) {
  node('win-small') {
    def out = sh returnStdout: true, script: "git ls-remote --heads $repoAddress"
    def lines = out.split("\n")
    def branches = []
    for (def l in lines) {
      def m = l =~ /.*refs\/heads\/(.*)/
      branches.add(m[0][1])
    }
    return branches
  }
}
