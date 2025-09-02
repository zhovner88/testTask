# testTask - Petstore API Test Framework

## Огляд

Автоматизовані API тести для Swagger Petstore з використанням Java 17, Rest Assured, Gradle, TestNG та багатомодульною архітектурою.

## Архітектура

### Технології
- Java 17, Rest Assured 5.5.0, TestNG 7.8.0, Gradle
- Allure Reports 2.25.0, SLF4J + Logback, Lombok, Jackson

### Структура проекту
```
testTask/                    # Root project
├── api-tests/              # Test module
│   ├── src/main/java/
│   │   ├── com/petstore/api/
│   │   │   ├── services/   # StoreApiService, UserApiService  
│   │   │   ├── model/      # Order, User POJOs
│   │   │   └── conditions/ # Custom validation conditions
│   │   ├── factory/        # OrderFactory, UserFactory
│   │   ├── common/         # Constants
│   │   └── utils/          # Utilities
│   └── src/test/java/
│       └── com/petstore/tests/ # Test classes
├── gradle/lib.versions.toml    # Version catalog
└── settings.gradle             # Multi-module config
```

### Підхід до тестування
- **Service Layer Pattern** - інкапсуляція REST API викликів
- **AssertableResponse** - custom fluent API для перевірок з Allure integration
- **Factory Pattern** - генерація тестових даних (OrderFactory, UserFactory)
- **Паралельне виконання** - TestNG methods-level parallelism

### Покриття тестів
- **StoreTests** - основні CRUD операції
- **StoreEdgeCasesTests** - граничні випадки
- **StoreErrorTests** - обробка помилок та validation

## Завантажити проект

```bash
git clone git@github.com:zhovner88/testTask.git
cd testTask
./gradlew build
```

**IDE налаштування**: Lombok plugin + "Enable annotation processing"
(для IntelliJ)

## Запуск тестів

```bash
./gradlew test                         # Всі тести
./gradlew :api-tests:test             # Тільки api-tests модуль
./gradlew test --tests "*Store*"      # Store тести
./gradlew test --info                 # Детальний вивід
./gradlew test --continuous           # Continuous mode
```

## Звіти

```bash
./gradlew showHtmlReport      # Gradle HTML звіт
./gradlew showTestNGReport    # TestNG HTML звіт  
./gradlew allureServe         # Allure звіт
```

**Розташування звітів:**
- Gradle: `api-tests/build/reports/tests/html/index.html`
- TestNG: `api-tests/build/test-output/index.html`

## Конфігурація

### Constants.java
```java
public static final String BASE_URI = "https://petstore.swagger.io/v2";
public static final String SPECIAL_API_KEY = "special-key";
```

### TestNG паралельне виконання тестів 
- Methods-level parallelism
- Thread count = доступні CPU cores
- Конфігурація: `api-tests/src/test/resources/testng.xml`

### Логування
- **SLF4J + Logback**
- Конфігурація: `api-tests/src/main/resources/logback.xml`
- RestAssured requests/responses логуються автоматично

## Формат API помилок

API повертає помилки у форматі:
```json
{
    "code": 404,
    "type": "unknown", 
    "message": "Order Not Found"
}
```

## Відомі обмеження

1. **Shared Environment** - використовується спільне тестове середовище
2. При переміщені тестових класів в окремі модулі, BASE_URL ініціалізується як localhost

## Gradle Tasks

### Verification tasks:
- `test` - Run tests
- `showHtmlReport` - Open Gradle test report 
- `showTestNGReport` - Open TestNG report
- `allureServe` - Generate and serve Allure report

### Build tasks:
- `build` - Build project
- `clean` - Clean build artifacts

## Розширення

- **Нові тести**: додати в `api-tests/src/test/java/com/petstore/tests/`
- **Нові API services**: створити в `api-tests/src/main/java/com/petstore/api/services/`
- **Нові моделі**: додати в `api-tests/src/main/java/com/petstore/api/model/`
- **Нові модулі**: додати до `settings.gradle`

---
**Вимоги**: Java 17+ (з вищими версіями будуть проблеми із сумісністю бібліотек, міграція на Java 21+ наприклад), Gradle 7.0+

**Version Catalog**: `gradle/lib.versions.toml` - централізоване управління залежностями