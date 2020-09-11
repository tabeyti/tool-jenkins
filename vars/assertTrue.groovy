def call(def boolVal, def errorMessage = null) {

  if (boolVal) return 

  def exMessage = "assertTrue: ${boolVal}"
  if (null != errorMessage) {      
    exMessage += " - ${errorMessage}"
  }
  
  throw new Exception(exMessage)
  
}