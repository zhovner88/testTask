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
│       └── com/petstore/tests/ 
│           ├── store/      # Store API tests (organized by domain)
│           ├── BaseApiTest.java # Base test class
│           └── PetTests.java    # Pet API tests
├── gradle/lib.versions.toml    # Version catalog
└── settings.gradle             # Multi-module config
```

### Підхід до тестування
- **Service Layer Pattern** - інкапсуляція REST API викликів
- **AssertableResponse** - custom fluent API для перевірок з Allure integration
- **Factory Pattern** - генерація тестових даних (OrderFactory, UserFactory)
- **Паралельне виконання** - TestNG methods-level parallelism

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

## Відомі проблеми 

1. ~~При переміщені тестових класів в окремі модулі, BASE_URL ініціалізується як localhost~~ - **Вирішено**: змінено static @BeforeClass на non-static

## Gradle Tasks

### Verification tasks:
- `test` - Run tests
- `showHtmlReport` - Open Gradle test report 
- `showTestNGReport` - Open TestNG report
- `allureServe` - Generate and serve Allure report

### Build tasks:
- `build` - Build project
- `clean` - Clean build artifacts

## Розширення фреймворку та додавання нових тестів:

- **Нові тести**: додати в `api-tests/src/test/java/com/petstore/tests/`
- **Нові API services**: створити в `api-tests/src/main/java/com/petstore/api/services/`
- **Нові моделі**: додати в `api-tests/src/main/java/com/petstore/api/model/`
- **Нові модулі**: додати до `settings.gradle`

---
**Вимоги**: Java 17+ (з вищими версіями будуть проблеми із сумісністю бібліотек, міграція на Java 21+ наприклад), Gradle 7.0+

**Version Catalog**: `gradle/lib.versions.toml` - централізоване управління залежностями



### Керування тестовими даними через автоматизовану генерацію POJO класів:

RoboPOJOGenerator: Правий клік → New → Generate POJO from JSON
Оптимізація: Генеруються тільки поля, Lombok забезпечує getters/setters
Налаштування: Обов'язково увімкнути "Enable annotation processing" в IntelliJ.

**Custom Conditions**
Гнучкий підхід для складних сценарієв з покращеним логуванням:

**CI/CD інтеграція:** Кожна перевірка окремо в консольних логах
Інформативність: 'Body field '%s' should match %s' - зрозуміло що перевіряється
Розширюваність: Додаткові метрики + логування для non-technical users
Переваги над Rest Assured: Детальніше логування кожної перевірки

### Покриття тестів
#### Store API:
- **StoreTests** - основні CRUD операції (store/)
- **StoreEdgeCasesTests** - граничні випадки та boundary testing
- **StoreErrorTests** - обробка помилок, validation, security tests

#### Security Testing:
- **SQL Injection** - тести на SQL injection в order ID параметрах
- **XSS Testing** - перевірка обробки небезпечних скриптів в даних замовлень
- **Input Validation** - тести на некоректні типи даних та граничні значення

### Test Design Strategy

**GET /store/inventory:**

- Позитивні: валідний API key, структура відповіді, performance
- Негативні: відсутність/невалідний API key, некоректні headers

**POST /store/order:**

* Позитивні: валідні дані, різні статуси (placed/approved/delivered), мінімальні поля
* Негативні: відсутні поля, неіснуючий petId, некоректні enum значення
* Edge cases: граничні значення quantity, спеціальні символи

**GET /store/order/{orderId}:**

* Boundary focus: ID діапазон 1-10 (згідно API документації)
* Позитивні: валідні ID в межах діапазону
* Негативні: ID поза діапазоном (0, 11+), неіснуючі ID

**DELETE /store/order/{orderId}:**

* Позитивні: видалення існуючих orders
* Негативні: негативні ID, неіснуючі orders, повторне видалення

**Integration Testing:**

Lifecycle тести: POST → GET → DELETE → GET(404)
Повна інтеграція Store API workflow з перевіркою consistency даних