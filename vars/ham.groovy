@Library('tools')_

/**
 * Base for the DSL classes.
 */
abstract class DslBase implements Serializable {
  protected def script
  public static Boolean DEBUG_ENABLED = false

  /**
   * Base constructor.
   * @param  script [description]
   */
  public DslBase(def script) {
    this.script = script
  }

  /**
   * Wrapper around script's println. Used for debug output.
   * @param  message [description]
   */
  protected debug(String message) {
    if (DslBase.DEBUG_ENABLED) {
      this.script.println message
    }
  }

  /**
   * Check to assert that the property is null. Generally
   * called at the beginning of a DSL property method if
   * the implementer wants to ensure an exception is thrown
   * if the property was called twice.
   * @param  val        The value
   * @param  propString The property name
   * @param  message    Custom message to display, otherwise a default exception
   * @return            Throws an Exception if value is not null
   */
  protected def assertNull(def val, String propString, String message = null) {
    if (null != val) {
      if (null == message) {
        throw new Exception("cannot define ${propString} twice.")
      }
      else {
        throw new Exception(message)
      }
    }
  }

  /**
   * Evaluates the DSL property values for correctness when
   * called, throwing an exception if invalid.
   */
  def abstract evaluate()
} // class DslBase

/**
 * DSL 'task' object.
 * Holds properties specific to running a
 * body of work on an agent/node.
 *
 * This was created as a base class to be
 * used by (potentially) other custom DSL type
 * 'steps'.
 */
class Task extends DslBase {
  public String name
  public String node
  public String sh
  public String bat
  public List env = []
  public List creds = []
  public String archive
  public Map build
  public def git
  public String svn

  def Task(def script) {
    super(script)
  }

  def Task(String name, def script) {
    super(script)
    this.name = name
  }

  def name(String name) {
    assertNull(this.name, 'name')
    debug "name: $name"
    this.name = name
  }

  def node(String node) {
    assertNull(this.node, 'node')
    debug "node: $node"
    this.node = node
  }

  def git(Object git) {
    assertNull(this.git, 'git')
    debug "git: ${git}"
    this.git = git
  }

  def sh(String cmd) {
    assertNull(this.sh, 'sh')
    debug "${this.name} - sh: ${cmd}"
    this.sh = cmd
  }

  def sh(List cmds) {
    assertNull(this.sh, 'sh')
    debug "${this.name} - sh: ${cmds}"
    this.sh = cmds.join('\n')
  }

  def bat(String cmd) {
    assertNull(this.bat, 'bat')
    debug "${this.name} - bat: ${cmd}"
    this.bat = cmd
  }

  def bat(List cmds) {
    assertNull(this.bat, 'bat')
    debug "${this.name} - bat: ${cmds}"
    this.bat = cmds.join('\n')
  }  

  def creds(Object...creds) {
    debug "creds: $creds"
    this.creds = creds
  }

  def env(String...env) {
    debug "env: $env"
    this.env.addAll(env)
  }

  def archive(String pattern) {
    assertNull(this.archive, 'archive')
    debug "archive: $pattern"
    this.archive = pattern
  }

  def evaluate() {
    if (null == this.node) {
      throw new Exception('Must provide a node for a task.')
    }
    if (null == this.name) {
      throw new Exception('Must provide a name for a task.')
    }
  }
} // class Task

/**
 * Axis DSL class.
 * NOTE: I hate/fear inheritance, but I could not come up with
 * a better design pattern that would allow sharing of DSL class
 * specific properties that wouldn't take forever to develop.
 * I'm also a bad engineer and apparently lazy.
 */
class Axis extends Task {
  public Map config

  def Axis(script) {
    super(script)
  }

  def Axis(String name, def script) {
    super(script)
    this.name = name
  }

  def config(Map config) {
    assertNull(this.config, 'config')
    debug 'config'
    this.config = config
  }

  def evaluate() {
    if (null == config || config.isEmpty()) {
      throw new Exception('Must provide a config map for an axis')
    }
    super.evaluate()
  }
} // class Axis

/**
 * Parent DLS class for the Ham block.
 */
class Ham extends Task {
  public List     axes = []
  public List     posts = []
  public List     tasks = []
  public Map      email
  public String   description

  def Ham(def script) {
    super(script)
  }

  def email(String recips) {
    assertNull(this.email, 'email')
    debug "email recips: ${recips}"
    this.email = [recips: recips, on: 'FAILURE']
  }

  def email(Map args) {
    assertNull(this.email, 'email')
    debug "email: ${args}"
    this.email = [recips: args.recips, on: args.on]
  }

  def axis(Closure body) {
    debug 'axis'
    def a = new Axis(this.script)
    body.delegate = a
    body()
    this.axes.add(a)
  }

  def axis(String name, Closure body) {
    debug 'axis'
    def a = new Axis(name, this.script)
    body.delegate = a
    body()
    this.axes.add(a)
  }

