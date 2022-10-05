1. Проверьте что запущен **Docker Engine**

    
    sudo systemctl status docker

2. Если статус указывает, что сервис не запущен, будет вот такое сообщение:

    
    Active: inactive (dead) ...

3. Тогда проверьте **context**. Это нужно в том случае, если вы параллельно
установили **Docker Desktop**. У вас можеть быть **context** ->  *default* и **context** ->  *desktop-linux*.
  Для использования **Docker Engine**, нужно выбрать **context** ->  *default*

    
    docker context use default


5. Запустите **Docker Engine**

    
    sudo systemctl start docker.service  && sudo systemctl start containerd.service &&  sudo systemctl status docker



6. Проверьте, что сервис запущен

    
    sudo systemctl status docker

Вывод будет таким:   

    Active: active (running)...

7. Теперь можно запускать docker-compose, чтобы развернуть нужные базы данных.

    
    docker-compose -f docker-compose.yaml up

8. Создайте файл **application-\*.yaml** c нужным вам префиксом и укажите имя профиля
    в конфигурационных настройках проекта.

 
    Run/EditConfiguration ...
 
Далее выбираете конфигурацию, которая запускает ваш проект и в ней выбираете

 
    Modify options/Environment variables
 
 Здесь указываете имя вашего профиля, 
через который вы будете локально запускать приложение (например)

    Environment variables: spring.profiles.active=dev