import org.classic.*

/**
* Merges stage and node into one call and sends metrics about it.
* @param stageName name the stage
* @param nodeName which node to use
* @param closure what to execute
*/
def call(String stageName, String nodeName, Closure closure) {
    def queueStartTime = System.currentTimeMillis()
    node(nodeName) {

        stage(stageName) {
            printnode()

            def metricsData = influx.initStageDataModel(env.STAGE_NAME, currentBuild.number, env.JOB_BASE_NAME, env.BRANCH_NAME)
            def stageStartTime = System.currentTimeMillis()
            metricsData["fields"]["QueueTime"] = stageStartTime - queueStartTime

            closure()

            metricsData["fields"]["StageTime"] = System.currentTimeMillis() - stageStartTime
            influx.publishData(metricsData, "queue_times")
        }
    }
}
