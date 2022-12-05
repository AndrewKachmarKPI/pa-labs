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

## Lab 2
Lab 2 contains 2 runners for each algorithm
> #### Runners folder 
> **lab2/src/com/labs/** 
>> * LDFSRunner.java
>> * AStarRunner.java
_________________
To be able to run code you should have prepared environment installed JDK and JVM \
Also you can check your java installation executing
> java -version

To run code you should execute such commands (from command line in folder with runners)
FROM SRC FOLDER
> #### Compile 
> javac com/labs/*.java -d classes

> #### Run
>  java -cp classes com.labs.[RUNNER NAME]

> #### Run Tests
>  mvn '-Dtest=com.labs.*[TEST RUNNER NAME]' test

from root of project

## Lab 4
> #### Runners folder 
> **lab4/src/com/labs/** 
>> * ApplicationRunnerLab4.java


> #### Compile 
> javac com/labs/*.java -d classes

from src folder

> #### Run Application
>  java -cp classes com.labs.ApplicationRunnerLab4

> #### Run Tests
>  mvn '-Dtest=com.labs.*[TEST RUNNER NAME]' test

from root of project

