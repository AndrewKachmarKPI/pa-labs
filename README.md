# Algorithms design

## Lab links
| Lab code  |  Lab report  |
|:---------:|:------------:|
| [Lab 1](lab-1) | [Report 1](lab-1/lr1_report.pdf) |
| [Lab 2](lab-2) | [Report 2](lab-2/lr2_report.pdf) |
| [Lab 3](lab-3) | [Report 3](lab-3/lr3_report.pdf) |
| [Lab 4](lab-4) | [Report 4](lab-4/lr4_report.pdf) |
| [Lab 5](lab-5) | [Report 5](lab-5/lr5_report.pdf) |
| [Lab 6](lab-6) | [Report 6](lab-6/lr6_report.pdf) |

## Run guide
* [JDK install](https://phoenixnap.com/kb/install-java-windows)
* [JVM install](https://www.java.com/en/download/manual.jsp)
* [Maven install](https://mkyong.com/maven/how-to-install-maven-in-windows)
* IDE
  * [Intellij IDEA](https://www.jetbrains.com/idea/download/)
  * [Eclipse](https://www.eclipse.org/downloads/)

## Runner guide
_________________  
To be able to run code and tests you should have prepared environment installed JDK, JVM and Maven  

Also you can check your java installation executing
> java -version


Also you can check your maven installation executing
> mvn -version


## Lab2
To run code you should execute such commands
> #### Compile  FROM (**\lab-2\src\main\java) folder of lab
>  javac com/labs/*.java -d classes

> #### Run  FROM (**\lab-2\src\main\java) folder of lab
> ##### Run A* algorithm
>  java -cp classes com.labs.AStarRunner
> ##### Run LDFS algorithm
>  java -cp classes com.labs.LDFSRunner

> #### Run Tests  FROM (**\lab-2) folder of lab
>  mvn clean test

## Lab4
To Build and Run code you should execute such commands
> #### Build code  FROM (**\lab-4) folder of lab
>  mvn clean package -DskipTests

> #### Run code  FROM (**\lab-4) folder of lab
>  java -jar target/pa-labs-0.0.1-SNAPSHOT.jar

> #### Run Tests  FROM (**\lab-4) folder of lab
>  mvn clean test

## Lab5
To Build and Run code you should execute such commands

> #### Build code  FROM (**\lab-5) folder of lab
>  mvn clean package -DskipTests

> #### Run code  FROM (**\lab-5) folder of lab
>  java -jar target/pa-labs-0.0.1-SNAPSHOT.jar

> #### Run Tests  FROM (**\lab-5) folder of lab
>  mvn clean test
