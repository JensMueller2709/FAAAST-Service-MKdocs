# Getting Started

This is an example of how to set up your project locally.
To get a local copy up and running follow these simple example steps.
To compile the FA³ST service you need to have a JDK and Maven installed.

### Prerequisites

-   Java 11+
-   Maven

### Building from Source

```sh
git clone https://github.com/FraunhoferIOSB/FAAAST-Service
cd FAAAST-Service
mvn clean install
```
### Use

#### From JAR

[Download latest version as precompiled JAR](https://search.maven.org/remote_content?g=de.fraunhofer.iosb.ilt.faaast.service&a=starter&v=LATEST)

To start the Service from command line use the following commands.
```sh
cd /starter/target
java -jar starter-{version}.jar -m {path/to/your/AASEnvironment}
```
For further information on using the command line see [here](#usage-with-command-line).

#### As Maven Dependency
```xml
<dependency>
	<groupId>de.fraunhofer.iosb.ilt.faaast.service</groupId>
	<artifactId>starter</artifactId>
	<version>0.1.0</version>
</dependency>
```

#### As Gradle Dependency
```kotlin
implementation 'de.fraunhofer.iosb.ilt.faaast.service:starter:0.1.0'
```

A maven plugin we are using in our build script leads to an error while resolving the dependency tree in gradle. Therefore you need to add following code snippet in your `build.gradle`. This code snippet removes the classifier of the transitive dependency `com.google.inject:guice`.
```kotlin
configurations.all {
	resolutionStrategy.eachDependency { DependencyResolveDetails details ->
		if (details.requested.module.toString() == "com.google.inject:guice") {
			details.artifactSelection{
				it.selectArtifact(DependencyArtifact.DEFAULT_TYPE, null, null)
			}
		}
	}
}
```

### Example

The following code starts a FA³ST Service with a HTTP endpoint on port 8080.

```java
String pathToYourAASEnvironment = "{pathTo}\\FAAAST-Service\\misc\\examples\\demoAAS.json";
AssetAdministrationShellEnvironment environment = AASEnvironmentHelper.fromFile(new File(pathToYourAASEnvironment));
Service service = new Service(environment,
	new ServiceConfig.Builder()
		.core(new CoreConfig.Builder()
			.requestHandlerThreadPoolSize(2)
			.build())
		.persistence(new PersistenceInMemoryConfig())
		.endpoint(new HttpEndpointConfig())
		.messageBus(new MessageBusInternalConfig())
		.build());
service.start();
```
Afterwards, you can reach the running FA³ST Service via `http://localhost:8080/shells`.

<p align="right">(<a href="#top">back to top</a>)</p>

## Usage with Command Line

This section provides a short introduction of the most important command line arguments. For more details see the [full documentation](./documentation/commandline.md).

If not already done, follow the step in [Building from Source](#building-from-source).

1.  Move to the output folder of the starter
```sh
cd starter/target
```

2.  Execute the `.jar` file to start a FA³ST Service directly with a default configuration. Replace the `{path/to/your/AASEnvironment}` with your file to the Asset Administration Shell Environment you want to load with the FA³ST Service. If you just want to play around, you can use a example AASEnvironment from us [here](starter/src/test/resources/AASFull.json).
```sh
java -jar starter-{version}.jar -m {path/to/your/AASEnvironment}
```

Currently we supporting following formats of the Asset Administration Shell Environment model:
>json, json-ld, aml, xml, opcua nodeset, rdf
<p>

Following command line parameters could be used:
```
\[<String=String>...\]   		Additional properties to override values of configuration using
				JSONPath notation without starting '$.' (see https://goessner.net/articles/JsonPath/)

-c, --config=<configFile>  	The config file path. Default Value = config.json

--emptyModel 			Starts the FA³ST service with an empty Asset Administration Shell Environment.
				False by default

--endpoint=<endpoints>\[,<endpoints>...\]
				Additional endpoints that should be started.

-h, --help                 	Show this help message and exit.

-m, --model=<modelFile>    	Asset Administration Shell Environment FilePath.
				Default Value = aasenvironment.*

--\[no-\]autoCompleteConfig
				Autocompletes the configuration with default
				values for required configuration sections. True
				by default

--\[no-\]modelValidation 		Validates the AAS Environment. True by default

-V, --version              	Print version information and exit.
```

## Usage with Docker

### Docker-Compose
Clone this repository, navigate to `/misc/docker/` and run this command inside it.
```sh
cd misc/docker
docker-compose up
```
To use your own AAS environment replace the model file `/misc/examples/demoAAS.json`.
To modify the configuration edit the file `/misc/examples/exampleConfiguration.json`.
You can also override configuration values using environment variables [see details](./documentation/commandline.md).

### Docker CLI
To start the FA³ST service with an empty AAS environment execute this command.
```sh
docker run --rm -P fraunhoferiosb/faaast-service '--emptyModel' '--no-modelValidation'
```
To start the FA³ST service with your own AAS environment, place the JSON-file (in this example `demoAAS.json`) containing your enviroment in the current directory and modify the command accordingly.
```sh
docker run --rm -v ../examples/demoAAS.json:/AASEnv.json -e faaast.model=AASEnv.json -P fraunhoferiosb/faaast-service '--no-modelValidation'
```
Similarly to the above examples you can pass more arguments to the FA³ST service by using the CLI or a configuration file as provided in the cfg folder (use the `faaast.config` environment variable for that).
