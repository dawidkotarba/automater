![Java CI with Gradle](https://github.com/dawidkotarba/automater/workflows/Java%20CI%20with%20Gradle/badge.svg)

# automater

## Description

A simple Java tool that can help you to automate manual and repetitive processes. Main features:

- can be programmed by simple commands
- can type characters and words
- can move, scroll and click mouse
- can repeat the whole execution plan and wait between steps
- can stop working after certain time
- the execution plan can be started either by UI (`http://localhost:9999`) or REST
  calls (`http://localhost:9999/start`, `http://localhost:9999/stop`)

## Screenshots

- Configuration page:
  ![Configuration page](doc/screenshots/main.png)

- Plan in progress:
  ![Progress](doc/screenshots/progress.png)

- Capture mouse coordinates:
  ![Mouse coordinates](doc/screenshots/coords.png)

- Available steps:
  ![Available steps](doc/screenshots/steps.png)

## Requirements

- JDK 11

## Build

```shell
./gradlew build
```

## Run:

- by using Gradle and Spring Boot run task:
```shell
./gradlew bootRun
```
- ... or just by running the jar (can be downloaded [here](https://github.com/dawidkotarba/automater/releases/latest/download/automater.jar):
```shell
java -jar build/libs/automater.jar
```

Then access the configuration page to start the plan: `http://localhost:9999`.

- To start the app without the access from another computer:
```shell
java -jar build/libs/automater.jar --server.address=127.0.0.1
```

- To change the default `9999` port:
```shell
java -jar build/libs/automater.jar --server.port=8888
```
or
```shell
./gradlew bootRun --args='--server.port=8888'
```

## Start/Stop an execution plan by a REST call:

- start:
  Execute `POST` to `http://localhost:9999/start` with a body in below format:

```json
{
  "sleepBetweenSteps": 100,
  "maxExecutionTimeSecs": 3600,
  "executionLines": [
    "MOUSE moveTo 100 100",
    "SLEEP random 200",
    "MOUSE moveTo 300 300",
    "MOUSE leftClick",
    "MOUSE moveTo 500 500",
    "MOUSE moveTo 800 800",
    "MOUSE rightClick",
    "MOUSE leftClick",
    "SLEEP randomBetween 100 500",
    "MOUSE moveRandom",
    "MOUSE scroll -1",
    "MOUSE scrollRandom 5",
    "SLEEP of 500",
    "KEYBOARD press WINDOWS"
  ]
}
```

- stop:
  Execute `POST` to `http://localhost:9999/stop`.
