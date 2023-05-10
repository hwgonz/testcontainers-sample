# Testcontainers Sample - Build and Run 

## Building the project
This project is built using `sbt`. If you are on mac os you can install it using brew:

```
brew install sbt
```

Build the jars by running:
```shell
sbt clean assembly
```

## Running the testcontainer test

To run the tests, just use this command at the main project root folder, where retailProductService is the sub project 'logical name'.

```shell
sbt retailProductService/it:test 
```


