import groovy.time.TimeCategory
import java.util.concurrent.ThreadLocalRandom;


if (args.recreateRelease) {
    def envName = args.environmentName?: 'prod'
    project args.projectName, {
        release args.releaseName, {
            plannedEndDate = args.plannedEndDate
            plannedStartDate = args.plannedStartDate
        }
        application args.applicationName
        environment envName
    }
}

if (args.build) {
  loadBuildData()
}

if (args.systemTest) {
  loadTestData('system-test', args.systemTest)
}

if (args.unitTest) {
  loadTestData('unit-test', args.unitTest)
}

if (args.deployment) {
  loadDeploymentData()
}

if (args.environments) {
  loadDeploymentData2()
}

if (args.incident) {
  loadIncidentData()
}

if (args.feature) {
  loadFeatureData()
}

if (args.defect) {
  loadDefectData()
}

/**
 * Reads following parameter args:
 * build.lastNumberOfDays
 * build.successCount
 * build.failureCount
 * build.trendUp
 */
def loadBuildData() {

    def dates = []
    def endDates = []
    calculateDates(args.build, dates, endDates)


    def duration = args.build.durationInMillis?:60000
    def trendUp = args.build.trendUp?:false
    def successCount = args.build.successCount?:0
    int failureCount = args.build.failureCount?:0

    //toggle trendUp since we go backwards in dates list
    trendUp = !trendUp

    dates.eachWithIndex { date, index ->
        if (trendUp) {
            if (successCount > 0) successCount--
        } else {
            if (failureCount > 0) failureCount--
        }
        def statuses = []
        successCount.times {
            statuses << 'SUCCESS'
        }
        failureCount.times {
            statuses << 'FAILURE'
        }
        println "successCount is $successCount from $args.build.successCount "
        println "failureCount is $failureCount from $args.build.failureCount"

        println "duration is: $duration"
        println "startTime is $date"
        println "endTime is ${endDates[index]}"
        println "buildStatus is $status"
        println "timestamp is ${endDates[index]}"
        statuses.each { status ->
            sendReportingData reportObjectTypeName: 'build', payload: """{
                        "source": "Jenkins",
                        "sourceUrl": "http://10.200.1.210:8080/job/HelloWorld/$index",
                        "pluginName": "EC-Jenkins",
                        "pluginConfiguration": "config",
                        "releaseName": "$args.releaseName",
                        "releaseProjectName": "$args.projectName",
                        "duration" : $duration,
                        "startTime": "$date",
                        "endTime": "${endDates[index]}",
                        "buildStatus": "$status",
                        "timestamp": "${endDates[index]}"
                    }"""
        }
        }
}

/**
 * Reads following parameter args from the specs arg:
 * specs.lastNumberOfDays
 * specs.successCount
 * specs.trendUp
 */
def loadTestData(def category, def specs) {

    def source = (category == 'system-test') ? 'HPALM' : 'Jenkins'
    def sourceUrl = (category == 'system-test') ? 'http://10.200.1.210:8080/qcbin/domain' : 'http://10.200.1.210:8080/job/HelloWorld/1'
    def pluginName = (category == 'system-test') ? 'EC-ALM' : 'EC-Jenkins'

    def dates = []
    def endDates = []
    calculateDates(specs, dates, endDates)

    def duration = specs.durationInMillis?:60000
    def trendUp = specs.trendUp?:false
    def successCount = specs.successCount?:10

    //toggle trendUp since we are going backwards in dates list
    trendUp = !trendUp
    int failureCount = specs.failureCount?:(successCount/2)
    dates.eachWithIndex { date, index ->

        if (trendUp) {
            if (failureCount > 0) failureCount--
            successCount++
        } else {
            failureCount++
            if (successCount > 0) successCount--
        }

        println "Success count: $successCount, Failure count: $failureCount"

        sendReportingData reportObjectTypeName: 'quality', payload: """{
                    "source": "$source",
                    "sourceUrl": "$sourceUrl",
                    "pluginName": "$pluginName",
                    "pluginConfiguration": "config",
                    "releaseName": "$args.releaseName",
                    "releaseProjectName": "$args.projectName",
                    "duration" : $duration,
                    "timestamp": "$date",
                    "category": "$category",
                    "failedTests": $failureCount,
                    "successfulTests": $successCount
                }"""
    }
}

