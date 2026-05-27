# MyCar — Vehicle Inspection Tracker

Android-приложение для отслеживания состояния автомобиля. Позволяет фиксировать повреждения кузова при каждой инспекции и сравнивать их с baseline — первоначальным состоянием автомобиля.

---

## Возможности

- **Управление автомобилями** — добавление авто с основными данными
- **Инспекции** — создание инспекций с пометкой повреждений на схеме кузова
- **Baseline** — фиксация эталонного состояния, с которым сравниваются последующие инспекции
- **История** — хронология всех инспекций по каждому авто
- **Офлайн-режим** — локальное хранение данных в Room, синхронизация с сервером при наличии сети

---

## Архитектура

Проект построен по принципам **Clean Architecture** с разделением на модули:

```
app/       — UI-слой (Fragments, ViewModels, Navigation)
domain/    — бизнес-логика (Use Cases, модели, интерфейсы репозиториев)
data/      — реализация репозиториев, Room БД, Retrofit API, маппинг DTO ↔ Domain
```

### Схема потока данных

```
Fragment → ViewModel → UseCase → Repository
                                     ↓            ↓
                                  Room DB     Retrofit (FastAPI)
```

---

## Стек технологий

### Android
| Категория       | Технология                              |
|-----------------|-----------------------------------------|
| Язык            | Kotlin                                  |
| DI              | Hilt                                    |
| Асинхронность   | Kotlin Coroutines + Flow                |
| Сеть            | Retrofit + OkHttp                       |
| БД              | Room (SQLite)                           |
| Навигация       | Navigation Component                    |
| Архитектура     | MVVM + Clean Architecture               |
| UI              | ViewBinding, Material Design            |

### Backend
| Категория  | Технология          |
|------------|---------------------|
| Язык       | Python              |
| Framework  | FastAPI             |
| ORM        | SQLAlchemy          |
| БД         | SQLite / PostgreSQL  |

---

## Ключевые технические решения

- **UUID как идентификаторы** — все сущности используют UUID-строки вместо автоинкрементных Long ID, что обеспечивает уникальность при офлайн-создании и последующей синхронизации с сервером
- **Sync-логика** — локально созданные записи синхронизируются с backend'ом; состояние синхронизации отслеживается флагом на уровне Room-сущности
- **Foreign key constraints** — включены в Room с `onDelete = CASCADE` для обеспечения целостности данных между авто, инспекциями и повреждениями

---

## Запуск проекта

### Android
1. Клонировать репозиторий
2. Открыть в Android Studio (Hedgehog или новее)
3. В `data/src/main/.../api/ApiConfig.kt` указать URL backend-сервера
4. Запустить на эмуляторе или устройстве (minSdk 26)

### Backend
```bash
git clone https://github.com/MoAlqw/MyCar-backend  # если вынесен отдельно
cd backend
pip install -r requirements.txt
uvicorn main:app --reload
```

---

## Структура БД (основные сущности)

```
Vehicle          Inspection           DamageMark
--------         ----------           ----------
id (UUID)  ←──  vehicle_id (FK)      inspection_id (FK)
brand            created_at           x, y (координаты на схеме)
model            notes                severity
year             is_synced            is_baseline_diff
is_synced        baseline_id (FK?)
```

---

## Статус проекта

🚧 В активной разработке. Базовый функционал готов, ведётся работа над улучшением sync-логики и UI инспекций.
