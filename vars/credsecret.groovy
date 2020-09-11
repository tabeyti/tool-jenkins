def call(credId, secretVar, closure) {
  try {
    withCredentials([[$class: 'StringBinding', credentialsId: credId, variable: secretVar]]) {
      closure()
    }
  }
  catch (Exception e) {
    def module = 'credsecret'
    logger.error("${e.class} - ${e.message}", module)

    /// We print additional error information for credential ID issues.
    if (e.class == org.jenkinsci.plugins.credentialsbinding.impl.CredentialNotFoundException) {
      def credsUri = env.JENKINS_URL ? "${env.JENKINS_URL}credentials" : "<JENKINS_HOME>/${credentials}"
      def message = "Could not locate credential id '${credId}' for StringBinding (a.k.a secret). Please see ${credsUri} to ensure that ID exists."
      logger.error(message, module)
    }
    throw e
  }
}

def call(credId, closure) {
  call(credId, 'SECRET', closure)
}