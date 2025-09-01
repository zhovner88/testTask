# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Gradle-based Java project designed for REST API testing, specifically targeting the Petstore API. The project uses a multi-module structure with the main testing logic contained in the `api-tests` module.

## Architecture

- **Root Project**: `testTask` - Main project container
- **Submodule**: `api-tests` - Contains all API test implementations
- **Test Framework**: JUnit 5 (Jupiter) with JUnit Platform
- **Build Tool**: Gradle with wrapper (gradlew)

## Common Commands

### Building and Testing
```bash
# Build the entire project
./gradlew build

# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :api-tests:test

# Clean build artifacts
./gradlew clean

# Build without running tests
./gradlew build -x test
```

### Development Commands
```bash
# List all available tasks
./gradlew tasks

# Run tests in continuous mode (if configured)
./gradlew test --continuous

# Generate test reports
./gradlew test --info
```

## Project Structure

```
testTask/
├── api-tests/                 # Main testing module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/         # Test utilities and helpers
│   │   │   └── resources/    # Configuration files
│   │   └── test/
│   │       ├── java/         # Test classes
│   │       └── resources/    # Test data and configs
│   └── build.gradle          # Module-specific dependencies
├── build.gradle              # Root project configuration
├── settings.gradle           # Multi-module settings
└── gradlew                   # Gradle wrapper
```

## Key Configuration

- **Java Version**: Uses default JVM (check project requirements)
- **JUnit Version**: 5.10.0 with BOM platform management
- **Repository**: Maven Central for dependencies
- **Test Runner**: JUnit Platform with Jupiter engine

## Development Notes

- All test implementations should go in the `api-tests/src/test/java` directory
- Shared test utilities and base classes should be placed in `api-tests/src/main/java`
- Configuration files and test data should be stored in respective `resources` directories
- The project uses Gradle's standard directory layout
- JUnit 5 is configured as the testing framework with platform launcher