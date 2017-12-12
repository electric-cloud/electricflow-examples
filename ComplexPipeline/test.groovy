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
        println "  myState is $myState.name - subprocedure = $myState.subprocedure"
    }

    myWorkflow.transitionDefinitions?.each { myTransition ->
        println "  myTransition is $myTransition.name"
        println "    condition, stateDefinitionName, targetState, trigger = $myTransition.condition, $myTransition.stateDefinitionName, $myTransition.targetState, $myTransition.trigger"
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

myProject.environments?.each { myEnvironment ->
    println "myEnvironment is $myEnvironment.name"

    myEnvironment.environmentTiers?.each {myEnvironmentTier->
        println "   myEnvironmentTier is $myEnvironmentTier.name"

        myEnvironmentTier.resources?.each {myResource ->
            println "       resource: $myResource"
        }
    }
}

myProject.pipelines?.each { myPipeline ->
    println "myPipeline is $myPipeline.name"
    myPipeline.formalParameters?.each { myParameter ->
        switch (myParameter.type) {
            case ~/checkbox/:
                println "  checkbox $myParameter.name of type $myParameter.type"
                break
            default:
                println "  default $myParameter.name of type $myParameter.type"
                break
        }
    }

    myPipeline.stages?.each { myStage ->
        println "  myStage is $myStage.name"

        myStage.gates?.each { myGate ->
            println "    myGate is $myGate.task.name of type $myGate.gateType"
        }
        myStage.tasks?.each { myTask ->
            switch (myTask.taskType) {
                case ~/PROCEDURE/:
                    println "      myTask $myTask.name type Procedure $myTask.subproject : $myTask.subprocedure"
                    break
                case ~/COMMAND/:
                    println "      myTask $myTask.name type Command : $myTask.commandToRun"
                    break
                case ~/PROCESS/:
                    println "      myTask $myTask.name type Process"
                    break
                case ~/MANUAL/:
                    println "      myTask $myTask.name type Manual"
                    break
                case ~/PLUGIN/:
                    println "      myTask $myTask.name type Plugin"
                    break
                case ~/WORKFLOW/:
                    println "      myTask $myTask.name type Workflow"
                    break
                case ~/UTILITY/:
                    println "      myTask $myTask.name type Utility"
                    break
                case ~/GROUP/:
                    println "      myTask $myTask.name type Group"
                    break
                default:
                    println "      myTask $myTask.name type Unhandled"
                    break
            }
        }
    }
}
