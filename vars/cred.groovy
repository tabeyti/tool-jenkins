def call(credId, usernameVar, passwordVar, closure) {
  try {
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: credId,
                    usernameVariable: usernameVar, passwordVariable: passwordVar]]) {
      closure();
    }
  }
  catch (Exception e) {
    def module = 'cred'
    logger.error("${e.class} - ${e.message}", module)

    // We print additional error information for credential ID issues.
    if (e.class == org.jenkinsci.plugins.credentialsbinding.impl.CredentialNotFoundException) {
      def credsUri = env.JENKINS_URL ? "${env.JENKINS_URL}credentials" : "<JENKINS_HOME>/${credentials}"
      def message = "Could not locate credential id '${credId} for UsernamePasswordMultiBinding'. Please see ${credsUri} to ensure that ID exists."
      logger.error(message, module)
    }
    throw e
  }
}

def call(credId, closure) {
  call(credId, 'USERNAME', 'PASSWORD', closure)
}