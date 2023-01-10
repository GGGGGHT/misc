# Arthas[^2]
Java 应用诊断利器

## 功能 

- Dashboard  实时查看系统的运行状况
- watch 查看函数调用的参数，返回值和异常
- retransform 在线热更新
- trace 方法内部调用路径，并输出方法路径上的每个节点上耗时
- WebConsole 在线诊断，点开网页诊断线上应用 需要额外配置

## command[^3]

#### watch 

<table><thead><tr><th style="text-align:right;">参数名称</th><th style="text-align:left;">参数说明</th></tr></thead><tbody><tr><td style="text-align:right;"><em>class-pattern</em></td><td style="text-align:left;">类名表达式匹配</td></tr><tr><td style="text-align:right;"><em>method-pattern</em></td><td style="text-align:left;">函数名表达式匹配</td></tr><tr><td style="text-align:right;"><em>express</em></td><td style="text-align:left;">观察表达式，默认值：<code>{params, target, returnObj}</code></td></tr><tr><td style="text-align:right;"><em>condition-express</em></td><td style="text-align:left;">条件表达式</td></tr><tr><td style="text-align:right;">[b]</td><td style="text-align:left;">在<strong>函数调用之前</strong>观察</td></tr><tr><td style="text-align:right;">[e]</td><td style="text-align:left;">在<strong>函数异常之后</strong>观察</td></tr><tr><td style="text-align:right;">[s]</td><td style="text-align:left;">在<strong>函数返回之后</strong>观察</td></tr><tr><td style="text-align:right;">[f]</td><td style="text-align:left;">在<strong>函数结束之后</strong>(正常返回和异常返回)观察</td></tr><tr><td style="text-align:right;">[E]</td><td style="text-align:left;">开启正则表达式匹配，默认为通配符匹配</td></tr><tr><td style="text-align:right;">[x:]</td><td style="text-align:left;">指定输出结果的属性遍历深度，默认为 1，最大值是 4</td></tr>
    <tr><td style="text-align:right;">[n]</td><td style="text-align:left;">指定观察的次数 e.g. -n 1 表示只输出一次 </td></tr>
    </tbody></table>


### 表达式核心变量


|变量名|	变量解释|
|--|--|
|loader|	本次调用类所在的 ClassLoader|
|clazz|	本次调用类的 Class 引用|
|method|	本次调用方法反射引用|
|target|	本次调用类的实例|
|params|	本次调用参数列表，这是一个数组，如果方法是无参方法则为空数组|
|returnObj|	本次调用返回的对象。当且仅当 isReturn==true 成立时候有效，表明方法调用是以正常返回的方式结束。如果当前方法无返回值 void，则值为 null|
|throwExp|	本次调用抛出的异常。当且仅当 isThrow==true 成立时有效，表明方法调用是以抛出异常的方式结束。|
|isBefore|	辅助判断标记，当前的通知节点有可能是在方法一开始就通知，此时 isBefore==true 成立，同时 isThrow==false 和 isReturn==false，因为在方法刚开始时，还无法确定方法调用将会如何结束。|
|isThrow	|辅助判断标记，当前的方法调用以抛异常的形式结束。|
|isReturn	|辅助判断标记，当前的方法调用以正常返回的形式结束。|

1. 查看第一个参数 watch Test test params[0] -n 1
2. 查看第一个参数(集合)中的第一个元素 
    - watch Test test params[0][0] -n 1
    - watch Test test params[0].get(0) -n 1
3. 查看第一个参数(集合)中第一个元素的属性 
    - watch Test test params[0].get(0).age -n 1
    - watch Test test **'** params[0][0]["age"] **'** -n 1