  def description(String description) {
    assertNull(this.description, 'description')
    debug "description: ${description}"
    this.description = description
  }

  def desc(String description) {
    assertNull(this.description, 'description')
    debug "description: ${description}"
    this.description = description
  }

  def post(Closure body) {
    debug 'post'
    def t = new Task(this.script)
    body.delegate = t
    body()
    this.posts.add(t)
  }

  def post(String name, Closure body) {
    debug "post: ${name}"
    def t = new Task(name, this.script)
    body.delegate = t
    body()
    this.posts.add(t)
  }

  def task(Closure body) {
    debug 'task'
    def t = new Task(this.script)
    body.delegate = t
    body()
    this.tasks.add(t)
  }

  def task(String name, Closure body) {
    debug "task: ${name}"
    def t = new Task(name, this.script)
    body.delegate = t
    body()
    this.tasks.add(t)
  }

  def evaluate() {
    if (this.axes.isEmpty() && this.tasks.isEmpty()) {
      throw new Exception('Must provide at least one task or axis block.')
    }

    // Add top level properties to each task's associated property
    axes.each { it.sh = "${this.sh}\n${it.sh}" }
    posts.each { it.sh = "${this.sh}\n${it.sh}" }
    tasks.each { it.sh = "${this.sh}\n${it.sh}" }

    axes.each { it.bat = "${this.bat}\n${it.bat}" }
    posts.each { it.bat = "${this.bat}\n${it.bat}" }
    tasks.each { it.bat = "${this.bat}\n${it.bat}" }

    axes.each { it.env.addAll(this.env) }
    posts.each { it.env.addAll(this.env) }
    tasks.each { it.env.addAll(this.env) }

    axes.each { it.creds.addAll(this.creds) }
    posts.each { it.creds.addAll(this.creds) }
    tasks.each { it.creds.addAll(this.creds) }

    if (null != this.archive) {
      axes.each { it.archive += ",${this.archive}" } // multiple archive patterns are comma separated
      posts.each { it.archive += ",${this.archive}" }
      tasks.each { it.archive += ",${this.archive}" }
    }

    // Evaluate all tasks
    axes.each { it.evaluate() }
    tasks.each { it.evaluate() }
    posts.each { it.evaluate() }
  }
}

/**
 * Wrapper around the general scm step for svn checkout
 * @param  remote The svn remote url
 */
def svnCheckout(String remote) {
  checkout poll: false,
    scm: [$class: 'SubversionSCM',
      additionalCredentials: [],
      excludedCommitMessages: '',
      excludedRegions: '',
      excludedRevprop: '',
      excludedUsers: '',
      filterChangelog: false,
      ignoreDirPropChanges: false,
      includedRegions: '',
      locations: [[credentialsId: '8d6bb302-14c3-455e-83b9-d4143763f8ce',
        depthOption: 'infinity',
        ignoreExternalsOption: true,
        local: '.',
        remote: remote]],
      quietOperation: true,
      workspaceUpdater: [$class: 'UpdateUpdater']]
}

/**
 * Creates combinations from the key/value pairs of the provided map.
 * @param  map A map of key/value pairs.
 * @return     A list of maps. Each map is a specific combination of
 *             the provided map.
 */
def combos(Map map) {
  def combinations = []
  def vals = map.values().combinations()
  for (def c in vals) {
    def tempMap = [:]
    def keys = map.keySet()
    for (int i = 0; i < keys.size(); ++i) {
      tempMap[keys[i]] = c[i]
    }
    combinations.add(tempMap)
  }

  return combinations
}

/**
 * Converts a map of key/value pairs into a list of
 * strings used for the 'withEnv' step
 * @param  map Map of key/value pairs.
 * @return     A list of strings under the format 'Key=Value' for 'withEnv' step.
 */
def mapToEnv(map) {
  def envList = []
  for ( e in map) {
    envList.add("${e.getKey()}=${e.getValue()}")
  }
  return envList
}

/**
 * Converts a map of key value pairs into a formatted
 * string of the map's values to be used as a label.
 * @param  map        Map of key/value pairs.
 * @param  ignoreKeys A list of key-strings to exclude from the label.
 * @return A formatted label string.
 */
def mapToLabel(Map map, def ignoreKeys = null) {
  def list = []
  for (def kv in map) {
    if (null != ignoreKeys && ignoreKeys.contains(kv.key)) {
      continue
    }
    list.add("${kv.value}")
  }
  return list.join('-')
}

/**
 * Method for running a block of work (checkout, commands, archiving)
 * on a targeted node under a specified stage.
 * @param  args   A map of pairs needed for the block of
 *                work.
 */
