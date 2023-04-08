# misc 



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

## Java 泛型通配符使用
- An "in" variable is defined with an upper bounded wildcard, using the extends keyword. 
- An "out" variable is defined with a lower bounded wildcard, using the super keyword.
- In the case where the "in" variable can be accessed using methods defined in the Object class, use an unbounded wildcard.
- In the case where the code needs to access the variable as both an "in" and an "out" variable, do not use a wildcard.

`pecs`即生产者使用extend,消费者使用super. 生产者可以看做只读.[^1]


## NMT (Native Memory Tracking)

enable native memory tracking `-XX:NativeMemoryTracking=summary` or `-XX:NativeMemoryTracking=detail`.
```sh
jcmd pid VM.native_memory baseline
jcmd pid VM.native_memory detail.diff 
```

### Native Memory Tracking Memory Categories 

<table cellpadding="4" cellspacing="0" class="Formal" title="Native Memory Tracking Memory Categories" summary="This table describes native memory tracking memory categories" width="100%" frame="hsides" border="1" rules="rows">
                           <thead>
                              <tr align="left" valign="top">
                                 <th align="left" valign="bottom" width="30%" id="d2672e1987">Category</th>
                                 <th align="left" valign="bottom" width="70%" id="d2672e1990">Description</th>
                              </tr>
                           </thead>
                           <tbody>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e1995" headers="d2672e1987 ">
                                    <p>Java Heap</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e1995 d2672e1990 ">
                                    <p>The heap where your objects live</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2002" headers="d2672e1987 ">
                                    <p>Class</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2002 d2672e1990 ">
                                    <p>Class meta data</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2009" headers="d2672e1987 ">
                                    <p>Thread</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2009 d2672e1990 ">
                                    <p>Memory used by threads, including thread data structure, resource area, handle area, and so on</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2016" headers="d2672e1987 ">
                                    <p>Code</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2016 d2672e1990 ">
                                    <p>Generated code</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2023" headers="d2672e1987 ">
                                    <p>GC</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2023 d2672e1990 ">
                                    <p>Data use by the GC, such as card table, except the remembered sets </p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2031" headers="d2672e1987 ">
                                    <p>GCCardSet</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2031 d2672e1990 ">
                                    <p>Data use by the GC's remembered sets (optional, G1 only)</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2042" headers="d2672e1987 ">
                                    <p>Compiler</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2042 d2672e1990 ">
                                    <p>Memory tracking used by the compiler when generating code</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2049" headers="d2672e1987 ">
                                    <p>Internal</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2049 d2672e1990 ">
                                    <p>Memory that does not fit the previous categories, such as the memory used by the command line parser, JVMTI, properties, and so on</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2056" headers="d2672e1987 ">
                                    <p>Other</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2056 d2672e1990 ">
                                    <p>Memory not covered by another category</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2063" headers="d2672e1987 ">
                                    <p>Symbol</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2063 d2672e1990 ">
                                    <p>Memory for symbols</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2070" headers="d2672e1987 ">
                                    <p>Native Memory Tracking</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2070 d2672e1990 ">
                                    <p>Memory used by NMT</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2078" headers="d2672e1987 ">
                                    <p>Arena Chunk</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2078 d2672e1990 ">
                                    <p>Memory used by chunks in the arena chunk pool</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2085" headers="d2672e1987 ">
                                    <p>Logging</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2085 d2672e1990 ">
                                    <p>Memory used by logging</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2092" headers="d2672e1987 ">
                                    <p>Arguments</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2092 d2672e1990 ">
                                    <p>Memory for arguments</p>
                                 </td>
                              </tr>
                              <tr align="left" valign="top">
                                 <td align="left" valign="top" width="30%" id="d2672e2099" headers="d2672e1987 ">
                                    <p>Module</p>
                                 </td>
                                 <td align="left" valign="top" width="70%" headers="d2672e2099 d2672e1990 ">
                                    <p>Memory used by modules</p>
                                 </td>
                              </tr>
                           </tbody>
                        </table>
                        
                        
---
[^1]: https://dev.java/learn/generics/wildcards/#:~:text=of%20Double%20values.-,Guidelines%20for%20Wildcard%20Use,-One%20of%20the
