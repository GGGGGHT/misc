# learn java use jdk 



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

## Virtual Threads: New Foundations for High-Scale Java Applications
1. 虚拟线程是Java线程轻量级的实现，做为预览功能在jdk19中被交付
2. 虚拟线程极大地减少了编写、维护和观察高吞吐量并发应用程序的工作量。
3. 虚拟线程为熟悉 `thread-per-request` 编程风格注入了新的活力，使其能够以接近最佳的硬件利用率进行扩展。
4. 虚拟线程与现有的 Thread API 完全兼容，因此现有的应用程序和库可以通过最小的更改来支持它们。
5. 虚拟线程支持现有的调试和分析接口，可以使用现有工具和技术轻松地对虚拟线程进行故障排除、调试和分析。
