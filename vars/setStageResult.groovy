import groovy.transform.InheritConstructors

@InheritConstructors
class FakeException extends Exception {
}

def call(String result) {
    catchError(buildResult: currentBuild.currentResult, stageResult: result) {
        sh script: "echo Stage set to $result", label: "Stage set to $result"
        throw new FakeException('Fake exception - used to invoke catchError for modifying the stage status')
    }
}