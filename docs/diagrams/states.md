# 📊 Диаграммы состояний Cookbook

### 1. Состояние рецепта
```mermaid
stateDiagram-v2
    [*] --> Draft
    Draft --> Published : Сохранить как опубликованный
    Draft --> Archived : Удалить черновик
    Published --> Draft : Редактировать
    Published --> Archived : Архивировать
    Archived --> Published : Восстановить
    Archived --> [*] : Удалить навсегда
```

### 2.Процесс приготовления
```mermaid
stateDiagram-v2
    [*] --> NotStarted
    NotStarted --> InProgress : Начать приготовление
    InProgress --> StepActive : Перейти к шагу
    
    state "Активный шаг" as StepActive {
        [*] --> Instruction
        Instruction --> TimerRunning : Запустить таймер
        TimerRunning --> TimerFinished : Таймер завершен
        TimerFinished --> Instruction : Показать инструкцию
        Instruction --> [*] : Завершить шаг
    }
    
    StepActive --> StepCompleted : Завершить шаг
    StepCompleted --> InProgress : Следующий шаг
    StepCompleted --> Completed : Последний шаг завершен
    InProgress --> Paused : Приостановить
    Paused --> InProgress : Продолжить
    Completed --> [*] : Завершить
```

### 3.Состояние пользовательской сессии
```mermaid
stateDiagram-v2
    [*] --> Anonymous
    Anonymous --> Authenticating : Вход в систему
    Authenticating --> Authenticated : Успешная аутентификация
    Authenticating --> Anonymous : Ошибка аутентификации
    Authenticated --> Cooking : Запуск режима приготовления
    Authenticated --> Editing : Редактирование рецепта
    Authenticated --> Browsing : Просмотр рецептов
    Cooking --> Authenticated : Завершение приготовления
    Editing --> Authenticated : Сохранение рецепта
    Browsing --> Authenticated : Возврат в меню
    Authenticated --> Anonymous : Выход из системы