/**
 * Reads following parameter args:
 * deployment.lastNumberOfDays
 * deployment.successCount
 * deployment.failureCount
 * deployment.trendUp
 */
def loadDeploymentData() {

    def dates = []
    def endDates = []
    calculateDates(args.deployment, dates, endDates)


    def duration = args.deployment.durationInMillis?:60000
    def trendUp = args.deployment.trendUp?:false
    def successCount = args.deployment.successCount?:10

    //loop up in the system for ids since deploy reports currently use ids also
    def application = getApplication projectName: args.projectName, applicationName: args.applicationName
    def release = getRelease projectName: args.projectName, releaseName: args.releaseName
    def envName = args.environmentName?: 'prod'
    def environment = getEnvironment projectName: args.projectName, environmentName: envName

    //toggle trendUp since we go backwards in dates list
    trendUp = !trendUp
    int failureCount = args.deployment.failureCount?:(successCount/2)
    dates.eachWithIndex { date, index ->

        if (trendUp) {
            if (failureCount > 0) failureCount--
            successCount++
        } else {
            failureCount++
            if (successCount > 0) successCount--
        }
        def statuses = []
        successCount.times {
            statuses << 'success'
        }
        failureCount.times {
            statuses << 'error'
        }
        println "Success count: $successCount, Failure count: $failureCount"


        statuses.each { status ->
            sendReportingData reportObjectTypeName: 'deployment', payload: """{
                        "reportEventType": "ef_process_run_completed",
                        "projectName": "$args.projectName",
                        "releaseProjectName": "$args.projectName",
                        "releaseName": "$args.releaseName",
                        "releaseId": "$release.releaseId",
                        "environmentProjectName": "$args.projectName",
                        "environmentName": "$environment.environmentName",
                        "environmentId": "$environment.environmentId",
                        "applicationProjectName": "$args.projectName",
                        "applicationName" : "$args.applicationName",
                        "applicationId" : "$application.applicationId",
                        "jobStart": "$date",
                        "jobFinish": "${endDates[index]}",
                        "deploymentOutcome": "$status"
                    }"""
        }
    }
}

