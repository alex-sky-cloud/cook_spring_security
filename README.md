# cook_spring_security

1. Для того, чтобы **Authorization server** другие сервисы могли обнаружить по имени - **auth-server**,
на локальном ПК, вам нужно в файле hosts (Windows : **c:\Windows\System32\drivers\etc\hosts**) установить
   (Выполняется по правами администратора ОС)

    
    127.0.0.1 auth-server   

2. Для пакета spring-security, нужно изментить версию одной транзитивной зависимости,
так как в ней найдена уязвимость

```yaml
       <!-- изменение версии транзитивной зависимости по умолчанию для
        Spring security-->
        <dependency>
            <groupId>org.yaml</groupId>
            <artifactId>snakeyaml</artifactId>
            <version>${version.snakeyaml}</version>
        </dependency>
```
 
Затем нужно выполнить перезагрузку maven проекта (Download sources or Reload project).