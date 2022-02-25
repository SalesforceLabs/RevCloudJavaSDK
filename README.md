# RevCloudJavaSDK
Java SDK for Revenue Cloud APIs

The RevCloudJavaSDK helps simplify invocation of Salesforce Revenue Cloud APIs. It wraps authentication, async event handling, error normalization into a few simple lines of Java method calls, and provides the option to invoke async APIs synchronously.

### Usage

**1. Initialization**
```
ApiContext apiContext = new ApiContext();
apiContext.init(args.username, args.password, args.appVersion);
```

or alternately, construct an ApiContext with a sessionId, serviceUrl and appVersion
```
ApiContext apiContext = new ApiContext(sessionId, serviceUrl, appVersion);
```

**2. Invocation (sync)**
```
CreateOrderFromQuoteResponse result = new CreateOrderFromQuote(apiContext).setQuoteRecordId(quoteId).invokeAndWait();
```

**3. Result processing**
```
if (result.isSuccess()) {
    logger.info("Quote to Order succeeded");
    orderId = result.getQuoteToOrderCompletedEvent().getOrderId();
} else {
    logger.error("Error creating order from quote", result.getErrors());
    // more error handling code
}

```

**4. Finish**
```
apiContext.close();
```

### Invocation synchronicity
For APIs that are async in nature, it can be invoked either synchronously or asynchronously.
To invoke an API synchronously, call the ```invokeAndWait()``` method, it will block the thread until the expected event arrives, or until a timeout (configurable via ```ApiContext.setEventListenerTimeout```) limit expires.

To invoke an API asynchronously, call the ```invoke()``` method, after passing in a message consumer by calling ```setMessageConsumer```. For example:
```
Consumer callback = (ApiResponse) -> {
    String orderId = ((CreateOrderFromQuoteResponse) quoteToOrderResp).getQuoteToOrderCompletedEvent().getOrderId(); // this code will be invoked when the anticipated event is received.
}
new CreateOrderFromQuote(apiContext).setQuoteRecordId(quoteId).setMessageConsumer(callback).invoke();
```

### API Client Generation

The RevCloudSDK comes with the ability to generate API client code based on Salesforce's API description or RAML spec. 

To generate client code for an API, add the relevant API information to src/main/resources/manifest.yaml (or a separate yaml file passed with the "-f" argument). Then run GenerateAPI:

```
java GenerateAPI -h [Salesforce login host] -u [username] -p [password] -v 55.0 -pkg com.salesforce.revcloud.api.generated -d [java source directory]
```

After generation completes, compile the project by running ```mvn compile```. You'll find the newly generated files in the packages specified, for example:

- com.salesforce.revcloud.api.generated - API client code's package location, e.g., CreateOrderFromQuote
- com.salesforce.revcloud.api.generated.event - event code's package location, e.g., QuoteToOrderCompletedEvent
- com.salesforce.revcloud.api.generated.response - API response code's package location, e.g., CreateOrderFromQuoteResponse

#### The manifest file

```endpoint``` The URL endpoint of an API, it should contain everything after the Salesforce service URL.

```event``` Name of the event if an API is asynchronous, and it emits an event at the end of its execution. As a result, the generated API will support the ```invokeAndWait``` method.

```raml``` The RAML spec file, if an API's GET request does not produce a JSON description.

```className``` The class name of the generated API client.

```package``` Optional override of the -package argument specified in the GenerateAPI program. This helps better organize the generated code and avoid potential conflicts.

