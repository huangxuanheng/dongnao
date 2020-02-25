# RPC

## RPC是什么

**remote procedure call** (**RPC**) ： **远程过程调用**

**何为过程？**

过程就是程序。

远程过程调用：在分布式计算中，一个程序运行中调用触发另一台机器上的程序执行来完成一个计算（或一个处理）。这个远程调用就像调用本地的方法一样，不需要编程者编码处理远程交互的细节。也就是说调用远程过程和调用本地方法的代码写法没区别，它具有远程透明性！

远程过程调用是一种Client-Server结构，通常通过request-response消息传输协议来实现。



**和 RMI 有什么区别？**

RMI(remote method invocation) 远程方法调用是oop领域中RPC的一种具体实现。



**我们熟悉的webservice、restfull接口调用是RPC吗？**

都是RPC，只是消息的组织方式、消息协议的不同罢了！



**远程过程调用较本地调用有何不同？**

- 慢几个数量级
- 也不那么可靠



RPC不是新事物，1960/70年代就有， 1981年Bruce Jay Nelson首次提出这个叫法。近些年因分布式系统、服务化、微服务的兴起而再次成为热点，为大众吹捧！



**RPC的过程**

RPC由Client发起，Client向已知的远程Server发送请求消息，以使用提供的参数执行指定的过程。远程Server向Client发送一个响应，应用程序继续它的进程。当Server处理调用时，Client会被阻塞(它会等到Server完成处理后才恢复执行)。Client也可向Server发送异步请求，比如XMLHttpRequest。

在各种RPC实现中有许多变化和微妙之处，导致各种不同的(不兼容的)RPC协议。

具体的程序过程