def loadDeploymentData2() {

    //look up in the system for ids since deploy reports currently use ids also
    def application = getApplication projectName: args.projectName, applicationName: args.applicationName
    def release = getRelease projectName: args.projectName, releaseName: args.releaseName

    args.environments.each { env ->

        if (args.recreateRelease) {
            environment env.environmentName, projectName: args.projectName
        }
        def environment = getEnvironment projectName: args.projectName, environmentName: env.environmentName

        def dates = []
        def endDates = []
        def dateValues = []
        def statuses = []
        calculateDates2(env, dates, endDates, statuses, dateValues)

        def tasksToRunAtleastOnce = true
        statuses.eachWithIndex { status, index ->
            if (args.createDeployments) {
                sendReportingData reportObjectTypeName: 'deployment', payload: """{
                        "reportEventType": "ef_process_run_completed",
                        "projectName": "$args.projectName",
                        "releaseProjectName": "$args.projectName",
                        "releaseName": "$args.releaseName",
                        "releaseId": "$release.releaseId",
                        "environmentProjectName": "$args.projectName",
                        "environmentName": "$environment.environmentName",
                        "environmentId": "$environment.environmentId",
                        "applicationProjectName": "$args.projectName",
                        "applicationName" : "$args.applicationName",
                        "applicationId" : "$application.applicationId",
                        "jobStart": "${dates[index]}",
                        "jobFinish": "${endDates[index]}",
                        "deploymentOutcome": "$status"
                    }"""

                // create a pipeline task corresponding to the deployment
                sendReportingData reportObjectTypeName: 'pipelineRun', payload: """{
                    "reportEventType": "ef_pipeline_run_task_completed",
                    "projectName": "$args.projectName",
                    "releaseProjectName": "$args.projectName",
                    "releaseName": "$args.releaseName",
                    "releaseId": "$release.releaseId",
                    "environmentProjectName": "$args.projectName",
                    "environmentName": "$environment.environmentName",
                    "environmentId": "$environment.environmentId",
                    "applicationProjectName": "$args.projectName",
                    "applicationName" : "$args.applicationName",
                    "applicationId" : "$application.applicationId",
                    "flowRuntimeStateStart": "${dates[index]}",
                    "flowRuntimeStateFinish": "${endDates[index]}"
                }"""
            }

            def startDate = dateValues[index]

            env.tasks?.each { task ->
                def manual = task.manual?:false
                def durationInMinutes = task.durationInMinutes?:30
                def repeat = task.repeat?:1
                def runOnce = task.runOnce?:false
                //tasksToRunAtleastOnce
                if (!runOnce || tasksToRunAtleastOnce) {
                    def endDate
                    use( TimeCategory ) {
                        int randomMin = ThreadLocalRandom.current().nextInt(durationInMinutes, durationInMinutes + 5)
                        endDate = startDate + randomMin.minutes
                    }
                    def endDateStr = endDate.format("yyyy-MM-dd'T'HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))

                    repeat.times{
                        sendReportingData reportObjectTypeName: 'pipelineRun', payload: """{
                            "reportEventType": "ef_pipeline_run_task_completed",
                            "projectName": "$args.projectName",
                            "releaseProjectName": "$args.projectName",
                            "releaseName": "$args.releaseName",
                            "releaseId": "$release.releaseId",
                            "environmentProjectName": "$args.projectName",
                            "environmentName": "$environment.environmentName",
                            "environmentId": "$environment.environmentId",
                            "applicationProjectName": "$args.projectName",
                            "applicationName" : "$args.applicationName",
                            "applicationId" : "$application.applicationId",
                            "flowRuntimeStateStart": "${dates[index]}",
                            "flowRuntimeStateFinish": "$endDateStr",
                            "manual": $manual,
                            "taskName": "$task.taskName"
                        }"""
                    }
                }
            }
            tasksToRunAtleastOnce = false
        }
    }

}

/**
 * Reads following parameter args:
 * incident.lastNumberOfDays
 * incident.openCount
 * incident.closedCount
 * incident.trendUp
 */
def loadIncidentData() {

    def dates = []
    def endDates = []
    calculateDates(args.incident, dates, endDates)


    def duration = args.incident.durationInMillis?:60000
    def trendUp = args.incident.trendUp?:false
    def closedCount = args.incident.closedCount?:10

    //toggle trendUp since we go backwards in dates list
    trendUp = !trendUp
    int openCount = args.incident.openCount?:(closedCount/2)
    dates.eachWithIndex { date, index ->

        if (trendUp) {
            if (openCount > 0) openCount--
            closedCount++
        } else {
            openCount++
            if (closedCount > 0) closedCount--
        }
        def statuses = []
        closedCount.times {
            statuses << 'Resolved'
        }
        openCount.times {
            statuses << 'Open'
        }
        println "Closed/Resolved count: $closedCount, Open count: $openCount"

        statuses.eachWithIndex { status, statusIndex ->
            def resolvedOnStr = status == 'Resolved' ? "\"resolvedOn\" : \"${endDates[index]}\"," : ""
            def incidentId = "Incident_${statusIndex}"

            sendReportingData reportObjectTypeName: 'incident', payload: """{
                        "source": "ServiceNow",
                        "sourceUrl": "https://ven077552.service-now.com",
                        "pluginName": "EC-ServiceNow",
                        "pluginConfiguration": "config",
                        "releaseName": "$args.releaseName",
                        "releaseProjectName": "$args.projectName",
                        "incidentId" : "$incidentId",
                        "createdOn": "$date",
                        "modifiedOn": "$date",
                        $resolvedOnStr
                        "status": "$status",
                        "timestamp": "$date"
                    }"""
        }
    }
}

/**
 * Reads following parameter args:
 * feature.lastNumberOfDays
 * feature.openCount
 * feature.closedCount
 * feature.trendUp
 * feature.percentWithStoryPoints
 */
def loadFeatureData() {

    def dates = []
    def endDates = []
    calculateDates(args.feature, dates, endDates)


    def duration = args.feature.durationInMillis?:60000
    def trendUp = args.feature.trendUp?:false
    def closedCount = args.feature.closedCount?:10

    //toggle trendUp since we go backwards in dates list
    trendUp = !trendUp
    int openCount = args.feature.openCount?:(closedCount/2)
    dates.eachWithIndex { date, index ->

        if (trendUp) {
            if (openCount > 0) openCount--
            closedCount++
        } else {
            openCount++
            if (closedCount > 0) closedCount--
        }
        def statuses = []
        closedCount.times {
            def statusStr = it%2 == 0 ? 'Resolved' : 'Closed'
            println "Status $statusStr"
            statuses << statusStr
        }
        openCount.times {
            statuses << 'Open'
        }
        println "Closed/Resolved count: $closedCount, Open count: $openCount"

        def percentWithStoryPoints = args.feature.percentWithStoryPoints?:100
        def storiesWithPoints = percentWithStoryPoints * statuses.size() /100

        statuses.eachWithIndex { status, statusIndex ->
            def resolvedOnStr = status == 'Resolved' ? "\"resolvedOn\" : \"${endDates[index]}\"," : ""
            def featureId = "Feature_${statusIndex}"
            def storyPointsStr = (storiesWithPoints >= (statusIndex + 1)) ? "\"storyPoints\" : 10," : ""

            sendReportingData reportObjectTypeName: 'feature', payload: """{
                        "source": "JIRA",
                        "sourceUrl": "jira.electric-cloud.com/browse/${featureId}",
                        "pluginName": "EC-JIRA",
                        "pluginConfiguration": "config",
                        "releaseName": "$args.releaseName",
                        "releaseProjectName": "$args.projectName",
                        "featureName" : "$featureId",
                        "key" : "$featureId",
                        "type" : "Story",
                        $storyPointsStr
                        "createdOn": "$date",
                        "modifiedOn": "$date",
                        $resolvedOnStr
                        "status": "$status",
                        "timestamp": "$date"
                    }"""
        }
    }
}

/**
 * Reads following parameter args:
 * defect.lastNumberOfDays
 * defect.openCount
 * defect.closedCount
 * defect.trendUp
 */
def loadDefectData() {

    def dates = []
    def endDates = []
    calculateDates(args.defect, dates, endDates)


    def duration = args.defect.durationInMillis?:60000
    def trendUp = args.defect.trendUp?:false
    def closedCount = args.defect.closedCount?:10

    //toggle trendUp since we go backwards in dates list
    trendUp = !trendUp
    int openCount = args.defect.openCount?:(closedCount/2)
    dates.eachWithIndex { date, index ->

        if (trendUp) {
            if (openCount > 0) openCount--
            closedCount++
        } else {
            openCount++
            if (closedCount > 0) closedCount--
        }
        def statuses = []
        closedCount.times {
            statuses << 'Resolved'
        }
        openCount.times {
            statuses << 'Open'
        }
        println "Closed/Resolved count: $closedCount, Open count: $openCount"

        statuses.eachWithIndex { status, statusIndex ->
            def resolvedOnStr = status == 'Resolved' ? "\"resolvedOn\" : \"${endDates[index]}\"," : ""

            def resolution = status == 'Resolved' ? 'Fixed' : ""
            def defectId = "Defect_${statusIndex}"
            sendReportingData reportObjectTypeName: 'defect', payload: """{
                        "source": "JIRA",
                        "sourceUrl": "jira.electric-cloud.com/browse/${defectId}",
                        "pluginName": "EC-JIRA",
                        "pluginConfiguration": "config",
                        "releaseName": "$args.releaseName",
                        "releaseProjectName": "$args.projectName",
                        "defectName" : "$defectId",
                        "key" : "$defectId",
                        "createdOn": "$date",
                        "modifiedOn": "$date",
                        $resolvedOnStr
                        "status": "$status",
                        "resolution": "$resolution",
                        "timestamp": "$date"
                    }"""
        }
    }
}

def calculateDates(def specs, def dates, def endDates) {
    def count = specs.lastNumberOfDays?:1
    def dayOffSet = specs.dayOffSet?:0
    def duration = specs.durationInMillis?:60000
    int durationInMin = (int)duration/(1000*60)
    int frequencyOverDays = specs.frequencyOverDays?:2
    def now = new Date()

    for (def i = dayOffSet; i < count + dayOffSet; i = i + frequencyOverDays) {
        use( TimeCategory ) {
            def daysAgo = now - (i).days
            def dateStr = daysAgo.format("yyyy-MM-dd'T'HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))
            println "Date: $dateStr"
            dates << dateStr

            //hitting groovy bug
            //def endDate = daysAgo + duration.milliseconds
            def endDate = daysAgo + durationInMin.minutes
            //these dates are used for events that have already happened
            //so don't want dates in future.
            if (endDate.compareTo(now) > 0 ) {
                endDate = now
            }
            def endDateStr = endDate.format("yyyy-MM-dd'T'HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))
            println "End date: $endDateStr"
            endDates << endDateStr
        }
    }
}


def calculateDates2(def specs, def dates, def endDates, def statuses, def dateValues) {
    def count = specs.lastNumberOfDays?:1
    def dayOffSet = specs.dayOffSet?:0
    def duration = specs.durationInSeconds?:60
    int durationInMin = (int)(duration/60)
    int frequencyOverDays = specs.frequencyOverDays?:1

    def now = new Date()
    def index = count - 1
    specs.abortCount.times {
        if (toss()) {
            statuses << 'aborted'
            use( TimeCategory ) {
                if (index < 0) {
                    index = count - 1
                }
                def startDate = now - (dayOffSet + frequencyOverDays + index).days
                index--
                def dateStr = startDate.format("yyyy-MM-dd'T'HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))
                println "Date: $dateStr"
                dates << dateStr
                dateValues << startDate
                // duration variance
                int minDuration = durationInMin + (index/10)
                int maxDuration = durationInMin + (index/10) + 5
                int randomDuration = ThreadLocalRandom.current().nextInt(minDuration, maxDuration)
                def endDate = startDate + randomDuration.minutes
                def endDateStr = endDate.format("yyyy-MM-dd'T'HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))
                println "End date: $endDateStr"
                endDates << endDateStr
            }
        }
    }

    index = count -1
    specs.errorCount.times {
        if (toss()) {
            statuses << 'error'
            use( TimeCategory ) {
                if (index < 0) {
                    index = count - 1
                }
                def startDate = now - (dayOffSet + frequencyOverDays + index).days
                index--
                def dateStr = startDate.format("yyyy-MM-dd'T'HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))
                println "Date: $dateStr"
                dates << dateStr
                dateValues << startDate

                int minDuration = durationInMin + (index/10)
                int maxDuration = durationInMin + (index/10) + 5
                int randomDuration = ThreadLocalRandom.current().nextInt(minDuration, maxDuration)
                def endDate = startDate + randomDuration.minutes
                def endDateStr = endDate.format("yyyy-MM-dd'T'HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))
                println "End date: $endDateStr"
                endDates << endDateStr
            }
        }
    }

    index = count -1
    specs.rollbackCount.times {
        if (toss()) {
            statuses << 'rollback'
            use( TimeCategory ) {
                if (index < 0) {
                    index = count - 1
                }
                def startDate = now - (dayOffSet + frequencyOverDays + index).days
                index--
                def dateStr = startDate.format("yyyy-MM-dd'T'HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))
                println "Date: $dateStr"
                dates << dateStr
                dateValues << startDate

                int minDuration = durationInMin + (index/10)
                int maxDuration = durationInMin + (index/10) + 5
                int randomDuration = ThreadLocalRandom.current().nextInt(minDuration, maxDuration)
                def endDate = startDate + randomDuration.minutes
                def endDateStr = endDate.format("yyyy-MM-dd'T'HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))
                println "End date: $endDateStr"
                endDates << endDateStr
            }
        }
    }

    index = 0
    specs.successCount.times {
        if (toss()) {
            statuses << 'success'
            use( TimeCategory ) {
                if (index > count) {
                    index = 0
                }
                def startDate = now - (dayOffSet + frequencyOverDays + index).days
                index++
                def dateStr = startDate.format("yyyy-MM-dd'T'HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))
                println "Date: $dateStr"
                dates << dateStr
                dateValues << startDate

                int minDuration = durationInMin + (index/10)
                int maxDuration = durationInMin + (index/10) + 5
                int randomDuration = ThreadLocalRandom.current().nextInt(minDuration, maxDuration)
                def endDate = startDate + randomDuration.minutes
                def endDateStr = endDate.format("yyyy-MM-dd'T'HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))
                println "End date: $endDateStr"
                endDates << endDateStr
            }
        }
    }

}

def toss() {
    int randomDuration = ThreadLocalRandom.current().nextInt(1, 3)
    randomDuration == 2
}
