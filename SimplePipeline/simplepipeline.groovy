// This is a stubbed implementation of a simple pipeline with three deployments to DEV-QA-PROD.
// Some of the basic features in a pipeline are demonstrated here.

def projName = "Simple Pipeline"
def appNameOne = "Simple Application "
def appTiers = [ appTierOne: "App", appTierTwo: "DB"]
def envNames = [ "DEV", "QA", "Prod" ]
def envTiers = [ envTierOne: "App Server", envTierTwo: "DB Server" ]
def groupId = "com.ec.example"
def resourceNameBase = ['simple-pipeline']
def artifactId = 'simple-pipeline'

envNames.each { eName->
	resource  projName + '-' + eName, {
		description = 'local resource for the sample application'
		hostName = '127.0.0.1'
		hostType = 'CONCURRENT'
		port = '7800'
		resourceDisabled = '0'
		trusted = '0'
		useSSL = '1'
		zoneName = 'default'
	}
}

// Create the project and define it's overall behavior
project projName, {
	description = "Simple Pipeline Example"
}

// Create environments for this project
project projName, {
	envNames.each {eName ->
		environment projName + '-' + eName, {
			description = 'Environment to represent ' + eName
			environmentEnabled = '1'
			reservationRequired = '0'
			
			environmentTier envTiers.envTierOne, {
				resourceName = [projName + '-' + eName,]
			}
		}
	}
}


project projName, {

	procedure 'Build Code', {
		description = 'Stubbed procedure to give the impression of building code'
		jobNameTemplate = ''
		resourceName = 'local'
		timeLimitUnits = 'minutes'

		step 'build code', {
			description = 'Stubbed step to represent building code'
			command = 'echo "here is where you would run your build scripts"'
			errorHandling = 'failProcedure'
			exclusiveMode = 'none'
			releaseMode = 'none'
		}
	}

	procedure 'Get Code', {
		description = 'Stubbed procedure to give the impression of getting code'
		jobNameTemplate = ''
		resourceName = 'local'
		timeLimitUnits = 'minutes'

		step 'checkout code', {
			description = 'Stubbed step to represent getting code'
			command = 'echo "simulated checkout"'
			errorHandling = 'failProcedure'
			exclusiveMode = 'none'
			parallel = '0'
			releaseMode = 'none'
		}
	}

	procedure 'Pre-Deploy Checks', {
		description = 'Stubbed procedure to give the impression of running tests'
		jobNameTemplate = ''
		resourceName = 'local'
		timeLimitUnits = 'minutes'

		step 'Run Predeploy Checks', {
			description = 'This is a stubbed procedure.  Your real implementation will perform sanity checks.'
			alwaysRun = '0'
			broadcast = '0'
			command = 'echo \'insert pre-deploy checks here.\''
			errorHandling = 'failProcedure'
			exclusiveMode = 'none'
			parallel = '0'
			releaseMode = 'none'
			subprocedure = null
			subproject = null
		}
	}

	procedure 'Test Environment', {
		description = 'Stubbed procedure to give the impression of testing an environment'
		jobNameTemplate = ''
		resourceName = 'local'
		timeLimit = ''
		timeLimitUnits = 'minutes'

	step 'Test Environment', {
		description = 'This is a stubbed procedure.  Your real implementation will perform sanity checks.'
		alwaysRun = '0'
		broadcast = '0'
		command = 'echo \'Test the environment\''
		errorHandling = 'failProcedure'
		exclusiveMode = 'none'
		parallel = '0'
		releaseMode = 'none'
		}
	}
}
