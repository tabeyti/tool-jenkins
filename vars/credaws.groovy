def call(credId, accessKeyId, secretAccessKey, closure) {
  try {
    withCredentials([
        [$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: accessKeyId,
        credentialsId: credId, secretKeyVariable: secretAccessKey]]) {
      closure()
    }
  }
  catch (Exception e) {
    def module = 'credaws'
    logger.error("${e.class} - ${e.message}", module)

    // We print additional error information for credential ID issues.
    if (e.class == org.jenkinsci.plugins.credentialsbinding.impl.CredentialNotFoundException) {
      def credsUri = env.JENKINS_URL ? "${env.JENKINS_URL}credentials" : "<JENKINS_HOME>/${credentials}"
      def message = "Could not locate credential id '${credId}' for AmazonWebServicesCredentialsBinding. Please see ${credsUri} to ensure that ID exists."
      logger.error(message, module)
    }
    throw e
  }
}

def call(credId, closure) {
  call(credId, 'AWS_ACCESS_KEY_ID', 'AWS_SECRET_ACCESS_KEY', closure)
}