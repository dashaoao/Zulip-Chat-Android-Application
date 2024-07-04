# Чат-приложение на основе Zulip API

Zulip — это групповой чат для совместной работы.
Cообщения в приложении разделяются по каналам, которые, в свою очередь, делятся по темам - топикам.

## Стек:
- Jetpack Compose
- Custom View/ViewGroup для отображения сообщений и реакций
- Kotlin Coroutines
- Retrfoit
- Room
- Elmslie
- Cicerone
- Dagger2
- Kaspresso | Wiremock | Junit

## Фичи:
★ Получение данных из Zulip API<br>
★ Кэширование данных<br>
★ Фичи разделены на модули<br>
★ Ui тесты для экрана чата<br>
★ Unit тесты для бизнес- и ui-логики 

## Архитектура
<img src="https://github.com/dashaoao/Zulip-Chat-Android-Application/assets/113306856/078a82ae-2fac-4e01-9d3c-267ef0d224dc" width="200" align="left" hspace="10">

- Проект представляет собой многомодульное приложение, где каждый модуль соответствует отдельной фиче.<br>
- Архитектура основана на паттерне MVU.<br>
- Библиотека elmslie используется для реализации данного паттерна.

<br clear="left"/>

## Экраны

### Каналы
- Список подписанных каналов
- Список всех каналов
- Поиск по каналам
<img src="https://github.com/dashaoao/Zulip-Chat-Android-Application/assets/113306856/158e8db2-71fb-4628-aa0d-7d7bb6f8ee77" width="400">
<br>
<br>

- Создание нового канала
<img src="https://github.com/dashaoao/Zulip-Chat-Android-Application/assets/113306856/b09a76ee-5589-4277-a8b6-46a6a3e51529" width="300">
<br>
<br>

- По клику на канал открывается список его топиков
<img src="https://github.com/dashaoao/Zulip-Chat-Android-Application/assets/113306856/a9063ac4-474c-4ae4-9036-248b14104546" width="300">
<br>

### Чат
- bottom sheet с доступными реакциями
<img src="https://github.com/dashaoao/Zulip-Chat-Android-Application/assets/113306856/40db56dc-1c02-4a80-b1d1-9c7255513eb4" width="300">
<br>
<br>

По лонг тапу открывается bottom sheet и там можно:
- Поставить реакцию
- Удалить сообщение
- Отредактировать сообщение
- Поменять топик у этого сообщения
- Скопировать сообщение в буфер обмена
<img src="https://github.com/dashaoao/Zulip-Chat-Android-Application/assets/113306856/585ed089-b58e-48d9-b907-65936ddededb" width="300">
<br>

### Профиль
- Список контактов
- Стутус пользователя
- Поиск пользователей
- Переход на экран пользователя
<img src="https://github.com/dashaoao/Zulip-Chat-Android-Application/assets/113306856/feb0979e-4584-40ba-94a0-8cc69ab4b306" width="400">
<br>
<br>

- Экран собственного профиля
<img src="https://github.com/dashaoao/Zulip-Chat-Android-Application/assets/113306856/dcd63444-0295-4e18-89c4-c079e8e1d43b" width="200">
