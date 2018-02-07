def myProject = args.project

project myProject.name, {
    myProject.workflows.each { myWorkflow ->
        workflowDefinition myWorkflow.name, {
            description = myWorkflow.description

            // For this model, assume all subprocedure calls are into the same project.  Later, we can revise the JSON
            // model to include the name of the parent project, and then add details here.
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