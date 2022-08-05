# Persistence
Each persistence configuration supports at least the following configuration parameters:
-   `initialModel` (optional, can be overriden by CLI parameter or environment variable): Path to the AAS Environment model file
-   `decoupleEnvironment` (optional, default: `true`): Only applicable if the AAS Environment is given as Java Object. If set to true, the persistence makes a deep copy of the AAS Environment and decouples the internal AAS Environment from the AAS Environment parsed on startup. If set to false, the same object instance is used in the FA³ST Service, which may have unexpected side effects.

Example of a persistence configuration:
```json
{
	"persistence" : {
		"@class" : "de.fraunhofer.iosb.ilt.faaast.service.persistence.memory.PersistenceInMemory",
		"initialModel" : "{pathTo}/FAAAST-Service/misc/examples/demoAAS.json",
		"decoupleEnvironment" : true
	}
}
```

Not yet implemented:
-   AASX Packages
-   Package Descriptors
-   SubmodelElementStructs

## In Memory Persistence
The In Memory Persistence implementation keeps the AAS environment model parsed at startup in the local memory. Any change request, such as changing the value of a property, results in a change to the AAS environment model in the local memory. When the FA³ST Service is stopped, the changes to the AAS environment are lost.

The In Memory Persistence has no additional configuration parameters.

## File Persistence
The file persistence keeps the entire AAS Environment in a model file which is stored at the local machine. Any change request, such as changing the value of a property, results in a change to the AAS environment model file. Thus, changes are stored permanently.

File Persistence configuration supports the following configuration parameters:
-   `dataDir` (optional, default: `/`): Path under which the model file created by the persistence is to be saved
-   `keepInitial` (optional, default: `true`): If false the model file parsed on startup will be overriden with changes. If true a copy of the model file will be created by the persistence which keeps the changes.
-   `dataformat` (optional, default: same data format as input file): Determines the data format of the created file by file persistence. Ignored if the `keepInitial` parameter is set to false. Supported data formats are `JSON`, `XML`, `AML`, `RDF`, `AASX`, `JSONLD`, `UANODESET`.

Example configuration for the file persistence:

```json
{
	"persistence" : {
		"@class" : "de.fraunhofer.iosb.ilt.faaast.service.persistence.file.PersistenceFile",
		"initialModel" : "{pathTo}/FAAAST-Service/misc/examples/demoAAS.json",
		"dataDir": ".",
		"keepInitial": true,
		"dataformat": "XML"
	}
}
```

<hr>
