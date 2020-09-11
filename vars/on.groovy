def call(Map args) {

  // Verify node targets
  if (!args.regex && !args.nodes) {
    throw new Exception("Must provide a 'node' list or node 'regex'.")
  }

  // Can be expanded to be other operations, such as 'bat'
  if (!args.sh && !args.closure) {
    throw new Exception("Must provide 'sh' or 'closure'.")
  }

  // Set default batch size as unlimited
  if (!args.batchSize) {
    args.batchSize = 1000
  }
  def batchList = []  

  def nodes = args.regex ? nodeNames(args.regex) : args.nodes
  nodes.eachWithIndex { n, index ->

    // Create new parTasks list for every X elements
    if (0 == (index % args.batchSize)) {
      batchList.add([:])
    }

    def parTasks = batchList.last()
    def label = "${batchList.size()}: ${n}"
    parTasks[label] = {
      stage(label) {
        node(n) {
          if (args.sh) {
              sh args.sh
          }
          if (args.closure) {
              args.closure.call()
          }
        }
      }
    }
    println "Number of batches: ${batchList.size()}"
  
    batchList.eachWithIndex { p, i ->
      println "Executing batch #${i+1}"
      parallel p
    }
  }
}