4. 获取所有的name  watch Test test params[0].{name} -n 1
5. 集合过滤
    - 过滤所有的age大于5的name watch Test test "params[0].{? #this.age > 5}.{name}" -n 1 
       > 其中{? #this.age > 5} 相当于stream里面的filter，后面的name相当于stream里面的map

    - 找到第一个age大于5的Pojo的name watch Test test "params[0].{^ #this.age > 5}.{name}" -n 1
    - 找到最后一个age大于5的Pojo的name watch Test test "params[0].{$ #this.age > 5}.{name}" -n 1

6. 多行表达式
    - 需要取所有的name 并在结果中增加一个新的值 并返回　watch Test test '(#test=params[0].{name}, #test.add("abc"), #test)' -n 1
    - 需要取所有的age 返回三个集合　一个中是所有的年龄　一个中是基数　另一个是偶数　watch Test test '#ages=test.params[0].{age}, #odd=new java.util.HashSet(), #even = new java.util.HashSet(), #odd.addAll(#ages.{? #this % 2 != 0}) #even.addAll(#ages.{? #this % 2 == 0}), {#ages, #odd, #even}' -n 1
        

7. 访问静态变量 
    - watch  Test test '@Test@m' -n 1
    - ognl '@Test@m' -n 1
    - getstatic Test m 
   > 可以通过`@class@filed`方式访问，需要填写全类名

8. 调用静态方法
    - watch Test test '@java.lang.System@getProperty("java.version")' -n 1
    - ognl '@java.lang.System@getProperty("java.version")' -n 1

## ognl[^1]


## profiler 


## misc

### ongl demo 
```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Test {

    public static final Map m = new HashMap<>();
    public static final Map n = new HashMap<>();

    static {
        m.put("a", "aaa");
        m.put("b", "bbb");

        n.put(Type.RUN, "aaa");
        n.put(Type.STOP, "bbb");
    }

    public static void main(String[] args) throws Exception {
        // System.out.println(ProcessHandle.current().pid());
        List<Pojo> list = new ArrayList<>();

        for (int i = 0; i < 40; i ++) {
            Pojo pojo = new Pojo();
            pojo.setName("name " + i);
            pojo.setAge(i + 2);

            list.add(pojo);
        }

        while (true) {
            int random = new Random().nextInt(40);

            String name = list.get(random).getName();
            list.get(random).setName(null);

            try {

                test(list, random);
            } catch (Exception e) {
                
            }

            list.get(random).setName(name);

            Thread.sleep(1000l);
        }
    }

    public static void test(List<Pojo> list, int i) throws Exception {
        if ((i & 1) == 0) {
            throw new Exception("run time");
        }
    }

    public static void invoke(String a) {
        System.out.println(a);
    }

    static class Pojo {
        String name;
        int age;
        String hobby;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }
    }
}

enum Type {
    RUN, STOP;
}

```

### Web console config[^4]
1. 下载部署 [arthas tunnel server](https://github.com/alibaba/arthas/releases/download/arthas-all-3.6.7/arthas-tunnel-server-3.6.7-fatjar.jar)
2. 启动fatjar java -Darthas.enable-detail-pages=true -Dserver.port=9090 -jar arthas-tunnel-server-3.6.7-fatjar.jar
3. 默认情况下，arthas tunnel server 的 web 端口是8080，arthas agent 连接的端口是7777。 启动之后，可以访问 http://127.0.0.1:8080/ ，再通过agentId连接到已注册的 arthas agent 上。通过http://localhost:9090/apps.html可以查看到所有的已经注册的服务.
4. 启动 arthas 时连接到 tunnel server 

    1. shell
    ```sh
    as.sh --tunnel-server 'ws://127.0.0.1:7777/ws' --app-name xxx
    ```
    2. use jar 
    ```sh
    java -jar arthas-boot.jar --tunnel-server 'ws://127.0.0.1:7777/ws' --app-name xxx
    ```
    3. 项目启动直接attach

   ```pom
    <dependency>
        <groupId>com.taobao.arthas</groupId>
        <artifactId>arthas-spring-boot-starter</artifactId>
        <version> 3.6.7</version>
    </dependency>
    ```
    ```
    implementation 'com.taobao.arthas:arthas-spring-boot-starter:3.6.7'
    ```
    ```
    
    arthas.agent-id=spring-boot-3-demo
    arthas.tunnel-server=ws://localhost:7777/ws
    arthas.telnet-port=${random.int(10000,15000)}
    arthas.http-port=${random.int(10000,15000)}
    server.port=8888
    ```

    ![image](https://user-images.githubusercontent.com/26846402/211269126-28b25fb7-af71-462a-89ae-6443b33fb7cf.png)

    ![image](https://user-images.githubusercontent.com/26846402/211205802-873eda38-6147-44ce-b8a5-95e0d424c918.png)


### idea plugin 
[idea-plugin](https://plugins.jetbrains.com/plugin/13581-arthas-idea)

## See Also
[^1]: https://commons.apache.org/proper/commons-ognl/
[^2]: https://arthas.aliyun.com
[^3]: https://arthas.aliyun.com/doc/commands.html
[^4]: https://arthas.aliyun.com/doc/tunnel.html


