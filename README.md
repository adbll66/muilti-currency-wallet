# Multi-Currency Wallet with AI Advisor

Полнофункциональное финтех-приложение (MVP) для управления мультивалютными счетами, проведения денежных переводов, конвертации валют и получения аналитики накопительных счетов с помощью локального искусственного интеллекта.
 Ключевые возможности
* Мультивалютные счета: Открытие и ведение счетов в популярных валютах (USD, EUR, RUB, KGS).
* Финансовые операции:
  * Пополнение баланса счетов.
  * Мгновенные переводы между пользователями.
  * Конвертация средств между счетами.
* История и отчетность:
  * Детальный лог транзакций для каждого счета.
  * Выгрузка банковских выписок в формате CSV.
* Визуализация данных: Интерактивная круговая диаграмма распределения активов (Chart.js).
* ИИ Финансовый советник: 
  * Интеграция с локальной языковой моделью (Ollama / Llama 3) через Spring AI.
  * Автоматический анализ балансов и предоставление персональных рекомендаций по управлению накоплениями без использования сторонних платных API.

## Технологический стек
### Backend
* Java 17
* Spring Boot 3.2.2
* Spring Data JPA (Hibernate)
* Spring Security
* Spring AI (Ollama Starter)
* PostgreSQL (База данных)
* Gradle (Сборка проекта)

### Frontend
* HTML5 / CSS3 (Custom Modern UI, CSS Variables, Dark Mode support)
* JavaScript (Async / Fetch API)
* Chart.js (Визуализация графиков)

## Инструкция по запуску проекта

### 1. Требования
Убедитесь, что у вас установлены:
* JDK 17 или выше
* PostgreSQL
* Ollama (для работы ИИ-советника)

### 2. Настройка ИИ (Ollama)
1. Скачайте и установите Ollama (https://ollama.com/).
2. Запустите модель llama3 в консоли:
   ```bash
   ollama run llama3
  
###  3. Настройка базы данных
spring.datasource.url=jdbc:postgresql://localhost:5432/wallet_db
spring.datasource.username=your_postgres_user
spring.datasource.password=your_postgres_password

spring.jpa.hibernate.ddl-auto=update

### 4. Запуск приложения
Склонируйте репозиторий и запустите проект через Gradle:
# Клонирование репозитория
git clone [https://github.com/adbll66/muilti-currency-wallet.git](https://github.com/adbll66/muilti-currency-wallet.git)

# Переход в папку проекта
cd muilti-currency-wallet

# Запуск приложения
./gradlew bootRun

REST API Endpoints
GET /api/wallets/user/{userId} — Получение списка счетов пользователя
POST /api/wallets?userId={id}&currency={code} — Открытие нового счета
POST /api/wallets/{id}/deposit?amount={sum} — Пополнение счета
POST /api/wallets/transfer — Перевод между счетами
POST /api/wallets/convert — Конвертация валют
GET /api/transactions/wallet/{walletId} — История операций по счету
GET /api/transactions/wallet/{walletId}/export — Выгрузка выписки в CSV
GET /api/ai/advice/{userId} — Запрос ответа ИИ-советника

Автор 
GitHub: @adbll66
