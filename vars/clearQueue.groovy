import hudson.model.*
import jenkins.model.Jenkins

def call () {
    println "Clearing queue for ${env.JOB_NAME}"

    try {
        def q = Jenkins.instance.queue
        def currenBuildInfo = currentBuild.externalizableId.split('#')

        def buildName = currenBuildInfo[0]
        def buildNumber = currenBuildInfo[1].toInteger()

        def canceledItems = []
        q.items.findAll {
            // check that the item in the queue is a BuildableItem
            it.class ==  hudson.model.Queue.BuildableItem &&
            // check that the task of the BuildableItem is a PlaceholderTask
            it.task.class == org.jenkinsci.plugins.workflow.support.steps.ExecutorStepExecution.PlaceholderTask &&
            // finally check the runId isn't null and matches our parameters
            null != it.task.runId && it.task.runId.split('#')[0] == buildName && it.task.runId.split('#')[1].toInteger() < buildNumber
        }.each {
            q.cancel(it)
            canceledItems.add(it.task.runId)
        }

        // We "println" once all the items in order to avoid cluttering
        // the number of steps in a build.
        println "Canceled Items:\n${canceledItems.join('\n')}"

    } catch (Exception e) {
        println "Exception: $e"
        // TODO: Remove this once we're happy with the functionality

        def emailBody = "Exception: $e"

        Jenkins.instance.queue.items.each() {
            emailBody += "\n\n${it.task}"
        }

        emailext (
            body: emailBody,
            compressLog: true,
            subject: "ClearQueue exception ${JENKINS_URL}",
            to:  "mjivan@blizzard.com"
        )
    }
}
