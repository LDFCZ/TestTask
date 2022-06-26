# TestTask
To run and test, follow these steps:

Clone project and install all requierd libraries:

```
$ git clone https://github.com/LDFCZ/TestTask.git
$ cd TestTask
$ cd front
$ npm install
```

Go to application.properties and setup your DB url, login, password.

```
spring.datasource.url= your DB url
spring.datasource.username= your login
spring.datasource.password= your password
```

Check Java version (Make sure you using jdk17)

Now you need to do the first backend run.
```
$ mvn clean install 
$ mvn spring-boot: run
```
After that, all the necessary tables will be created in your DB. Now stop backend application and run this SQL script:
```
 LOCK TABLES `roles` WRITE;
 INSERT INTO `roles` (`ID`, `NAME`) VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');
 UNLOCK TABLES;
```
Now you can run full application. Go to ```front``` direcrory and run frontend part.
```
$ npm start
```
Next step is launching backend
```
$ mvn clean install 
$ mvn spring-boot: run
```
Go to the http://localhost:3000 
Here you need to register.

After that you need to next SQL script to chande your user role:
```
UPDATE `user_roles` SET `role_id` = '1' WHERE `user_roles`.`user_id` = 1 AND `user_roles`.`role_id` = 2
```
It will give you admin power and you will be able to moderate banners and categories!
# But remember, with great power comes great responsobility!