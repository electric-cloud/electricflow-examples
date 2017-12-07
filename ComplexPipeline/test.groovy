def myProject = args.projectName

println "myProject is $myProject"

args.resources.each { myResource ->
    println "myResource is $myResource"
}

args.procedures.each { myProcedure ->
    println "myProcedure is $myProcedure.name"
}

args.workflows?.each { myWorkflow ->
    println "myWorkflow is $myWorkflow.name"
    myWorkflow.stateDefinitions?.each { myState ->
        println "  myState is $myState.name"
        println "    subprocedure = $myState.subprocedure"
    }

    myWorkflow.transitionDefinitions?.each { myTransition ->
        println "  myTransition is $myTransition.name"
        println "    condition = $myTransition.condition"
        println "    stateDefinitionName = $myTransition.stateDefinitionName"
        println "    targetState= $myTransition.targetState"
        println "    trigger = $myTransition.trigger"
    }
}

args.pipelines?.each { myPipeline ->
    println "myPipeline is $myPipeline.name"
    myPipeline.stages?.each { myStage ->
        println "  myStage is $myStage.name"
        myStage.gates?.each { myGate ->
            println "    myGate is $myGate.task.name of type $myGate.gateType"
        }
        myStage.tasks?.each { myTask ->
            println "    myTask is $myTask.name"
        }
    }
}