def run(Map args) {

  // TODO: HACK: https://issues.jenkins-ci.org/browse/JENKINS-9104
  def envList = ["_MSPDBSRV_ENDPOINT_=$BUILD_TAG"] + args.env 
  envList += [
    // We add certain fields as env vars for shell/batch
    "name=${args.name}",
    "node=${args.node}",
  ]

  stage(args.name) {
    node(args.node) {
      withEnv(envList) {

        // If given, checkout the appropriate scm
        if (null != args.git) {
          git args.git
        }
        else if (null != args.svn) {
          svnCheckout(args.svn)
        }
        else {
          checkout scm
        }

        // Execute given command line
        withCredentials(args.creds) {
          sh args.sh
        }

        // Archive artifacts if specified
        if (null != args.archive) {
          println "Archiving: ${args.archive}"
          archive args.archive
        }
      }
    }
  }
}


def ham(Closure body) {
  def s = new Ham(this)
  body.delegate = s
  body()
  s.evaluate()

  def failedTasksMap = [:]
  def parTasks = [:]

  currentBuild.description = s.description ?: ''

  // Create parallel tasks from each 'task' block defined
  s.tasks.each { task ->
    def label = task.name
    def envList = task.env
    parTasks[label] =  {
      try {
          run   name: label,
                node: task.node,
                git: s.git, 
                svn: s.svn,
                env: envList,
                sh: task.sh,
                archive: task.archive,
                creds: task.creds
      } catch(Exception e) {
        currentBuild.result = 'FAILURE'
        failedTasksMap[label] = e.message
      }      
    }    
  }

  // Create parallel tasks from 'axis' blocks
  s.axes.each { axis ->
    def combos = combos(axis.config)
    combos.each { c ->
      def envList = mapToEnv(c) + axis.env
      def label = "${axis.name ?: axis.node}-${mapToLabel(c)}"
      parTasks[label] = {
        try {
          run   stage: label,
                node: axis.node,
                git: s.git, 
                svn: s.svn,
                env: envList,
                sh: axis.sh,
                archive: axis.archive,
                creds: axis.creds
          } catch(Exception e) {
            currentBuild.result = 'FAILURE'
            failedTasksMap[label] = e.message
          }
      } // parTasks
    } // combos
  } // axes

  // Run in a try catch so we don't exit execution when there is
  // a parallel stage exception. We still got work to do!
  try {
    parallel parTasks
  } catch (e) {/*ignore*/}

  // Evaluate and execute 'post' tasks (in serial).
  if (currentBuild.result != 'FAILURE' && !s.posts.isEmpty()) {
    s.posts.each { p ->
      try {
        def envList = p.env
        def label = p.name
        run   stage: label,
              node: p.node,
              git: s.git, 
              svn: s.svn,
              env: p.env,
              sh: p.sh,
              archive: p.archive,
              creds: p.creds
      } catch(Exception e) {
        currentBuild.result = 'FAILURE'
        failedTasksMap[label] = e.message
      }
    } // posts
  }

  // Email notification stage
  if (null != s.email && null != s.node) {
    stage('Notify') {
      node(s.node) {
        if ('FAILURE' == currentBuild.currentResult && 'FAILURE' == s.email.on) {
          failedTasksMap.each { println "$it.key: $it.value" }
          // notifier  failures: failedTasksMap, recips: s.email.recips
        }
        else if ('SUCCESS' == currentBuild.currentResult && 'SUCCESS' == s.email.on) {
          // notifier recips: s.email.recips
          println 'sent an email to someone@someplace.com'
        }
      }
    }
  }
}

def parms = [
  GIT_BRANCH_PARAM: 'package',
  GIT_REMOTE: 'tabeyti',
  COMMENT: 'testing potato consistency',
  NOTIFY_LIST: 'tabeyti@gmail.com',
  PUBLISH: true,
  PUBLISH_SERVER: true,
]

ham {
  git     'https://github.com/tabeyti/test.git'
  env     'BUILD_NUMBER=2', // "BUILD_NUMBER=${env.BUILD_NUMBER}",
          "PUBLISH=${parms.PUBLISH}",
          "COMMENT=${parms.COMMENT}"

  sh      '''
          git clean -ffd
          git clean -ffd
          '''

  desc    "${parms.GIT_BRANCH_PARAM}: ${parms.COMMENT}"

  // axis {
  //   name    'comboking'
  //   node    'windows10-1'
  //   config  ARCH: ['x86', 'x86_64']
  //   creds   string(credentialsId: 'aaedce21-b3d0-4f92-adaf-0ae7b8f82a42', variable: 'SECRET')
  //   sh      '''
  //           echo SECRET: $SECRET
  //           echo PUBLISH: $PUBLISH
  //           echo MY ARCH IS: $ARCH
  //           '''
  // }

  task {
    node  'master'
    name  'yam'
    sh    '''
          echo $name: `date +%s`
          sleep 10s
          '''
  }

  task {
    node  'master'
    name  'spam'
    sh    '''
          echo $name: `date +%s`
          sleep 10s
          '''
  }
}