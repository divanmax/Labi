# TLS (Задание 6) — цепочка из 3 сертификатов (ID: 22017)

Здесь скрипт-план генерации цепочки:

- Root CA (самоподписанный)
- Intermediate CA (подписан Root)
- Server certificate (подписан Intermediate) для `localhost`

Во всех сертификатах присутствует идентификатор студента **22017** (в `OU=STUDENT-22017`).

## Что генерируется

- `certs/public/root-ca-22017.crt` — Root CA (публичный)
- `certs/public/intermediate-ca-22017.crt` — Intermediate CA (публичный)
- `certs/public/server-22017.crt` — серверный сертификат (публичный)
- `certs/public/server-22017-chain.crt` — цепочка для сервера (server + intermediate + root)
- `certs/private/server-22017.p12` — keystore PKCS12 (приватный ключ + цепочка) **НЕ коммитить**

Приватные ключи и `.p12` должны быть в `.gitignore`.

## Как запустить (Windows PowerShell)

Требования: установлен OpenSSL (например, через Git for Windows или отдельный пакет).

1) Перейди в корень проекта `RBPO-Aviation-service`.

2) Запусти команды из `scripts/tls/generate-chain.ps1`.

## Как включить HTTPS в приложении

Поставь переменные окружения (пример):

- `SERVER_PORT=8443`
- `TLS_ENABLED=true`
- `TLS_KEYSTORE_PATH=<абсолютный путь до server-22017.p12>`
- `TLS_KEYSTORE_PASSWORD=<пароль>`
- (опционально) `TLS_KEY_ALIAS=aviation-22017`

После этого сервис будет доступен на `https://localhost:8443`.

## Доверие в браузере

Импортируй `certs/public/root-ca-22017.crt` в доверенные корневые ЦС Windows (Local Machine → Trusted Root Certification Authorities).

## GitHub Secrets (для CI)

Создай secrets:

- `TLS_KEYSTORE_B64` — base64 от файла `server-22017.p12`
- `TLS_KEYSTORE_PASSWORD` — пароль от `.p12`

CI декодирует keystore во временный файл и запускает Maven (compile/test/package), затем загружает jar как artifact.

