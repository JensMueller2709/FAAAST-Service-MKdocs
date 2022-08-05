# AssetConnections
`AssetConnection` implementations allows connecting/synchronizing elements of your AAS to/with assets via different protocol. This functionality is further divided into  3 so-called provider, namely
-   [ValueProvider](https://github.com/FraunhoferIOSB/FAAAST-Service/blob/main/core/src/main/java/de/fraunhofer/iosb/ilt/faaast/service/assetconnection/AssetValueProvider.java), supporting reading and writing values from/to the asset, i.e. each time a value is read or written via an endpoint the request is forwarded to the asset
-   [OperationProvider](https://github.com/FraunhoferIOSB/FAAAST-Service/blob/main/core/src/main/java/de/fraunhofer/iosb/ilt/faaast/service/assetconnection/AssetOperationProvider.java), supporting the execution of operations, i.e. forwards operation invocation requests to the asset and returning the result value,
-   [SubscriptionProvider](https://github.com/FraunhoferIOSB/FAAAST-Service/blob/main/core/src/main/java/de/fraunhofer/iosb/ilt/faaast/service/assetconnection/AssetSubscriptionProvider.java), supporting synchronizing the AAS with pub/sub-based assets, i.e. subscribes to the assets and updates the AAS with new values over time.

An implemented does not have to implement all providers, in fact it is often not possible to implement all of them for a given network protocol as most protocols do not support pull-based and pub/sub mechanisms at the same time (e.g. HTTP, MQTT).

Each provider is connected to exactly one element of the AAS. Each asset connection can have multiples of each provider type. Each FA³ST Service can have multiple asset connections.
Accordingly, each asset connection configuration supports at least this minimum structure

```json
{
	"@class": "...",
	"valueProviders":
	{
		"{serialized Reference of AAS element}":
		{
			// value provider configuration
		},
		...
	},
	"operationProviders":
	{
		"{serialized Reference of AAS element}":
		{
			// operation provider configuration
		},
		...
	},
	"subscriptionProviders":
	{
		"{serialized Reference of AAS element}":
		{
			// subscription provider configuration
		},
		...
	}
}
```

A concrete example for OPC UA asset connection could look like this
```json

{
	"@class": "de.fraunhofer.iosb.ilt.faaast.service.assetconnection.opcua.OpcUaAssetConnection",
	"host": "opc.tcp://localhost:4840",
	"valueProviders":
	{
		"(Submodel)[IRI]urn:aas:id:example:submodel:1,(Property)[ID_SHORT]Property1":
		{
			"nodeId": "some.node.id.property.1"
		},
		"(Submodel)[IRI]urn:aas:id:example:submodel:1,(Property)[ID_SHORT]Property2":
		{
			"nodeId": "some.node.id.property.2"
		}
	},
	"operationProviders":
	{
		"(Submodel)[IRI]urn:aas:id:example:submodel:1,(Operation)[ID_SHORT]Operation1":
		{
			"nodeId": "some.node.id.operation.1"
		}
	},
	"subscriptionProviders":
	{
		"(Submodel)[IRI]urn:aas:id:example:submodel:1,(Property)[ID_SHORT]Property3":
		{
			"nodeId": "some.node.id.property.3",
			"interval": 1000
		}
	}
}
```

## MQTT AssetConnection
The MQTT asset connection supports the following functionality:

-   `ValueProvider`: write values only, read not supported
-   `OperationProvider`: not supported
-   `SubscriptionProvider`: subscribe to value changes

**Configuration Parameters**
-   on connection level
-   `serverUri`: URL of the MQTT server
-   `clientId`: id of the MQTT client (optional, default: random)
-   on ValueProdiver level
-   `topic`: MQTT topic to use
-   `contentFormat`: JSON|XML (default: JSON, currently, only JSON supported)
-   `query`: additional information how to format messages sent via MQTT - depends on `contentFormat`. For JSON this is a JSON Path expression.
-   on SubscriptionProdiver level
-   `topic`: MQTT topic to use
-   `contentFormat`: JSON|XML (default: JSON, currently, only JSON supported)
-   `query`: additional information how to extract actual value from received messages - depends on `contentFormat`. For JSON this is a JSON Path expression.

Example configuration for one of the providers:

```json
{
	"topic": "example/myTopic",
	"query": "$.property.value",
	"contentFormat": "JSON"
}
```

## OPC UA AssetConnection
The OPC UA asset connection supports the following functionality:

-   `ValueProvider`: fully supported (read/write)
-   `OperationProvider`: invoke operations synchroniously, async invocation not supported
-   `SubscriptionProvider`: fully supported

**Configuration Parameters**
-   on connection level
-   `host`: URL of the OPC UA server. Please be sure that the URL starts with `opc.tcp://`.
-   on ValueProdiver level
-   `nodeId`: nodeId of the the OPC UA node to read/write
-   on OperationProdiver level
-   `nodeId`: nodeId of the the OPC UA node representing the OPC UA method to invoke
-   on SubscriptionProdiver level
-   `nodeId`: nodeId of the the OPC UA node to subscribe to
-   `interval`: subscription interval in ms

Example configuration for a subscription provider:

```json
{
	"nodeId": "some.node.id.property",
	"interval": 1000
}
```
