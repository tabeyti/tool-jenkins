/**
 * Smoke test validating username/password withCredentials wrapper.
 * @return [description]
 */
def credTests_CredSmoke() {
  node('master') {
    cred('test-username-password') {
      println "$USERNAME:$PASSWORD"
      assertTrue(USERNAME == this.username)
      assertTrue(PASSWORD == this.password)
    }
  }
}

/**
 * Overload test validating username/password withCredentials wrapper with
 * specified env vars.
 * @return [description]
 */
def credTests_CredOverload() {
  node('master') {
    cred('test-username-password', 'uname', 'pword') {
      println "$uname:$pword"
      assertTrue(uname == this.username)
      assertTrue(pword == this.password)
    }
  }
}

/**
 * Smoke test validating secret withCredentials wrapper.
 * @return [description]
 */
def credTests_SecretSmoke() {
  node('master') {
    credsecret('test-secret') {
      println SECRET
      assertTrue(SECRET == this.secret)
    }
  }
}

/**
 * Overload test validating secret withCredentials wrapper with
 * specified env var.
 * @return [description]
 */
def credTests_SecretOverload() {
  node('master') {
    credsecret('test-secret', 'mySecret') {
      println mySecret
      assertTrue(mySecret == this.secret)
    }
  }
}

// AWS cred wrapper disabled as there is no use for the wrapper
// at this time

// /**
//  * Smoke test validating aws withCredentials wrapper.
//  * @return [description]
//  */
// def credTests_AwsSmoke() {
//   node('master') {
//     credaws('test-aws') {
//       println "$AWS_ACCESS_KEY_ID:$AWS_SECRET_ACCESS_KEY"
//       assertTrue(AWS_ACCESS_KEY_ID == this.awsKeyId)
//       assertTrue(AWS_SECRET_ACCESS_KEY == this.awsKeySecret)
//     }
//   }
// }

// /**
//  * Overload test validating aws withCredentials wrapper with
//  * specified env vars.
//  * @return [description]
//  */
// def credTests_AwsOverload() {
//   node('master') {
//     credaws('test-aws', 'id', 'secret') {
//       println "$id:$secret"
//       assertTrue(id == this.awsKeyId)
//       assertTrue(secret == this.awsKeySecret)
//     }
//   }
// }

def getUsername() {
  return 'test'
}

def getPassword() {
  return 'blizzard1'
}

def getAwsKeyId() {
  return 'test'
}

def getAwsKeySecret() {
  return 'blizzard1'
}

def getSecret() {
  return 'blizzard1'
}

return this