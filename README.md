# learn java use jdk17 



## How a Java Application Can Discover its Process ID (PID)

1. Using the java management and monitoring API (java.lang.management)
```java
  ManagementFactory.getRuntimeMXBean().getName();
```
return like `28906@localhost` where `28906` is the PID of JVM's process, which is in fact the PID of my app.

2. Use <b>JDK9</b> API (java.lang.ProcessHandle)
```java
  ProcessHandle.current().pid();
```

return `53114` this is the PID of JVM's process

3. Using shell script in addition to Java properties Start your app with a shellscript like this:
```sh
exec java -Dpid=$$ -jar /Applications/bsh-2.0b4.jar
```
```java
System.getProperty("pid");
```
4. Using Java Native Interface (JNI) - a very cumbersome and platform dependent solution. 
5. Using $PPID and Runtime.exec(String[]) method - described in detail in this post
```java
import java.io.IOException;

public class Pid {
  public static void main(String[] args) throws IOException {
    byte[] bo = new byte[100];
    String[] cmd = {"bash", "-c", "echo $PPID"};
    Process p = Runtime.getRuntime().exec(cmd);
    p.getInputStream().read(bo);
    System.out.println(new String(bo));
 }
}
```