1. The client calls the client [stub](https://en.wikipedia.org/wiki/Stub_(distributed_computing)). The call is a local procedure call, with parameters pushed on to the stack in the normal way.
2. The [client stub](https://en.wikipedia.org/wiki/Class_stub) packs the parameters into a message and makes a system call to send the message. Packing the parameters is called [marshalling](https://en.wikipedia.org/wiki/Marshalling_(computer_science))（编组）.
3. The client's local [operating system](https://en.wikipedia.org/wiki/Operating_system) sends the message from the client machine to the server machine.
4. The local [operating system](https://en.wikipedia.org/wiki/Operating_system) on the server machine passes the incoming packets to the [server stub](https://en.wikipedia.org/wiki/Class_skeleton).
5. The server stub unpacks the parameters from the message. Unpacking the parameters is called [unmarshalling](https://en.wikipedia.org/wiki/Unmarshalling)（解组）.
6. Finally, the server stub calls the server procedure. The reply traces the same steps in the reverse direction.

stub（存根）：分布式计算中的存根是一段代码，它转换在远程过程调用(RPC)期间Client和server之间传递的参数。



**过程中会涉及哪些问题**

写一个RMI的程序作为示例，说明上面的过程。



## RPC协议是什么

RPC调用过程中需要将参数编组为消息进行发送，接收方需要解组消息为参数，如何来组织消息（消息采用什么格式）就是消息协议。**RPC调用过程中采用的消息协议称为RPC协议。**

RPC是要做的事，RPC协议规定请求、响应消息的格式。在TCP（网络传输控制协议）之上我们可以选用或自定义消息协议来完成我们的RPC消息交互。

我们可以选用通用的标准协议，如：http、https

也可根据自身的需要定义自己的消息协议！

在这里请一定要记住：远程过程调用完成的就是调用一个远程过程，消息传递是过程需要的参数或返回的结果。把它看成是本地方法调用，则可看出它的简单性。



- NFS (Network File System) is one of the most prominent users of RPC
- [Open Network Computing Remote Procedure Call](https://en.wikipedia.org/wiki/Open_Network_Computing_Remote_Procedure_Call), by [Sun Microsystems](https://en.wikipedia.org/wiki/Sun_Microsystems)
- [D-Bus](https://en.wikipedia.org/wiki/D-Bus) open source [IPC](https://en.wikipedia.org/wiki/Inter-process_communication) program provides similar function to [CORBA](https://en.wikipedia.org/wiki/CORBA).
- [SORCER](https://en.wikipedia.org/wiki/SORCER) provides the API and exertion-oriented language (EOL) for a federated method invocation
- [XML-RPC](https://en.wikipedia.org/wiki/XML-RPC) is an RPC protocol that uses [XML](https://en.wikipedia.org/wiki/XML) to encode its calls and [HTTP](https://en.wikipedia.org/wiki/HTTP) as a transport mechanism.
- [JSON-RPC](https://en.wikipedia.org/wiki/JSON-RPC) is an RPC protocol that uses [JSON](https://en.wikipedia.org/wiki/JSON)-encoded messages
- [JSON-WSP](https://en.wikipedia.org/wiki/JSON-WSP) is an RPC protocol that uses [JSON](https://en.wikipedia.org/wiki/JSON)-encoded messages
- [SOAP](https://en.wikipedia.org/wiki/SOAP) is a successor of XML-RPC and also uses XML to encode its HTTP-based calls.
- [ZeroC](https://en.wikipedia.org/wiki/ZeroC)'s [Internet Communications Engine](https://en.wikipedia.org/wiki/Internet_Communications_Engine) (Ice) distributed computing platform.
- [Etch](https://en.wikipedia.org/wiki/Etch_(protocol)) framework for building network services.
- [Apache Thrift](https://en.wikipedia.org/wiki/Apache_Thrift) protocol and framework.
- [CORBA](https://en.wikipedia.org/wiki/CORBA) provides remote procedure invocation through an intermediate layer called the *object request broker*.
- [Libevent](https://en.wikipedia.org/wiki/Libevent) provides a framework for creating RPC servers and clients.[[12\]](https://en.wikipedia.org/wiki/Remote_procedure_call#cite_note-12)
- [Windows Communication Foundation](https://en.wikipedia.org/wiki/Windows_Communication_Foundation) is an application programming interface in the .NET framework for building connected, service-oriented applications.
- [Microsoft .NET](https://en.wikipedia.org/wiki/.NET_Framework) [Remoting](https://en.wikipedia.org/wiki/.NET_Remoting) offers RPC facilities for distributed systems implemented on the Windows platform. It has been superseded by [WCF](https://en.wikipedia.org/wiki/Windows_Communication_Foundation).
- The Microsoft [DCOM](https://en.wikipedia.org/wiki/Distributed_Component_Object_Model) uses [MSRPC](https://en.wikipedia.org/wiki/Microsoft_RPC) which is based on [DCE/RPC](https://en.wikipedia.org/wiki/DCE/RPC)
- The Open Software Foundation [DCE/RPC](https://en.wikipedia.org/wiki/DCE/RPC) Distributed Computing Environment (also implemented by Microsoft).
- Google [Protocol Buffers](https://en.wikipedia.org/wiki/Protocol_Buffers) (protobufs) package includes an interface definition language used for its RPC protocols[[13\]](https://en.wikipedia.org/wiki/Remote_procedure_call#cite_note-13) open sourced in 2015 as [gRPC](https://en.wikipedia.org/wiki/GRPC).[[14\]](https://en.wikipedia.org/wiki/Remote_procedure_call#cite_note-14)
- [WAMP](https://en.wikipedia.org/wiki/Web_Application_Messaging_Protocol) combines RPC and [Publish-Subscribe](https://en.wikipedia.org/wiki/Publish%E2%80%93subscribe_pattern) into a single, transport-agnostic protocol.
- [Google Web Toolkit](https://en.wikipedia.org/wiki/Google_Web_Toolkit) uses an asynchronous RPC to communicate to the server service.[[15\]](https://en.wikipedia.org/wiki/Remote_procedure_call#cite_note-15)
- [Apache Avro](https://en.wikipedia.org/wiki/Apache_Avro) provides RPC where client and server exchange schemas in the connection handshake and code generation is not required.
- [Embedded RPC](https://github.com/EmbeddedRPC/erpc) is lightweight RPC implementation developed by NXP, targeting primary CortexM cores
- [KF Trusted Execution Environment](http://www.e-s-r.net/specifications/index.html) uses proxy and objects marshaling to communicate objects across sandboxes



为了让不同的客户机访问服务器，已经创建了许多标准化的RPC系统。其中大多数使用接口描述语言(IDL)来让各种平台调用RPC。然后可以使用IDL文件生成代码来在客户机和服务器之间进行接口。



## RPC框架是什么

封装好了参数编组、消息解组、底层网络通信的RPC程序开发框架，让我们可以直接在其基础上只需专注于我们的过程代码编写。

### 常用的RPC框架

java领域：

​	传统的webservice框架：Apache CXF、Apache Axis2、java 自带的 JAX-WS等等。webService框架大多基于标准的SOAP协议。

​	新兴的微服务框架：Dubbo、spring cloud、[Apache Thrift](https://en.wikipedia.org/wiki/Apache_Thrift) 等等

​	

## 为什么要用RPC

服务化

可重用

系统间交互调用





## RPC Specification 说明

- [RFC 1057](https://tools.ietf.org/html/rfc1057) - Specifies version 1 of ONC RPC
- [RFC 5531](https://tools.ietf.org/html/rfc5531) - Specifies version 2 of ONC RPC
- [Remote Procedure Calls (RPC)](http://www.cs.cf.ac.uk/Dave/C/node33.html) — A tutorial on ONC RPC by Dr Dave Marshall of Cardiff University
- [Introduction to RPC Programming](https://web.archive.org/web/20030404113118/http://techpubs.sgi.com/library/tpl/cgi-bin/getdoc.cgi?coll=0650&db=bks&srch=&fname=/SGI_Developer/IRIX_NetPG/sgi_html/ch04.html) — A developer's introduction to RPC and XDR, from SGI IRIX documentation.



### 术语

clients, calls, servers, replies, services,programs, procedures, and versions. Each remote procedure call has two sides: an active client side that makes the call to a server side, which sends back a reply. A network service is a collection of one or more remote programs. A remote program implements one or more
remote procedures; the procedures, their parameters, and results are documented in the specific program’s protocol specification. A server may support more than one version of a remote program in order to be compatible with changing protocols.



### RPC Model

​	One thread of control logically winds through two processes: the caller’s process and a server’s process. The caller first sends a call message to the server process and waits (blocks) for a reply message. The call message includes the procedure’s parameters, and the reply message includes the procedure’s results. Once the reply message is received, the results of the procedure are extracted, and the caller’s execution is resumed.
​	On the server side, a process is dormant awaiting the arrival of a call message. When one arrives, the server process extracts the procedure’s parameters, computes the results, sends a reply message, and then awaits the next call message.

There are a few important ways in which remote procedure calls differ from local procedure calls.

- Error handling: failures of the remote server or network must be handled when using remote procedure calls.
- Global variables and side effects: since the server does not have access to the client’s address space, hidden arguments cannot be passed as global variables or returned as side effects.（这点不需关注）
- Performance: remote procedures usually operate at one or more orders of magnitude slower than local procedure calls.
- Authentication: since remote procedure calls can be transported over unsecured networks, authentication may be necessary. Authentication prevents one entity from masquerading as some other entity.

The conclusion is that even though there are tools to automatically generate client and server libraries for a given service, protocols must still be designed carefully.





参考链接：

https://en.wikipedia.org/wiki/Remote_procedure_call



