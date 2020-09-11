def call(def expected, def actual) {

  if (expected != actual) return

  def exMessage = "assertNotEqual: $expected != $actual"
  if (null != errorMessage) {      
    exMessage += " - ${errorMessage}"
  }

  throw new Exception(exMessage) 
}