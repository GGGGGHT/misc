# Arthas[^2]
Java 应用诊断利器

## 功能 

- Dashboard  实时查看系统的运行状况
- watch 查看函数调用的参数，返回值和异常
- retransform 在线热更新
- trace 方法内部调用路径，并输出方法路径上的每个节点上耗时
- WebConsole 在线诊断，点开网页诊断线上应用 需要额外配置

## command[^3]

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

## ognl[^1]


## profiler 


## misc
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

## See Also
[^1]: https://commons.apache.org/proper/commons-ognl/
[^2]: https://arthas.aliyun.com
[^3]: https://arthas.aliyun.com/doc/commands.html
