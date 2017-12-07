def myProject = args.projectName

project myProject, {
    args.workflows.each { myWorkflow ->
        workflowDefinition myWorkflow.name, {
            description = myWorkflow.description

            myWorkflow.stateDefinitions.each { myState ->
                stateDefinition myState.name, {
                    startable = '1'
                    subprocedure = myState.subprocedure
                }
            }

            myWorkflow.transitionDefinitions.each {myTransition ->
                transitionDefinition myTransition.name, {
                    condition = myTransition.condition
                    stateDefinitionName = myTransition.stateDefinitionName
                    targetState = myTransition.targetState
                    trigger = myTransition.trigger
                }
            }
        }
    }
}