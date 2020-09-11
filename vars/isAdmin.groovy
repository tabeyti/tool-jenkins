/**
 * Dependencies:
 *   buildUser.groovy
 *
 */

def call() {
  def admins = [
    'schoi',
    'tabeyti',
    'nzufall',
    'mjivan'
  ]

  return admins.contains(buildUser())
}
