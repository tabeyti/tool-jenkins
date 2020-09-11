def stagenodeTests_Smoke() {

    def targetNodeName = 'master'
    def targetStageName = 'When in Rome'
    stagenode(targetStageName, targetNodeName) {
        // Node name is the actual node name not the label, need to fix
        // assertTrue(env.NODE_NAME == targetNodeName)
        assertTrue(env.STAGE_NAME == targetStageName)
        sh 'hostname && echo $NODE_NAME'
    }
}

return this
