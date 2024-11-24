## Sex IT

The coolest platfrom about SEX!!!

Made by: Машусик, Ярик, Леха

## Окружение

Java 17
Node >= 20

## Запуск локального окружения

For Mac OS! Нужно везде приписывать файл с маппингом портов
```bash
docker compose -f docker-compose.yml -f docker-compose-macos.yml up
```

1. Перейти в backend  
2. Запустить:  
```bash
./gradlew clean build && docker compose -f docker-compose.yml -f docker-compose-macos.yml up --force-recreate --build
```

```bash
./gradlew clean build && docker compose up --force-recreate --build
```
3. Чтобы запустить `platform` в режиме интеграции с BBB:
   1. `application-dev.yml`:
      ```yaml
      bbb:
        client-mode: real
      ```
   2. Создать `docker-compose-bbb-config.yml`:
      ```yaml
      services:
        platform:
          environment:
            - BBB_SECRET=secret1234
      ```
   3. Запустить docker compose:
      ```bash
      docker compose -f docker-compose.yml -f docker-compose-bbb-config.yml up --force-recreate --build
      ```

## Установка BBB в облаке

см. [доку](./devops/ansible/README.md)

## Обновить dump Keycloak

```bash
docker exec -it sexit-keycloak bash -c "kc.sh export --realm Sex-IT --file /tmp/sexit-realm.json" &&  
docker cp sexit-keycloak:/tmp/sexit-realm.json ./config/sexit-realm.json
```
