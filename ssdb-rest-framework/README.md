# SSDB REST Framework

This framework provides a lightweight and modular way to build RESTful APIs in Java without relying on heavy libraries
like Spring Boot. It uses custom annotations to define controllers, endpoints, and request mappings, supporting path
variables, query parameters, and request bodies.

## Features

- **Custom Annotations :**
    - `@RestController`: Define a REST controller with an optional route prefix.
    - `@Endpoint`: Map HTTP methods and routes to methods.
    - `@PathVariable`: Bind path variables from the URL to method parameters.
    - `@RequestParam`: Bind query parameters from the URL to method parameters.
- **Dynamic Route Registration:** Automatically scans and registers controllers from a specified package.
- **Path Variables and Query Parameters:** Handle dynamic paths and query strings easily.
- **JSON Serialization:** Methods can return objects, which are automatically serialized to JSON.
- **POST Request Body Handling:** Parse request bodies into objects for POST methods.

## Usage

### 1. Add the Framework as a Dependency

Include the framework in your project. For example, if using a Maven project, add the framework module as a dependency:

```xml

<dependency>
    <groupId>fr.unice.polytech.team.k</groupId>
    <artifactId>ssdb-rest-framework</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Define Controllers

Create a class annotated with `@RestController`. Use `@Endpoint` to map HTTP methods and routes to methods.

```java

@RestController(path = "/api")
public class HelloController {

    @Endpoint(path = "/hello/{name}", method = HttpMethod.GET)
    public String sayHello(@PathVariable("name") String name) {
        return "Hello, " + name + "!";
    }

    @Endpoint(path = "/add", method = HttpMethod.GET)
    public int addNumbers(@RequestParam("a") int a, @RequestParam("b") int b) {
        return a + b;
    }

    @Endpoint(path = "/echo", method = HttpMethod.POST)
    public Message echoBody(Message message) {
        return message;
    }
}

class Message {
    private String content;
// Getters and setters
}
```

### 3. Start the Server

Initialize and start the server, specifying the package to scan for controllers.

```java
public class Main {
    public static void main(String[] args) {
        SSDBHttpServer server = new SSDBHttpServer(8080, "com.yourcompany.api");
        server.start();
    }
}
```

### 4. Send Requests

- **GET** `/api/hello/John` → Response: `Hello, John!`
- **GET** `/api/add?a=5&b=10` → Response: `15`
- **POST** `/api/echo` with body `{ "content": "Hello World!" }` → Response: `{ "content": "Hello World!" }`

## Annotations Overview

### `@RestController`

Defines a REST controller and specify a route prefix for all endpoints in the class.

```java

@RestController(path = "/api")
public class ApiController {...}
```

### `@Endpoint`

Maps a route and HTTP method to a specific method in the controller.

```java

@Endpoint(path = "/example", method = HttpMethod.GET)
public String example() { ...}
```

### `@PathVariable`

Binds a path variable from the URL to a method parameter.

```java

@Endpoint(path = "/hello/{name}", method = HttpMethod.GET)
public String hello(@PathVariable("name") String name) { ...}
```

### `@RequestParam`

Binds a query parameter from the URL to a method parameter.

```java

@Endpoint(path = "/add", method = HttpMethod.GET)
public int add(@RequestParam("a") int a, @RequestParam("b") int b) { ...}
```

### `@RequestBody`

Binds the body of the query to a method parameter.

```java

@Endpoint(path = "/echo", method = HttpMethod.POST)
public MyObject echoBody(@RequestBody MyObject myObject) {
    return myObject;
}
```

### `@Response`

Allows to specify the response status code and an optional message.
If the method is executed successfully, the defined status code will be returned.
Also, if the method is void, you can specify the message to return.
The default status code is 200.

**1. No message and not void method :**

The following example returns the created object with a 201 status code in headers when the method is executed
successfully.

```java

@Endpoint(path = "/create", method = HttpMethod.POST)
@Response(status = 201)
public Object createdRandomObject() {...}
```

**2. No message and void method :**

The following example returns nothing but 201 status code in headers when the method is executed successfully.

```java

@Endpoint(path = "/create", method = HttpMethod.POST)
@Response(status = 201)
public void createdRandomObject() {...}
```

**3. Message and not void method :**

The following example returns the created object with a 201 status code in headers when the method is executed
successfully.

```java

@Endpoint(path = "/create", method = HttpMethod.POST)
@Response(status = 201, message = "Object created successfully")
public Object createdRandomObject() {...}
```

**4. Message and void method :**
The following example returns the message and a 201 status code in headers when the method is executed successfully.

```java

@Endpoint(path = "/create", method = HttpMethod.POST)
@Response(status = 201, message = "Object created successfully")
public void createdRandomObject() {...}
```

**Response**

```json
{
    "message": "Object created successfully"
}
```