
## RMI Lite

RMI Lite is a stupid simple mechanism for sending messages to objects in
another JVM without having to deal with `rmic` or catching
`java.rmi.RemoteException` all over your entire face.

First, create a server, objects registered with this server will be
able to receive messages from remote clients.

```java
import net.ladypleaser.rmilite.Server;
//...
server = new Server(3000); //server listening on port 3000
```

Then all you need is an interface:

```java
interface HelloSayer {
  public String sayHello();
}
```

and an object that implements it (formally or informally):

```java
public class Hello {
  public String sayHello() {
    return "Hello!"
  }
}
```

You can now register your object as the implementation of that interface:

```java
server.publish(HelloSayer.class, new Hello())
```

Now you can look up that object on remote VMs and invoke their methods

```java
client = new Client("server.host.com", 3000)
HelloSayer sayer = (HelloSayer)client.lookup(HelloSayer.class);
sayer.sayHello(); //Hello!
```

### Gotchas

All return values from remote methods must implement
`java.io.Serializeable`.

RMI Lite is actually capable of transparently passing back stubs to
objects that are not serializeable so that their method invocations
will come back across the wire, but 10 years of hind-sight. has made
me think that this isn't such a great idea. If you're still interested
in this capability. Ask me about it, and I'll document it.

### Developing

This uses ant for the build:

```
~$ ant compile                              
Buildfile: /Users/cowboyd/Projects/rmi-lite/build.xml

compile:
    [mkdir] Created dir: /Users/cowboyd/Projects/rmi-lite/build/classes
    [javac] Compiling 6 source files to /Users/cowboyd/Projects/rmi-lite/build/classes
     [rmic] RMI Compiling 1 class to /Users/cowboyd/Projects/rmi-lite/build/classes
    [mkdir] Created dir: /Users/cowboyd/Projects/rmi-lite/build/test-classes
    [javac] Compiling 7 source files to /Users/cowboyd/Projects/rmi-lite/build/test-classes

BUILD SUCCESSFUL
Total time: 1 second

```
Test:
```
~$ ant test
Buildfile: /Users/cowboyd/Projects/rmi-lite/build.xml

compile:
test:
    [mkdir] Created dir: /Users/cowboyd/Projects/rmi-lite/build/test-reports
    [junit] Running net.ladypleaser.rmilite.ClientServerTest
    [junit] Testsuite: net.ladypleaser.rmilite.ClientServerTest
    [junit] Tests run: 9, Failures: 0, Errors: 0, Time elapsed: 0.325 sec
    [junit] Tests run: 9, Failures: 0, Errors: 0, Time elapsed: 0.325 sec
    [junit]
    [junit] Testcase: testBasicMethodInvocation took 0.175 sec
    [junit] Testcase: testInvokesOverloadedMethods took 0.021 sec
    [junit] Testcase: testCallback took 0.009 sec
    [junit] Testcase: testCallbackOnReturnValue took 0.006 sec
    [junit] Testcase: testCallbackOnCallback took 0.01 sec
    [junit] Testcase: testCheckedExceptionThrownOnRemoteHost took 0.012 sec
    [junit] Testcase: testCheckedExceptionSubclassThrownOnRemoteHost took 0.015 sec
    [junit] Testcase: testRuntimeExceptionThrownOnRemoteHost took 0.01 sec
    [junit] Testcase: testErrorThrownOnRemoteHost took 0.009 sec

BUILD SUCCESSFUL
Total time: 2 seconds
```

### History

RMI Lite was originally written to control the test agent for the
Marathor test runner, but has found a home in several other
applications since.

My last official commit was in June of 2003:

//$Id: Server.java,v 1.1 2003/06/12 15:53:41 cowboyd Exp $


