def call(body) {
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  def par = [:]
  for (int i = 0; i < config.combinations; i++) {
    def c = combinations[0]
    node (c.SLAVE) {
      config.closure()
    }    
  }
}


