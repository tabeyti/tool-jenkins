def call(def expected, def actual, def errorMessage = null) {

  if (expected == actual) return
  
  def exMessage = "assertEqual: $expected == $actual"
  if (null != errorMessage) {      
    exMessage += " - ${errorMessage}"
  }

  throw new Exception(exMessage)
}