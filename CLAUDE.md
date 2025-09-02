# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Gradle-based Java project designed for REST API testing, specifically targeting the Petstore API. The project uses a multi-module structure with the main testing logic contained in the `api-tests` module.

## Architecture

- **Root Project**: `testTask` - Main project container
- **Submodule**: `api-tests` - Contains all API test implementations
- **Test Framework**: TestNG with parallel execution support
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

# Open Gradle HTML test report in browser
./gradlew showHtmlReport

# Open TestNG HTML test report in browser  
./gradlew showTestNGReport

# Generate Allure report
./gradlew allureServe
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

- **Java Version**: Java 17 (source and target compatibility)
- **TestNG Version**: 7.8.0 with parallel execution support
- **Repository**: Maven Central for dependencies
- **Test Runner**: TestNG with methods-level parallelism
- **Parallel Execution**: Methods run in parallel using available CPU cores

## Development Notes

- All test implementations should go in the `api-tests/src/test/java` directory
- Shared test utilities and base classes should be placed in `api-tests/src/main/java`
- Configuration files and test data should be stored in respective `resources` directories
- The project uses Gradle's standard directory layout
- TestNG is configured as the testing framework with parallel execution
- TestNG configuration file: `api-tests/src/test/resources/testng.xml`
- Use `@Test` annotations from `org.testng.annotations.Test`
- Use `@BeforeClass` instead of JUnit's `@BeforeAll`
- Use `@Test(enabled = false)` instead of JUnit's `@Disabled`