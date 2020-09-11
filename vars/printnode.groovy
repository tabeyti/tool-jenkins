def call() {
    sh  label: "${env.NODE_NAME}",
        script: "echo Running on ${NODE_NAME}"
}
