import groovy.time.TimeCategory
import java.util.concurrent.ThreadLocalRandom;


if (args.builds) {
    loadBuildData()
}

if (args.tests) {
    loadTestData()
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

//    Need data in the following format:
//    buildStatus is SUCCESS
//    duration is: 75000000
//    endTime is 2018-01-11T17:26:44.126
//    startTime is 2018-01-10T20:36:44.126
//    timestamp is 2018-01-11T17:26:44.126

    args.builds.each { myBuild->
        println "duration is: $myBuild.duration"
        println "startTime is $myBuild.startTime"
        println "endTime is $myBuild.endTime"
        println "buildStatus is $myBuild.status"
        println "timestamp is $myBuild.timestamp"

        sendReportingData reportObjectTypeName: 'build',
                payload: """{
                "source": "Jenkins",
                "sourceUrl": "$myBuild.sourceUrl",
                    "pluginName": "EC-Jenkins",
                    "pluginConfiguration": "config",
                    "releaseName": "$myBuild.releaseName",
                    "releaseProjectName": "$myBuild.projectName",
                    "duration" : "$myBuild.duration",
                    "startTime": "$myBuild.startTime",
                    "endTime": "$myBuild.endTime",
                    "buildStatus": "$myBuild.buildStatus",
                    "timestamp": "$myBuild.timestamp"
            }"""

    }
}

def loadTestData() {
    args.tests.each { myTest ->
        sendReportingData reportObjectTypeName: 'quality', payload: """{
                "category": "$myTest.category",
                "pluginName": "$myTest.pluginName",
                "duration" : "$myTest.duration",
                "failedTests": "$myTest.failedTests",
                "pluginConfiguration": "$myTest.pluginConfiguration",
                "releaseName": "$myTest.releaseName",
                "releaseProjectName": "$myTest.projectName",
                "source": "$myTest.source",
                "sourceUrl": "$myTest.sourceUrl",
                "successfulTests": "$myTest.successfulTests",
                "timestamp": "$myTest.timestamp"
            }"""
    }
}
