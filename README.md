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
To be able to run code you should have prepared environment installed JDK and JVM \
To be able to run tests you should install Maven \
Also you can check your java installation executing
> java -version


Also you can check your maven installation executing
> mvn -version

To run code you should execute such commands \
> #### Compile  FROM SRC FOLDER OF LAB
> javac com/labs/*.java -d classes

> #### Run
>  java -cp classes com.labs.[LAB RUNNER NAME]


> #### Run Tests  FROM ROOT OF PROJECT
>  mvn '-Dtest=com.labs.*[TEST RUNNER NAME]' test
