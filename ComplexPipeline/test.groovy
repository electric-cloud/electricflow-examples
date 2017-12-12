def myProject = args.project

println "myProject is $myProject.name"

args.resources.each { myResource ->
    println "myResource is $myResource"
}

myProject.procedures.each { myProcedure ->
    println "myProcedure is $myProcedure.name"
}

myProject.workflows?.each { myWorkflow ->
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

myProject.applications?.each { myApplication ->
    println "myApplication is $myApplication.name"
    myApplication.applicationTiers?.each { myApplicationTier ->
        println "   myApplicationTier is $myApplicationTier.name"
        myApplicationTier.components?.each { myComponent ->
            println "       myComponent is $myComponent.name, $myComponent.groupId:$myComponent.artifactId"
            myComponent.processes?.each { myProcess ->
                println "         myProcess is $myProcess.name"
                myProcess.processSteps?.each { myProcessStep ->
                    println "           myProcessStep is $myProcessStep.name"
                }
                myProcess.processDependencies?.each { myProcessDependency ->
                    println "           myProcessDependency: $myProcessDependency.source -> $myProcessDependency.target"
                }
            }
        }
    }
    myApplication.processes?.each { myProcess->
        println "   my(application)Process is $myProcess.name"
        myProcess.processSteps?.each { myProcessStep ->
            println "       myProcessStep is $myProcessStep.name"
        }
    }
    myApplication.tierMaps?.each { myTierMap->
        println "   myTierMap is $myTierMap.applicationName : $myTierMap.environmentName"
        myTierMap.tierMappings?.each { myTierMapping->
            println "       myTierMapping is $myTierMapping.applicationTierName: $myTierMapping.environmentTierName"
        }
    }

}

myProject.pipelines?.each { myPipeline ->
    println "myPipeline is $myPipeline.name"
    myPipeline.formalParameters?.each { myParameter ->
        println "  formalParameter is $myParameter.name of type $myParameter.type"
        switch (myParameter.type) {
            case ~/checkbox/:
                println "    ->checkbox"
                break
            default:
                println "    ->other"
                break
        }
    }

    myPipeline.stages?.each { myStage ->
        println "  myStage is $myStage.name"

        myStage.gates?.each { myGate ->
            println "    myGate is $myGate.task.name of type $myGate.gateType"
        }
        myStage.tasks?.each { myTask ->
            println "    myTask is $myTask.name"

            switch (myTask.taskType) {
                case ~/PROCEDURE/:
                    println "      Type Procedure $myTask.subproject : $myTask.subprocedure"
                    break
                case ~/COMMAND/:
                    println "      Type Command : $myTask.commandToRun"
                    break
                case ~/PROCESS/:
                    println "      Type Process"
                    break
                case ~/MANUAL/:
                    println "      Type Manual"
                    break
                case ~/PLUGIN/:
                    println "      Type Plugin"
                    break
                case ~/WORKFLOW/:
                    println "      Type Workflow"
                    break
                case ~/UTILITY/:
                    println "      Type Utility"
                    break
                case ~/GROUP/:
                    println "      Type Group"
                    break
                default:
                    println "      Type Unhandled"
                    break
            }
        }
    }
}
