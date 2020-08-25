

# ElasticSearch



## 简介

### 1、官网介绍

https://www.elastic.co/cn/what-is/elasticsearch

#### 什么是ElasticSearch？

Elasticsearch 是一个分布式的开源搜索和分析引擎，适用于所有类型的数据，包括文本、数字、地理空间、结构化和非结构化数据。Elasticsearch 在 Apache Lucene 的基础上开发而成，由 Elasticsearch N.V.（即现在的 Elastic）于 2010 年首次发布。Elasticsearch 以其简单的 REST 风格 API、分布式特性、速度和可扩展性而闻名，是 Elastic Stack 的核心组件；Elastic Stack 是适用于数据采集、充实、存储、分析和可视化的一组开源工具。人们通常将 Elastic Stack 称为 ELK Stack（代指 Elasticsearch、Logstash 和 Kibana），目前 Elastic Stack 包括一系列丰富的轻量型数据采集代理，这些代理统称为 Beats，可用来向 Elasticsearch 发送数据。

#### Elasticsearch 的用途是什么？

Elasticsearch 在速度和可扩展性方面都表现出色，而且还能够索引多种类型的内容，这意味着其可用于多种用例：

- 应用程序搜索
- 网站搜索
- 企业搜索
- 日志处理和分析
- 基础设施指标和容器监测
- 应用程序性能监测
- 地理空间数据分析和可视化
- 安全分析
- 业务分析

#### Elasticsearch 的工作原理是什么？

原始数据会从多个来源（包括日志、系统指标和网络应用程序）输入到 Elasticsearch 中。*数据采集*指在 Elasticsearch 中进行*索引*之前解析、标准化并充实这些原始数据的过程。这些数据在 Elasticsearch 中索引完成之后，用户便可针对他们的数据运行复杂的查询，并使用聚合来检索自身数据的复杂汇总。在 Kibana 中，用户可以基于自己的数据创建强大的可视化，分享仪表板，并对 Elastic Stack 进行管理。



---

### 2、基本概念

- Index（索引）

  动词，相当于 MySQL 的中的 insert

  名词，相当于 MySQL 中的 database

- Type（类型）

  在 index 中，可以定义一个或多个类型。类似于 MySQL 中的 table，每一种类型的数据放在一起。

- Document（文档）

  保存在某个 index 下，某种 type 的一个数据（Document），文档是 JSON 格式的，Document 就像是 MySQL 中的某个 Table 里面的内容。

- 倒排索引

![](https://gitee.com/itzhouq/images/raw/master/notes/20200822114218.png)

### 3、倒排索引

[ElasticSearch权威指南介绍](https://www.elastic.co/guide/cn/elasticsearch/guide/current/inverted-index.html)

[什么是倒排索引](https://www.cnblogs.com/zlslch/p/6440114.html)

### 4、参考文档

[官方文档](https://www.elastic.co/guide/index.html)

[Elasticsearch: 权威指南](https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html) 【基于 Elasticsearch 2.x 版本，有些内容可能已经过时】

[阮一峰：全文搜索引擎 Elasticsearch 入门教程](https://www.ruanyifeng.com/blog/2017/08/elasticsearch.html)

---

## Docker 安装 ES

- 拉取镜像

```shell
docker pull elasticsearch:7.4.2  # 存储和检索数据
docker pull kibana:7.4.2  # 可视化检索数据
```

- 创建挂载目录

```shell
[root@itzhouc mydata]# mkdir -p /mydata/elasticsearch/config  # 配置文件目录
[root@itzhouc mydata]# mkdir -p /mydata/elasticsearch/data    # 存放数据目录
[root@itzhouc mydata]# echo "http.host: 0.0.0.0" >/mydata/elasticsearch/config/elasticsearch.yml  # 设置任何IP的主机都能访问
```

- 开启容器

```shell
[root@itzhouc ~]# docker run --name elasticsearch -p 9200:9200 -p 9300:9300 \
> -e  "discovery.type=single-node" \
> -e ES_JAVA_OPTS="-Xms64m -Xmx128m" \
> -v /mydata/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
> -v /mydata/elasticsearch/data:/usr/share/elasticsearch/data \
> -v  /mydata/elasticsearch/plugins:/usr/share/elasticsearch/plugins \
> -d elasticsearch:7.4.2
864706d66fda64c4f661cd9f56bf76c722df6352e6ff531b4dc1d782e8384d7b
[root@itzhouc ~]# docker ps
CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS              PORTS                                            NAMES
864706d66fda        elasticsearch:7.4.2   "/usr/local/bin/dock…"   4 seconds ago       Up 3 seconds        0.0.0.0:9200->9200/tcp, 0.0.0.0:9300->9300/tcp   elasticsearch
f7e617b06a1e        redis                 "docker-entrypoint.s…"   4 weeks ago         Up 4 weeks          0.0.0.0:6379->6379/tcp                           redis
d80726b62b9b        mysql:5.7             "docker-entrypoint.s…"   4 weeks ago         Up 4 weeks          0.0.0.0:3306->3306/tcp, 33060/tcp                mysql
[root@itzhouc ~]# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                               NAMES
f7e617b06a1e        redis               "docker-entrypoint.s…"   4 weeks ago         Up 4 weeks          0.0.0.0:6379->6379/tcp              redis
d80726b62b9b        mysql:5.7           "docker-entrypoint.s…"   4 weeks ago         Up 4 weeks          0.0.0.0:3306->3306/tcp, 33060/tcp   mysql
[root@itzhouc ~]#
```

1. --name elascticsearch：容器名
2. 9200端口：外界访问 ElasticSearch 的端口
3. 9300端口：ES 节点之间通信的端口
4. -e：指定参数
5. discovery.type=single.node：以单节点模式运行
6. ES_JAVA_OPTS="-Xms64m -Xms128m"：配置 ES 的初始内存和最大内存
7. -v：挂载目录
8. -d：以后台启动方式运行

容器启动后有挂掉了。

- **查看错误日志**

```shell
[root@itzhouc ~]# docker logs elasticsearch
OpenJDK 64-Bit Server VM warning: Option UseConcMarkSweepGC was deprecated in version 9.0 and will likely be removed in a future release.
{"type": "server", "timestamp": "2020-08-22T08:07:32,775Z", "level": "WARN", "component": "o.e.b.ElasticsearchUncaughtExceptionHandler", "cluster.name": "elasticsearch", "node.name": "a6507718ac70", "message": "uncaught exception in thread [main]",
"stacktrace": ["org.elasticsearch.bootstrap.StartupException: ElasticsearchException[failed to bind service]; nested: AccessDeniedException[/usr/share/elasticsearch/data/nodes];",
"at org.elasticsearch.bootstrap.Elasticsearch.init(Elasticsearch.java:163) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.bootstrap.Elasticsearch.execute(Elasticsearch.java:150) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.cli.EnvironmentAwareCommand.execute(EnvironmentAwareCommand.java:86) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.cli.Command.mainWithoutErrorHandling(Command.java:125) ~[elasticsearch-cli-7.4.2.jar:7.4.2]",
"at org.elasticsearch.cli.Command.main(Command.java:90) ~[elasticsearch-cli-7.4.2.jar:7.4.2]",
"at org.elasticsearch.bootstrap.Elasticsearch.main(Elasticsearch.java:115) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.bootstrap.Elasticsearch.main(Elasticsearch.java:92) ~[elasticsearch-7.4.2.jar:7.4.2]",
"Caused by: org.elasticsearch.ElasticsearchException: failed to bind service",
"at org.elasticsearch.node.Node.<init>(Node.java:614) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.node.Node.<init>(Node.java:255) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.bootstrap.Bootstrap$5.<init>(Bootstrap.java:221) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.bootstrap.Bootstrap.setup(Bootstrap.java:221) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.bootstrap.Bootstrap.init(Bootstrap.java:349) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.bootstrap.Elasticsearch.init(Elasticsearch.java:159) ~[elasticsearch-7.4.2.jar:7.4.2]",
"... 6 more",
"Caused by: java.nio.file.AccessDeniedException: /usr/share/elasticsearch/data/nodes",
"at sun.nio.fs.UnixException.translateToIOException(UnixException.java:90) ~[?:?]",
"at sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:111) ~[?:?]",
"at sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:116) ~[?:?]",
"at sun.nio.fs.UnixFileSystemProvider.createDirectory(UnixFileSystemProvider.java:389) ~[?:?]",
"at java.nio.file.Files.createDirectory(Files.java:693) ~[?:?]",
"at java.nio.file.Files.createAndCheckIsDirectory(Files.java:800) ~[?:?]",
"at java.nio.file.Files.createDirectories(Files.java:786) ~[?:?]",
"at org.elasticsearch.env.NodeEnvironment.lambda$new$0(NodeEnvironment.java:272) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.env.NodeEnvironment$NodeLock.<init>(NodeEnvironment.java:209) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.env.NodeEnvironment.<init>(NodeEnvironment.java:269) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.node.Node.<init>(Node.java:275) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.node.Node.<init>(Node.java:255) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.bootstrap.Bootstrap$5.<init>(Bootstrap.java:221) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.bootstrap.Bootstrap.setup(Bootstrap.java:221) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.bootstrap.Bootstrap.init(Bootstrap.java:349) ~[elasticsearch-7.4.2.jar:7.4.2]",
"at org.elasticsearch.bootstrap.Elasticsearch.init(Elasticsearch.java:159) ~[elasticsearch-7.4.2.jar:7.4.2]",
"... 6 more"] }
```

AccessDeniedException 说明文件没有权限。

- 添加权限

```shell
[root@itzhouc elasticsearch]# chmod -R 777 /mydata/elasticsearch/ # -R 递归
[root@itzhouc elasticsearch]# ll
总用量 12
drwxrwxrwx 2 root root 4096 8月  22 15:39 config
drwxrwxrwx 2 root root 4096 8月  22 15:39 data
```

- 重启

```shell
[root@itzhouc elasticsearch]# docker restart elasticsearch
```

- 访问：http://47.96.30.109:9200/

 不能访问。因为用的阿里云 ECS 服务器，需要额外在阿里云安全组策略中开启 9200 端口。开启后才可以访问。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200822234533.png)

- 设置开机自启动 elasticsearch

```shell
docker update elasticsearch --restart=always
```

---



## Docker 安装 Kibana

### 1、使用Postman 向 ElasticSearch 发送请求

安装 ElasticSearch 之后可以不用安装 Kibana ，使用 postman 就能发送请求得到数据。比如得到 ES 的节点信息。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200822235730.png)



### 2、Kibana 安装

Kibana 的优势在于可视化。

#### 拉取镜像

```shell
docker pull kibana:7.4.2  
```

#### 启动容器

```shell
docker run --name kibana -e ELASTICSEARCH_HOSTS=http://47.96.30.109:9200/ -p 5601:5601 -d kibana:7.4.2
```

安装 Docker 容器都可以参考 Docker hub 中对应容器文档。

- Kibana：https://hub.docker.com/_/kibana
- Install Kibana with Docker：https://www.elastic.co/guide/en/kibana/current/docker.html#docker

#### 设置开机启动 Kibana

```shell
docker update kibana  --restart=always
```

#### 访问 Kibana:http://47.96.30.109:5601/

- 还是需要在阿里云安全组策略中开启 5601 端口。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823000332.png)

---



## 初步检索

### 1、_CAT

- GET/*cat/nodes：查看所有节点*

http://47.96.30.109:9200/_cat/nodes

```shell
127.0.0.1 72 95 3 0.82 0.87 0.57 dilm * a6507718ac70
```

> *表示集群中的主节点 

- GET/_cat/health：查看es健康状况

http://47.96.30.109:9200/_cat/health

```shell
1598146362 01:32:42 elasticsearch green 1 1 3 3 0 0 0 0 - 100.0%
```

> green表示健康值正常

- GET/_cat/master：查看主节点

http://47.96.30.109:9200/_cat/master

```shell
3aqr23nORWOfc5ttsbHbGg 127.0.0.1 127.0.0.1 a6507718ac70
```

> a6507718ac70 是主节点的名字，3aqr23nORWOfc5ttsbHbGg 是唯一标识

- GET/_cat/indicies：查看所有索引 ，等价于mysql数据库的show databases

http://47.96.30.109:9200/_cat/indices

```shell
green open .kibana_task_manager_1   8b_JqijJS8-mnpJNx_kAzg 1 0 2 0 30.5kb 30.5kb
green open .apm-agent-configuration EUqx3wAcT0-aoSNFdl6JCQ 1 0 0 0   283b   283b
green open .kibana_1                AyRIt6NsTIqA8IOLDMPiYw 1 0 8 0 25.3kb 25.3kb
```



### 2、索引一个文档(保存)

保存一个数据，保存在哪个索引的哪个类型下，指定用那个唯一标识 PUT customer/external/1;在customer索引下的external类型下保存1号数据为：

```shell
http://47.96.30.109:9200/customer/external/1
```

```shell
{
 "name":"John Doe"
}
```

PUT和POST都可以 POST新增。如果不指定id，会自动生成id。指定id就会修改这个数据，并新增版本号； PUT可以新增也可以修改。PUT必须指定id；由于PUT需要指定id，我们一般用来做修改操作，不指定id会报错。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823094907.png)

创建数据成功后，显示201 created表示插入记录成功。

```json
{
    "_index": "customer",
    "_type": "external",
    "_id": "1",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 0,
    "_primary_term": 1
}
```

这些返回的JSON串的含义；这些带有下划线开头的，称为元数据，反映了当前的基本信息。

"_index": "customer" 表明该数据在哪个数据库下；

"_type": "external" 表明该数据在哪个类型下；

"_id": "1" 表明被保存数据的id；

"_version": 1, 被保存数据的版本

"result": "created" 这里是创建了一条数据，如果重新put一条数据，则该状态会变为updated，并且版本号也会发生变化。



如果使用 POST 方式保存：

1. 添加数据的时候，指定ID，会使用该id，并且类型是新增：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823095427.png)

2. 再次使用POST插入数据，类型为updated

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823095505.png)

3. 添加数据的时候，不指定ID，会自动的生成id，并且类型是新增：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823095549.png)

4. 再次使用POST插入数据，仍然是新增的，ID不同

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823095633.png)

---

### 3、查看文档&&乐观锁

GET /customer/external/1

http://47.96.30.109:9200/customer/external

```json
{
    "_index": "customer", // 在哪个索引
    "_type": "external",  // 在哪个类型
    "_id": "1",  // 记录ID
    "_version": 2,  // 版本号
    "_seq_no": 1,  // 并发控制字段，每次更新都会+1，用来做乐观锁
    "_primary_term": 1,  // 并发控制字段，主分片重新分配，如果重启就会变化
    "found": true,
    "_source": {
        "name": "John Doe"
    }
}
```

- 乐观锁演示：

**通过“if_seq_no=1&if_primary_term=1 ”，当序列号匹配的时候，才进行修改，否则不修改。**

实例：将id=1的数据更新为name=“Jack”，然后再次更新为name=“Jack2”，起始_seq_no=6，_primary_term=1

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823101712.png)

将name更新为"Jack"，更新过程中使用seq_no=6

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823102008.png)

出现更新错误。

如果要更新成功，需要先查询得到seq_no再更新。

```shell
GET http://47.96.30.109:9200/customer/external/1
```

```json
{
    "_index": "customer",
    "_type": "external",
    "_id": "1",
    "_version": 4,
    "_seq_no": 7,
    "_primary_term": 1,
    "found": true,
    "_source": {
        "name": "Jack"
    }
}
```

这时候 seq_no=7

再次尝试更新：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823102226.png)

---

### 4、更新文档

语法：

```json
POST customer/external/1/_update
{
  "doc":{
    "name":"Join Doew"
  }
}
```

或者

```json
POST customer/external/1
{
  "name":"Join Doew"
}
```

或者：

```joon
PUT customer/external/1
{
  "name":"Join Doew"
}
```

- POST 操作会对比源文档数据，如果相同不会有什么操作，文档 version 不增加。PUT 操作总会将数据重新保存并增加 version 版本。
- 带_update 对比元数据如果一样就不进行任何操作。
- 看场景
  - 对于大并发更新，不带 update
  - 对于大并发查询偶尔更新，带 update；对比更新，重新计算分配规则。

- 更新同时增加属性



- POST更新文档，带有_update

http://47.96.30.109:9200/customer/external/1/_update

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823104126.png)

如果再次执行更新，则不执行任何操作，序列号也不发生变化

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823104210.png)

POST更新方式，会对比原来的数据，和原来的相同，则不执行任何操作（version和_seq_no）都不变。

- POST或者PUT更新文档，不带_update

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823104410.png)

在更新过程中，重复执行更新操作，数据也能够更新成功，不会和原来的数据进行对比。

---



### 5、删除文档或索引

```shell
DELETE customer/external/1
DELETE customer
```

> 注：elasticsearch并没有提供删除类型的操作，只提供了删除索引和文档的操作。

- 删除id=1的数据，删除后继续查询

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823105503.png)

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823105524.png)

---

- 删除整个costomer索引数据

删除的索引:

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823105631.png)

删除“ customer ”索引

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823105717.png)

删除“customer”索引之后的索引：

```shell
green open .kibana_task_manager_1   8b_JqijJS8-mnpJNx_kAzg 1 0 2 0 30.5kb 30.5kb
green open .apm-agent-configuration EUqx3wAcT0-aoSNFdl6JCQ 1 0 0 0   283b   283b
green open .kibana_1                AyRIt6NsTIqA8IOLDMPiYw 1 0 8 0 25.3kb 25.3kb
```

---



### 6、批量操作--bulk

语法格式：

```json
{action:{metadata}}\n
{request body  }\n

{action:{metadata}}\n
{request body  }\n
```

这里的批量操作，当发生某一条执行发生失败时，其他的数据仍然能够接着执行，也就是说彼此之间是独立的。

bulk api 以此按顺序执行所有的action（动作）。如果一个单个的动作因任何原因失败，它将继续处理它后面剩余的动作。当bulk api返回时，它将提供每个动作的状态（与发送的顺序相同），所以您可以检查是否一个指定的动作是否失败了。

- 执行多条数据

```shell
POST customer/external/_bulk
{"index":{"_id":"1"}}
{"name":"John Doe"}
{"index":{"_id":"2"}}
{"name":"John Doe"}
```



在 Kibana 的 Dev Tools 中测试。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823111317.png)



- 对于整个索引执行批量操作

```shell
POST /_bulk
{"delete":{"_index":"website","_type":"blog","_id":"123"}}
{"create":{"_index":"website","_type":"blog","_id":"123"}}
{"title":"my first blog post"}
{"index":{"_index":"website","_type":"blog"}}
{"title":"my second blog post"}
{"update":{"_index":"website","_type":"blog","_id":"123"}}
{"doc":{"title":"my updated blog post"}}
```

结果：took 是花费的时间

```json
#! Deprecation: [types removal] Specifying types in bulk requests is deprecated.
{
  "took" : 280,
  "errors" : false,
  "items" : [
    {
      "delete" : {
        "_index" : "website",
        "_type" : "blog",
        "_id" : "123",
        "_version" : 1,
        "result" : "not_found",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 0,
        "_primary_term" : 1,
        "status" : 404
      }
    },
    {
      "create" : {
        "_index" : "website",
        "_type" : "blog",
        "_id" : "123",
        "_version" : 2,
        "result" : "created",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 1,
        "_primary_term" : 1,
        "status" : 201
      }
    },
    {
      "index" : {
        "_index" : "website",
        "_type" : "blog",
        "_id" : "lqRPGXQBE-bXaLhHITps",
        "_version" : 1,
        "result" : "created",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 2,
        "_primary_term" : 1,
        "status" : 201
      }
    },
    {
      "update" : {
        "_index" : "website",
        "_type" : "blog",
        "_id" : "123",
        "_version" : 3,
        "result" : "updated",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 3,
        "_primary_term" : 1,
        "status" : 200
      }
    }
  ]
}
```

---



### 7、测试数据

准备了一份顾客银行账户信息的虚构的 JSON 文档样本。每个文档都有下列的schema（模式）。

```json
{
	"account_number": 1,
	"balance": 39225,
	"firstname": "Amber",
	"lastname": "Duke",
	"age": 32,
	"gender": "M",
	"address": "880 Holmes Lane",
	"employer": "Pyrami",
	"email": "amberduke@pyrami.com",
	"city": "Brogan",
	"state": "IL"
}
```

https://raw.githubusercontent.com/elastic/elasticsearch/master/docs/src/test/resources/accounts.json 导入测试数据。

```json
POST bank/account/_bulk
```

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823111820.png)



- 查看索引

```json
yellow open bank                     uvHeH4FwQ22Fp3zAxmddjQ 1 1 1000 0 426.9kb 426.9kb
yellow open website                  qt3FK24STyaVDH9fi6Ewuw 1 1    2 2   8.4kb   8.4kb
green  open .kibana_task_manager_1   8b_JqijJS8-mnpJNx_kAzg 1 0    2 0  30.5kb  30.5kb
green  open .apm-agent-configuration EUqx3wAcT0-aoSNFdl6JCQ 1 0    0 0    283b    283b
green  open .kibana_1                AyRIt6NsTIqA8IOLDMPiYw 1 0    8 0  25.3kb  25.3kb
yellow open customer                 BmLPdrUyS42Wb_Szwp6N2A 1 1    2 0   3.5kb   3.5kb
```



---



## 检索进阶

### 1、SearchAPI

**ES 所有查询语法都可以在官方文档中看到**：https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started-search.html



ES 支持两种基本方式检索：

- 一个是通过使用 REST request URI 发送所有参数（uri + 检索参数）
- 另一个是通过 REST request body 来发哦是哪个他们（uri + 请求体）

实例：

#### 使用 REST request URI 方式

```shell
GET bank/_search?q=*&sort=account_number:asc
```

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823140738.png)

ES 默认会分页查询 10 条数据。

结果中的字段含义可以看文档。

The response also provides the following information about the search request:

- `took` – how long it took Elasticsearch to run the query, in milliseconds
- `timed_out` – whether or not the search request timed out
- `_shards` – how many shards were searched and a breakdown of how many shards succeeded, failed, or were skipped.
- `max_score` – the score of the most relevant document found
- `hits.total.value` - how many matching documents were found
- `hits.sort` - the document’s sort position (when not sorting by relevance score)
- `hits._score` - the document’s relevance score (not applicable when using `match_all`)

#### Query DSL基本语法

Elasticsearch提供了一个可以执行查询的Json风格的DSL。这个被称为Query DSL，该查询语言非常全面。

```shell
GET /bank/_search
{
  "query": { "match_all": {} },
  "sort": [
    { "account_number": "asc" }
  ]
}
```

- 基本语法

```shell
QUERY_NAME:{
   ARGUMENT:VALUE,
   ARGUMENT:VALUE,...
}
```

- 如果针对于某个字段，那么它的结构如下：

```json
{
  QUERY_NAME:{
     FIELD_NAME:{
       ARGUMENT:VALUE,
       ARGUMENT:VALUE,...
      }   
   }
}
```

```json
GET bank/_search
{
  "query": {
    "match_all": {}
  },
  "from": 0,
  "size": 5,
  "sort": [
    {
      "account_number": {
        "order": "desc"
      }
    }
  ]
}
```

- query定义如何查询；
  - match_all查询类型【代表查询所有的所有】，es中可以在query中组合非常多的查询类型完成复杂查询；
  - 除了query参数之外，我们可也传递其他的参数以改变查询结果，如sort，size；
  - from+size限定，完成分页功能；
  - sort排序，多字段排序，会在前序字段相等时后续字段内部排序，否则以前序为准；

- 返回部分字段

```json
GET bank/_search
{
  "query": {
    "match_all": {}
  },
  "from": 0,
  "size": 5,
  "sort": [
    {
      "account_number": {
        "order": "desc"
      }
    }
  ],
  "_source": ["balance","firstname"]
  
}
```

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823142310.png)

---



### 2、Query DSL

#### match匹配查询

- 基本类型（非字符串），精确控制

```shell
GET /bank/_search
{
  "query": {
    "match": {
      "account_number": 20
    }
  }
}
```

match返回account_number=20的数据。结果：

```json
{
  "took" : 6,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "bank",
        "_type" : "account",
        "_id" : "20",
        "_score" : 1.0,
        "_source" : {
          "account_number" : 20,
          "balance" : 16418,
          "firstname" : "Elinor",
          "lastname" : "Ratliff",
          "age" : 36,
          "gender" : "M",
          "address" : "282 Kings Place",
          "employer" : "Scentric",
          "email" : "elinorratliff@scentric.com",
          "city" : "Ribera",
          "state" : "WA"
        }
      }
    ]
  }
}
```



- 字符串，全文检索

```shell
GET /bank/_search
{
  "query": {
    "match": {
      "address": "Kings Street"
    }
  }
}
```

全文检索，最终会按照评分进行排序，会对检索条件进行分词匹配。结果：

```json
{
  "took" : 17,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 387,
      "relation" : "eq"
    },
    "max_score" : 5.9908285,
    "hits" : [
      {
        "_index" : "bank",
        "_type" : "account",
        "_id" : "20",
        "_score" : 5.9908285,
        "_source" : {
          "account_number" : 20,
          "balance" : 16418,
          "firstname" : "Elinor",
          "lastname" : "Ratliff",
          "age" : 36,
          "gender" : "M",
          "address" : "282 Kings Place",
          "employer" : "Scentric",
          "email" : "elinorratliff@scentric.com",
          "city" : "Ribera",
          "state" : "WA"
        }
      },
      {
        "_index" : "bank",
        "_type" : "account",
        "_id" : "107",
        "_score" : 0.95395315,
        "_source" : {
          "account_number" : 107,
          "balance" : 48844,
          "firstname" : "Randi",
          "lastname" : "Rich",
          "age" : 28,
          "gender" : "M",
          "address" : "694 Jefferson Street",
          "employer" : "Netplax",
          "email" : "randirich@netplax.com",
          "city" : "Bellfountain",
          "state" : "SC"
        }
      }, {...}
    ]
  }
}
```



---

#### match_phrase 短句匹配

将需要匹配的值当成一整个单词（不分词）进行检索

```shell
GET bank/_search
{
  "query": {
    "match_phrase": {
      "address": "mill road"
    }
  }
}
```

查处address中包含mill_road的所有记录，并给出相关性得分。结果：

```json
{
  "took" : 21,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 8.926605,
    "hits" : [
      {
        "_index" : "bank",
        "_type" : "account",
        "_id" : "970",
        "_score" : 8.926605,
        "_source" : {
          "account_number" : 970,
          "balance" : 19648,
          "firstname" : "Forbes",
          "lastname" : "Wallace",
          "age" : 28,
          "gender" : "M",
          "address" : "990 Mill Road",
          "employer" : "Pheast",
          "email" : "forbeswallace@pheast.com",
          "city" : "Lopezo",
          "state" : "AK"
        }
      }
    ]
  }
}
```

文本字段的匹配，使用也可以使用 match.keyword，匹配的条件就是要显示字段的全部值，要进行精确匹配的。

match_phrase是做短语匹配，只要文本中包含匹配条件，就能匹配到。

```shell
GET bank/_search
{
  "query": {
    "match": {
      "address.keyword": "mill road"
    }
  }
}
```

address 如果没有精准匹配【整个字段的全部值】就查询不到。

```json
{
  "took" : 2,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 0,
      "relation" : "eq"
    },
    "max_score" : null,
    "hits" : [ ]
  }
}
```



#### multi_math多字段匹配

```shell
GET bank/_search
{
  "query": {
    "multi_match": {
      "query": "mill movico",
      "fields": ["address", "city"]
    }
  }
}
```

"address"或 "city" 属性包含"mill" 或 "movico" 都可以被查询到。查询过程中，会对于查询条件进行分词。结果：

```json
{
  "took" : 4,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 4,
      "relation" : "eq"
    },
    "max_score" : 6.505949,
    "hits" : [
      {
        "_index" : "bank",
        "_type" : "account",
        "_id" : "472",
        "_score" : 6.505949,
        "_source" : {
          "account_number" : 472,
          "balance" : 25571,
          "firstname" : "Lee",
          "lastname" : "Long",
          "age" : 32,
          "gender" : "F",
          "address" : "288 Mill Street",
          "employer" : "Comverges",
          "email" : "leelong@comverges.com",
          "city" : "Movico",
          "state" : "MT"
        }
      },
      {
        "_index" : "bank",
        "_type" : "account",
        "_id" : "970",
        "_score" : 5.4032025,
        "_source" : {
          "account_number" : 970,
          "balance" : 19648,
          "firstname" : "Forbes",
          "lastname" : "Wallace",
          "age" : 28,
          "gender" : "M",
          "address" : "990 Mill Road",
          "employer" : "Pheast",
          "email" : "forbeswallace@pheast.com",
          "city" : "Lopezo",
          "state" : "AK"
        }
      },
      {
        "_index" : "bank",
        "_type" : "account",
        "_id" : "136",
        "_score" : 5.4032025,
        "_source" : {
          "account_number" : 136,
          "balance" : 45801,
          "firstname" : "Winnie",
          "lastname" : "Holland",
          "age" : 38,
          "gender" : "M",
          "address" : "198 Mill Lane",
          "employer" : "Neteria",
          "email" : "winnieholland@neteria.com",
          "city" : "Urie",
          "state" : "IL"
        }
      },
      {
        "_index" : "bank",
        "_type" : "account",
        "_id" : "345",
        "_score" : 5.4032025,
        "_source" : {
          "account_number" : 345,
          "balance" : 9812,
          "firstname" : "Parker",
          "lastname" : "Hines",
          "age" : 38,
          "gender" : "M",
          "address" : "715 Mill Avenue",
          "employer" : "Baluba",
          "email" : "parkerhines@baluba.com",
          "city" : "Blackgum",
          "state" : "KY"
        }
      }
    ]
  }
}
```



---



#### bool 复合查询

复合语句可以合并，任何其他查询语句，包括符合语句。这也就意味着，复合语句之间 可以互相嵌套，可以表达非常复杂的逻辑。

must：必须达到must所列举的所有条件。

```shell
GET bank/_search
{
   "query":{
        "bool":{
             "must":[
              {"match":{"address":"mill"}},
              {"match":{"gender":"M"}}
             ]
         }
    }
}
```

must_not，必须不匹配must_not所列举的所有条件。

should，应该满足should所列举的条件。如果没有满足也能查询出来，只不过得分会降低。

- 匹配 gender 必须是M， address 必须包含 "mill"，age必须不是 18的，lastname 最好是 "Wallace" 的文档。

```shell
GET bank/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "gender": "M"
        }},
        {"match": {
          "address": "mill"
        }}
      ],
      "must_not": [
        {"match": {
          "age": 18
        }}
      ],
      "should": [
        {"match": {
          "lastname": "Wallace"
        }}
      ]
    }
  }
}
```

结果：

```json
{
  "took" : 19,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 3,
      "relation" : "eq"
    },
    "max_score" : 12.585751,
    "hits" : [
      {
        "_index" : "bank",
        "_type" : "account",
        "_id" : "970",
        "_score" : 12.585751,
        "_source" : {
          "account_number" : 970,
          "balance" : 19648,
          "firstname" : "Forbes",
          "lastname" : "Wallace",
          "age" : 28,
          "gender" : "M",
          "address" : "990 Mill Road",
          "employer" : "Pheast",
          "email" : "forbeswallace@pheast.com",
          "city" : "Lopezo",
          "state" : "AK"
        }
      },
      {
        "_index" : "bank",
        "_type" : "account",
        "_id" : "136",
        "_score" : 6.0824604,
        "_source" : {
          "account_number" : 136,
          "balance" : 45801,
          "firstname" : "Winnie",
          "lastname" : "Holland",
          "age" : 38,
          "gender" : "M",
          "address" : "198 Mill Lane",
          "employer" : "Neteria",
          "email" : "winnieholland@neteria.com",
          "city" : "Urie",
          "state" : "IL"
        }
      },
      {
        "_index" : "bank",
        "_type" : "account",
        "_id" : "345",
        "_score" : 6.0824604,
        "_source" : {
          "account_number" : 345,
          "balance" : 9812,
          "firstname" : "Parker",
          "lastname" : "Hines",
          "age" : 38,
          "gender" : "M",
          "address" : "715 Mill Avenue",
          "employer" : "Baluba",
          "email" : "parkerhines@baluba.com",
          "city" : "Blackgum",
          "state" : "KY"
        }
      }
    ]
  }
}
```

可以看到在其他条件相同的情况下匹配到should的文档得分较高。

---



#### Filter 结果过滤

并不是所有的查询都需要产生分数，特别是哪些仅用于filtering过滤的文档。为了不计算分数，elasticsearch会自动检查场景并且优化查询的执行。

filter 的用法和 must_not 相同，但是 filter 不会贡献得分。

- 查询年龄在 18 到 30 之间的文档

```shell
GET bank/_search
{
  "query": {
    "bool": {
      "filter": {
        "range": {
          "age": {
            "gte": 18,
            "lte": 30
          }
        }
      }
    }
  }
}
```

结果：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823152957.png)

可以看到查到的分数都为0。



filter 也可以在匹配之后再次过滤。比如在上面的查询结果最后再加上过滤条件。

```shell
GET bank/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "gender": "M"
        }},
        {"match": {
          "address": "mill"
        }}
      ],
      "must_not": [
        {"match": {
          "age": 18
        }}
      ],
      "should": [
        {"match": {
          "lastname": "Wallace"
        }}
      ],
      "filter": {
        "range": {
          "age": {
            "gte": 10,
            "lte": 30
          }
        }
      }
    }
  }
}
```

结果：

```json
{
  "took" : 4,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 12.585751,
    "hits" : [
      {
        "_index" : "bank",
        "_type" : "account",
        "_id" : "970",
        "_score" : 12.585751,
        "_source" : {
          "account_number" : 970,
          "balance" : 19648,
          "firstname" : "Forbes",
          "lastname" : "Wallace",
          "age" : 28,
          "gender" : "M",
          "address" : "990 Mill Road",
          "employer" : "Pheast",
          "email" : "forbeswallace@pheast.com",
          "city" : "Lopezo",
          "state" : "AK"
        }
      }
    ]
  }
}
```

将结果从 3 条过滤剩下一条，但是对得分没有影响。



---

#### Term 查询

和match一样。匹配某个属性的值。全文检索字段用match，其他非text字段匹配用term。

> Avoid using the `term` query for [`text`](https://www.elastic.co/guide/en/elasticsearch/reference/7.6/text.html) fields.
>
> 避免对文本字段使用“term”查询
>
> By default, Elasticsearch changes the values of `text` fields as part of [analysis](https://gitee.com/cosmoswong/markdownblog/blob/master/谷粒商城/谷粒商城—分布式高级.md). This can make finding exact matches for `text` field values difficult.
>
> 默认情况下，Elasticsearch作为[analysis](https://gitee.com/cosmoswong/markdownblog/blob/master/谷粒商城/谷粒商城—分布式高级.md)的一部分更改' text '字段的值。这使得为“text”字段值寻找精确匹配变得困难。
>
> To search `text` field values, use the match.
>
> 要搜索“text”字段值，请使用匹配。
>
> https://www.elastic.co/guide/en/elasticsearch/reference/7.6/query-dsl-term-query.html

- 使用 term 匹配查询

```shell
GET bank/_search
{
  "query": {
    "term": {
      "address": "mill Road"
    }
  }
}
```

结果：

```json
{
  "took" : 0,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 0,
      "relation" : "eq"
    },
    "max_score" : null,
    "hits" : [ ]
  }
}
```

一条文档也没有。

- 使用 match 匹配

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823154300.png)

能查到 32 条记录。

**全文检索字段用match，其他非text字段匹配用term**。



---



##  Aggregation 执行聚合

聚合提供了从数据中分组和提取数据的能力。最简单的聚合方法大致等于SQL Group by和SQL聚合函数。在elasticsearch中，执行搜索返回this（命中结果），并且同时返回聚合结果，把以响应中的所有hits（命中结果）分隔开的能力。这是非常强大且有效的，你可以执行查询和多个聚合，并且在一次使用中得到各自的（任何一个的）返回结果，使用一次简洁和简化的API啦避免网络往返。

"size":0

size:0不显示搜索数据 aggs：执行聚合。聚合语法如下：

```shell
"aggs":{
    "aggs_name这次聚合的名字，方便展示在结果集中":{
        "AGG_TYPE聚合的类型(avg,term,terms)":{}
     }
}
```

参考文档：https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-avg-aggregation.html

### 搜索address中包含mill的所有人的年龄分布以及平均年龄，但不显示这些人的详情

```shell
GET bank/_search
{
  "query": {
    "match": {
      "address": "Mill"
    }
  },
  "aggs": {
    "ageAgg": {
      "terms": {
        "field": "age",
        "size": 10
      }
    },
    "ageAvg": {
      "avg": {
        "field": "age"
      }
    },
    "balanceAvg": {
      "avg": {
        "field": "balance"
      }
    }
  },
  "size": 0
}
```

结果：

```json
{
  "took" : 16,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 4,
      "relation" : "eq"
    },
    "max_score" : null,
    "hits" : [ ]
  },
  "aggregations" : {
    "ageAgg" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : 38,
          "doc_count" : 2
        },
        {
          "key" : 28,
          "doc_count" : 1
        },
        {
          "key" : 32,
          "doc_count" : 1
        }
      ]
    },
    "ageAvg" : {
      "value" : 34.0
    },
    "balanceAvg" : {
      "value" : 25208.0
    }
  }
}
```



### 复杂： 按照年龄聚合，并且求这些年龄段的这些人的平均薪资

```shell
GET bank/_search
{
  "query": {
    "match_all": {}
  },
  "aggs": {
    "ageAgg": {
      "terms": {
        "field": "age",
        "size": 100
      },
      "aggs": {
        "ageAvg": {
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  },
  "size": 0
}
```

结果：

```json
{
  "took" : 6,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1000,
      "relation" : "eq"
    },
    "max_score" : null,
    "hits" : [ ]
  },
  "aggregations" : {
    "ageAgg" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 463,
      "buckets" : [
        {
          "key" : 31,
          "doc_count" : 61,
          "ageAvg" : {
            "value" : 28312.918032786885
          }
        },
        {
          "key" : 39,
          "doc_count" : 60,
          "ageAvg" : {
            "value" : 25269.583333333332
          }
        },
        {
          "key" : 26,
          "doc_count" : 59,
          "ageAvg" : {
            "value" : 23194.813559322032
          }
        },
        {
          "key" : 32,
          "doc_count" : 52,
          "ageAvg" : {
            "value" : 23951.346153846152
          }
        },
        {
          "key" : 35,
          "doc_count" : 52,
          "ageAvg" : {
            "value" : 22136.69230769231
          }
        },
        {
          "key" : 36,
          "doc_count" : 52,
          "ageAvg" : {
            "value" : 22174.71153846154
          }
        },
        {
          "key" : 22,
          "doc_count" : 51,
          "ageAvg" : {
            "value" : 24731.07843137255
          }
        },
        {
          "key" : 28,
          "doc_count" : 51,
          "ageAvg" : {
            "value" : 28273.882352941175
          }
        },
        {
          "key" : 33,
          "doc_count" : 50,
          "ageAvg" : {
            "value" : 25093.94
          }
        },
        {
          "key" : 34,
          "doc_count" : 49,
          "ageAvg" : {
            "value" : 26809.95918367347
          }
        }
      ]
    }
  }
}
```



### 查出所有年龄分布，并且这些年龄段中M的平均薪资和F的平均薪资以及这个年龄段的总体平均薪资

```shell
GET bank/_search
{
  "query": {
    "match_all": {}
  },
  "aggs": {
    "ageAgg": {
      "terms": {
        "field": "age",
        "size": 10
      },
      "aggs": {
        "genderAgg": {
          "terms": {
            "field": "gender.keyword"
          },
          "aggs": {
            "balanceAvg": {
              "avg": {
                "field": "balance"
              }
            }
          }
        },
        "ageBalanceAvg": {
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  },
  "size": 0
}
```

结果：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823164516.png)



---



## Mapping 映射

### 基本概念

Mapping(映射) Maping是用来定义一个文档（document），以及它所包含的属性（field）是如何存储和索引的。比如：使用maping来定义：

- 哪些字符串属性应该被看做全文本属性（full text fields）；
- 哪些属性包含数字，日期或地理位置；
- 文档中的所有属性是否都嫩被索引（all 配置）；
- 日期的格式；
- 自定义映射规则来执行动态添加属性；
- 查看mapping信息 GET bank/_mapping

**映射类型**：https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823201624.png)



### 新版本变化

ElasticSearch7-去掉type概念

1. 关系型数据库中两个数据表示是独立的，即使他们里面有相同名称的列也不影响使用，但ES中不是这样的。elasticsearch是基于Lucene开发的搜索引擎，而ES中不同type下名称相同的filed最终在Lucene中的处理方式是一样的。
   - 两个不同type下的两个user_name，在ES同一个索引下其实被认为是同一个filed，你必须在两个不同的type中定义相同的filed映射。否则，不同type中的相同字段名称就会在处理中出现冲突的情况，导致Lucene处理效率下降。
   - 去掉type就是为了提高ES处理数据的效率。
2. Elasticsearch 7.x URL中的type参数为可选。比如，索引一个文档不再要求提供文档类型。
3. Elasticsearch 8.x 不再支持URL中的type参数。



### 创建映射

创建索引并指定映射

```shell
PUT /my_index
{
  "mappings": {
    "properties": {
      "age": {
        "type": "integer"
      },
      "email": {
        "type": "keyword"
      },
      "name": {
        "type": "text"
      }
    }
  }
}
```

输出：

```json
{
  "acknowledged" : true,
  "shards_acknowledged" : true,
  "index" : "my_index"
}
```



### 查看映射

```shell
GET /my_index
```

输出：

```json
{
  "my_index" : {
    "aliases" : { },
    "mappings" : {
      "properties" : {
        "age" : {
          "type" : "integer"
        },
        "email" : {
          "type" : "keyword"
        },
        "name" : {
          "type" : "text"
        }
      }
    },
    "settings" : {
      "index" : {
        "creation_date" : "1598185097151",
        "number_of_shards" : "1",
        "number_of_replicas" : "1",
        "uuid" : "Lvskaa5xSDuYa7DAHDostQ",
        "version" : {
          "created" : "7040299"
        },
        "provided_name" : "my_index"
      }
    }
  }
}
```



### 添加新的字段映射

```shell
PUT /my_index/_mapping
{
  "properties": {
    "employee-id": {
      "type": "keyword",
      "index": false
    }
  }
}
```

输出：

```json
{
  "acknowledged" : true
}
```

- 这里的 "index": false，表明新增的字段不能被检索，只是一个冗余字段。



###  更新映射

对于已经存在的字段映射，我们不能更新。更新必须创建新的索引，进行数据迁移。

Except for supported [mapping parameters](https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-params.html), you can’t change the mapping or field type of an existing field. Changing an existing field could invalidate data that’s already indexed.

If you need to change the mapping of a field in a data stream’s backing indices, see [*Change mappings and settings for a data stream*](https://www.elastic.co/guide/en/elasticsearch/reference/current/data-streams-change-mappings-and-settings.html).

If you need to change the mapping of a field in other indices, create a new index with the correct mapping and [reindex](https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-reindex.html) your data into that index.

Renaming a field would invalidate data already indexed under the old field name. Instead, add an [`alias`](https://www.elastic.co/guide/en/elasticsearch/reference/current/alias.html) field to create an alternate field name.

### 数据迁移

- 先创建new_twitter的正确映射。然后使用如下方式进行数据迁移。

```shell
POST reindex [固定写法]
{
  "source":{
      "index":"twitter"
   },
  "dest":{
      "index":"new_twitters"
   }
}
```

- 将旧索引的type下的数据进行迁移

```shell
POST reindex [固定写法]
{
  "source":{
      "index":"twitter",
      "twitter":"twitter"
   },
  "dest":{
      "index":"new_twitters"
   }
}
```

https://www.elastic.co/guide/en/elasticsearch/reference/7.6/docs-reindex.html



对 bank 进行操作。

#### 查看基本信息

```shell
GET /bank/_search
```

```json
{
  "took" : 0,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1000,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "bank",
        "_type" : "account", // 这里的数据还有有 type 的
        "_id" : "1",
        "_score" : 1.0,
        "_source" : {
          "account_number" : 1,
          "balance" : 39225,
          "firstname" : "Amber",
          "lastname" : "Duke",
          "age" : 32,
          "gender" : "M",
          "address" : "880 Holmes Lane",
          "employer" : "Pyrami",
          "email" : "amberduke@pyrami.com",
          "city" : "Brogan",
          "state" : "IL"
        }
      },
      ...
```

如果我们不需要 type 了，就在迁移数据的时候去掉。

```shell
GET /bank/_mapping
```

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823204905.png)

#### 需要将 mapping 类型改为 integer 。

```json
PUT /newbank
{
  "mappings": {
    "properties": {
      "account_number": {
        "type": "long"
      },
      "address": {
        "type": "text"
      },
      "age": {
        "type": "integer"
      },
      "balance": {
        "type": "long"
      },
      "city": {
        "type": "keyword"
      },
      "email": {
        "type": "keyword"
      },
      "employer": {
        "type": "keyword"
      },
      "firstname": {
        "type": "text"
      },
      "gender": {
        "type": "keyword"
      },
      "lastname": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "state": {
        "type": "keyword"
      }
    }
  }
}
```

输出：

```json
{
  "acknowledged" : true,
  "shards_acknowledged" : true,
  "index" : "newbank"
}
```

#### 查看“newbank”的映射：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200823205154.png)

将bank中的数据迁移到newbank中

```shell
POST _reindex
{
  "source": {
    "index": "bank",
    "type": "account"
  },
  "dest": {
    "index": "newbank"
  }
}
```

```shell
#! Deprecation: [types removal] Specifying types in reindex requests is deprecated.
{
  "took" : 1287,
  "timed_out" : false,
  "total" : 1000,
  "updated" : 0,
  "created" : 1000,
  "deleted" : 0,
  "batches" : 1,
  "version_conflicts" : 0,
  "noops" : 0,
  "retries" : {
    "bulk" : 0,
    "search" : 0
  },
  "throttled_millis" : 0,
  "requests_per_second" : -1.0,
  "throttled_until_millis" : 0,
  "failures" : [ ]
}
```

---



## 分词器&&Nginx

一个tokenizer（分词器）接收一个字符流，将之分割为独立的tokens（词元，通常是独立的单词），然后输出tokens流。

例如：whitespace tokenizer遇到空白字符时分割文本。它会将文本“Quick brown fox!”分割为[Quick,brown,fox!]。

该tokenizer（分词器）还负责记录各个terms(词条)的顺序或position位置（用于phrase短语和word proximity词近邻查询），以及term（词条）所代表的原始word（单词）的start（起始）和end（结束）的character offsets（字符串偏移量）（用于高亮显示搜索的内容）。

elasticsearch提供了很多内置的分词器，可以用来构建custom analyzers（自定义分词器）。

关于分词器： https://www.elastic.co/guide/en/elasticsearch/reference/7.6/analysis.html

### 安装分词器

分词器版本需要和 ElasticSearch 版本对应。

https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.4.2/elasticsearch-analysis-ik-7.4.2.zip

在前面安装的 elasticsearch 时，我们已经 elasticsearch 容器的“/usr/share/elasticsearch/plugins”目录，映射到宿主机的“ /mydata/elasticsearch/plugins”目录下，所以比较方便的做法就是下载“/elasticsearch-analysis-ik-7.4.2.zip”文件，然后解压到该文件夹下即可。安装完毕后，需要重启elasticsearch容器。

```shell
[root@itzhouc plugins]# pwd
/mydata/elasticsearch/plugins
[root@itzhouc plugins]# wget https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.4.2/elasticsearch-analysis-ik-7.4.2.zip
[root@itzhouc plugins]# uzip elasticsearch-analysis-ik-7.4.2.zip
[root@itzhouc plugins]# cd ik
[root@itzhouc ik]# ls
commons-codec-1.9.jar    config                               httpclient-4.5.2.jar  plugin-descriptor.properties
commons-logging-1.2.jar  elasticsearch-analysis-ik-7.4.2.jar  httpcore-4.4.4.jar    plugin-security.policy
[root@itzhouc ik]# pwd
/mydata/elasticsearch/plugins/ik
[root@itzhouc plugins]# docker restart elasticsearch 
```



### 测试分词器

```shell
GET my_index/_analyze
{
   "analyzer": "ik_smart", 
   "text":"我是中国人"
}
```

输出：

```json
{
  "tokens" : [
    {
      "token" : "我",
      "start_offset" : 0,
      "end_offset" : 1,
      "type" : "CN_CHAR",
      "position" : 0
    },
    {
      "token" : "是",
      "start_offset" : 1,
      "end_offset" : 2,
      "type" : "CN_CHAR",
      "position" : 1
    },
    {
      "token" : "中国人",
      "start_offset" : 2,
      "end_offset" : 5,
      "type" : "CN_WORD",
      "position" : 2
    }
  ]
}
```

ik 分词器安装成功。



---



### 自定义词库

Ik 分词器有时候还是不能满足分词需求，比如一些比较新的网络用语，不能识别进行分词。为了解决这个问题，需要我们自定义词库。

自定义字库的基本思路是修改 ik 分词的配置文件 `/ik/config/IKAnalyzer.cfg.xml`，让 ik 分词器向远程词库发送请求得到最新的单词。远程词库可以借助于 nginx。nginx 的安装配置参考 nginx 配置部分。

- 修改配置：/mydata/elasticsearch/plugins/ik/config/IKAnalyzer.cfg.xml

```shell
[root@itzhouc config]# pwd
/mydata/elasticsearch/plugins/ik/config
[root@itzhouc config]# ls
extra_main.dic         extra_single_word_full.dic      extra_stopword.dic  main.dic         quantifier.dic  suffix.dic
extra_single_word.dic  extra_single_word_low_freq.dic  IKAnalyzer.cfg.xml  preposition.dic  stopword.dic    surname.dic
[root@itzhouc config]#
```

原配置：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
        <comment>IK Analyzer 扩展配置</comment>
        <!--用户可以在这里配置自己的扩展字典 -->
        <entry key="ext_dict"></entry>
         <!--用户可以在这里配置自己的扩展停止词字典-->
        <entry key="ext_stopwords"></entry>
        <!--用户可以在这里配置远程扩展字典 -->
        <!-- <entry key="remote_ext_dict">words_location</entry> -->
        <!--用户可以在这里配置远程扩展停止词字典-->
        <!-- <entry key="remote_ext_stopwords">words_location</entry> -->
</properties>
```

修改后：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
        <comment>IK Analyzer 扩展配置</comment>
        <!--用户可以在这里配置自己的扩展字典 -->
        <entry key="ext_dict"></entry>
         <!--用户可以在这里配置自己的扩展停止词字典-->
        <entry key="ext_stopwords"></entry>
        <!--用户可以在这里配置远程扩展字典 -->
        <entry key="remote_ext_dict">http://47.96.30.109/es/fenci.txt</entry>
        <!--用户可以在这里配置远程扩展停止词字典-->
        <!-- <entry key="remote_ext_stopwords">words_location</entry> -->
</properties>
```

- 重启 ES

```shell
[root@itzhouc config]# docker restart elasticsearch
```

- 在 Kibana 中测试

```shell
GET /my_index/_analyze
{
   "analyzer": "ik_max_word", 
   "text":"乔碧萝殿下"
}
```

结果：

```json
{
  "tokens" : [
    {
      "token" : "乔碧萝",
      "start_offset" : 0,
      "end_offset" : 3,
      "type" : "CN_WORD",
      "position" : 0
    },
    {
      "token" : "殿下",
      "start_offset" : 3,
      "end_offset" : 5,
      "type" : "CN_WORD",
      "position" : 1
    }
  ]
}
```

---



## Nginx 安装配置

- 拉取镜像

```shell
[root@itzhouc ~]# docker pull nginx:1.10
```

- 随便启动一个nginx实例，只是为了复制出配置

```shell
docker run -p80:80 --name nginx -d nginx:1.10   
```

- 将容器内的配置文件拷贝到/mydata/nginx/conf/ 下

```shell
[root@itzhouc mydata]# pwd
/mydata
[root@itzhouc mydata]# ls
elasticsearch  mysql  nginx  redis
[root@itzhouc mydata]# docker container cp nginx:/etc/nginx .   # 将容器内的配置文件拷贝出来
[root@itzhouc mydata]# ls
elasticsearch  mysql  nginx  redis
[root@itzhouc mydata]# cd nginx/
[root@itzhouc nginx]# ls
conf.d  fastcgi_params  koi-utf  koi-win  mime.types  modules  nginx.conf  scgi_params  uwsgi_params  win-utf
[root@itzhouc nginx]# docker stop nginx
nginx
[root@itzhouc nginx]# docker rm nginx
nginx
[root@itzhouc nginx]# cd ../
[root@itzhouc mydata]# ls
elasticsearch  mysql  nginx  redis
[root@itzhouc mydata]# mv nginx conf  # 重命名
[root@itzhouc mydata]# ls
conf  elasticsearch  mysql  redis
[root@itzhouc mydata]# mkdir nginx
[root@itzhouc mydata]# mv conf nginx/  # 移动文件
[root@itzhouc mydata]# ls
elasticsearch  mysql  nginx  redis
[root@itzhouc mydata]# cd nginx
[root@itzhouc nginx]# ls
conf
[root@itzhouc nginx]#
```

- 创建新的Nginx

```shell
docker run -p 80:80 --name nginx \
 -v /mydata/nginx/html:/usr/share/nginx/html \
 -v /mydata/nginx/logs:/var/log/nginx \
 -v /mydata/nginx/conf/:/etc/nginx \
 -d nginx:1.10
```

- 设置开机启动 nginx

```shell
docker update nginx --restart=always
```

- 创建“/mydata/nginx/html/index.html”文件，测试是否能够正常访问

```shell
[root@itzhouc nginx]# ls
conf  html  logs
[root@itzhouc nginx]# cd html/
[root@itzhouc html]# echo '<h2>hello nginx!</h2>' >index.html
[root@itzhouc html]# ls
index.html
```

访问：http://47.96.30.109/

![](https://gitee.com/itzhouq/images/raw/master/notes/20200824210756.png)



> 云服务器需要开启端口

### 创建自定义词库

```shell
[root@itzhouc html]# mkdir es
[root@itzhouc html]# cd es
[root@itzhouc es]# vim fenci.txt
```

写入：

```shell
乔碧萝
尚硅谷
```

访问：http://47.96.30.109/es/fenci.txt

![](https://gitee.com/itzhouq/images/raw/master/notes/20200824211333.png)

自定义词库能访问。再去配置 ik 的配置文件。



---



## Spring Boot 整合 ElasticSearch

### 引入

使用 elasticsearch-rest-high-level-client 客户端。

官方文档：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-getting-started-initialization.html

- 导入依赖

```shell
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>7.4.2</version>
</dependency>
```

- 配置类:参考文档配置

```java
/**
 * @author itzhouq
 * @date 2020/8/24 21:54
 */

@Configuration
@EnableDiscoveryClient
public class GuliamllElasticSearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost("47.96.30.109", 9200, "http")));
    }
}
```

- 测试

```java
@Resource
private RestHighLevelClient restHighLevelClient;

@Test
public void contextLoads() {
  System.out.println(restHighLevelClient);
  //org.elasticsearch.client.RestHighLevelClient@64d93096
}
```



---

### 设置项

文档：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-low-usage-requests.html#java-rest-low-usage-request-options

在配置类中添加设置项：

```java
public static final RequestOptions COMMON_OPTIONS;
static {
  RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
  //        builder.addHeader("Authorization", "Bearer " + TOKEN);
  //        builder.setHttpAsyncResponseConsumerFactory(
  //                new HttpAsyncResponseConsumerFactory
  //                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
  COMMON_OPTIONS = builder.build();
}
```

### 测试保存更新

文档：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-index.html

```java
@Test
public void indexData() throws IOException {
  IndexRequest indexRequest = new IndexRequest("users");
  indexRequest.id("1");
  User user = new User();
  user.setUserName("Jack");
  user.setAge(22);
  user.setGender("男");
  String jsonString = JSON.toJSONString(user);
  // 设置要保存的内容
  indexRequest.source(jsonString, XContentType.JSON);
  // 执行创建索引和保存数据
  IndexResponse index = restHighLevelClient.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
  System.out.println(index);
}

@Data
class User {
  private String userName;
  private String gender;
  private Integer age;
}
```

查询：

```shell
GET users/_search
```

结果：

```JSON
{
  "took" : 874,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 2,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "users",
        "_type" : "_doc",
        "_id" : "AhfwIHQBUPyF98YwDbov",
        "_score" : 1.0,
        "_source" : {
          "age" : 20,
          "gender" : "男",
          "userName" : "Jack"
        }
      },
      {
        "_index" : "users",
        "_type" : "_doc",
        "_id" : "1",
        "_score" : 1.0,
        "_source" : {
          "age" : 22,
          "gender" : "男",
          "userName" : "Jack"
        }
      }
    ]
  }
}
```



---

### 测试查询数据

文档：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-search.html

API 操作都可以在文档中查到。

示例：搜索address中包含mill的所有人的年龄分布以及平均年龄，平均薪资。

原生 DSL：

```shell
GET bank/_search
{
  "query": {
    "match": {
      "address": "Mill"
    }
  },
  "aggs": {
    "ageAgg": {
      "terms": {
        "field": "age",
        "size": 10
      }
    },
    "ageAvg": {
      "avg": {
        "field": "age"
      }
    },
    "balanceAvg": {
      "avg": {
        "field": "balance"
      }
    }
  }
}
```

Java 实现：

```java
/**
* 复杂检索:在bank中搜索address中包含mill的所有人的年龄分布以及平均年龄，平均薪资
* @throws IOException
*/
@Test
public void searchData() throws IOException {
  //1. 创建检索请求
  SearchRequest searchRequest = new SearchRequest();

  //1.1）指定索引
  searchRequest.indices("bank");
  //1.2）构造检索条件
  SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
  sourceBuilder.query(QueryBuilders.matchQuery("address","Mill"));

  //1.2.1)按照年龄分布进行聚合
  TermsAggregationBuilder ageAgg= AggregationBuilders.terms("ageAgg").field("age").size(10);
  sourceBuilder.aggregation(ageAgg);

  //1.2.2)计算平均年龄
  AvgAggregationBuilder ageAvg = AggregationBuilders.avg("ageAvg").field("age");
  sourceBuilder.aggregation(ageAvg);
  //1.2.3)计算平均薪资
  AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
  sourceBuilder.aggregation(balanceAvg);

  System.out.println("检索条件："+sourceBuilder);
  searchRequest.source(sourceBuilder);
  //2. 执行检索
  SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
  System.out.println("检索结果："+searchResponse);

  //3. 将检索结果封装为Bean
  SearchHits hits = searchResponse.getHits();
  SearchHit[] searchHits = hits.getHits();
  for (SearchHit searchHit : searchHits) {
    String sourceAsString = searchHit.getSourceAsString();
    Account account = JSON.parseObject(sourceAsString, Account.class);
    System.out.println(account);

  }

  //4. 获取聚合信息
  Aggregations aggregations = searchResponse.getAggregations();

  Terms ageAgg1 = aggregations.get("ageAgg");

  for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
    String keyAsString = bucket.getKeyAsString();
    System.out.println("年龄："+keyAsString+" ==> "+bucket.getDocCount());
  }
  Avg ageAvg1 = aggregations.get("ageAvg");
  System.out.println("平均年龄："+ageAvg1.getValue());

  Avg balanceAvg1 = aggregations.get("balanceAvg");
  System.out.println("平均薪资："+balanceAvg1.getValue());

}
```

结果：

```shell
检索条件：{"query":{"match":{"address":{"query":"Mill","operator":"OR","prefix_length":0,"max_expansions":50,"fuzzy_transpositions":true,"lenient":false,"zero_terms_query":"NONE","auto_generate_synonyms_phrase_query":true,"boost":1.0}}},"aggregations":{"ageAgg":{"terms":{"field":"age","size":10,"min_doc_count":1,"shard_min_doc_count":0,"show_term_doc_count_error":false,"order":[{"_count":"desc"},{"_key":"asc"}]}},"ageAvg":{"avg":{"field":"age"}},"balanceAvg":{"avg":{"field":"balance"}}}}
检索结果：{"took":2,"timed_out":false,"_shards":{"total":1,"successful":1,"skipped":0,"failed":0},"hits":{"total":{"value":4,"relation":"eq"},"max_score":5.4032025,"hits":[{"_index":"bank","_type":"account","_id":"970","_score":5.4032025,"_source":{"account_number":970,"balance":19648,"firstname":"Forbes","lastname":"Wallace","age":28,"gender":"M","address":"990 Mill Road","employer":"Pheast","email":"forbeswallace@pheast.com","city":"Lopezo","state":"AK"}},{"_index":"bank","_type":"account","_id":"136","_score":5.4032025,"_source":{"account_number":136,"balance":45801,"firstname":"Winnie","lastname":"Holland","age":38,"gender":"M","address":"198 Mill Lane","employer":"Neteria","email":"winnieholland@neteria.com","city":"Urie","state":"IL"}},{"_index":"bank","_type":"account","_id":"345","_score":5.4032025,"_source":{"account_number":345,"balance":9812,"firstname":"Parker","lastname":"Hines","age":38,"gender":"M","address":"715 Mill Avenue","employer":"Baluba","email":"parkerhines@baluba.com","city":"Blackgum","state":"KY"}},{"_index":"bank","_type":"account","_id":"472","_score":5.4032025,"_source":{"account_number":472,"balance":25571,"firstname":"Lee","lastname":"Long","age":32,"gender":"F","address":"288 Mill Street","employer":"Comverges","email":"leelong@comverges.com","city":"Movico","state":"MT"}}]},"aggregations":{"lterms#ageAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":38,"doc_count":2},{"key":28,"doc_count":1},{"key":32,"doc_count":1}]},"avg#ageAvg":{"value":34.0},"avg#balanceAvg":{"value":25208.0}}}
GulimallSearchApplicationTests.Account(account_number=970, balance=19648, firstname=Forbes, lastname=Wallace, age=28, gender=M, address=990 Mill Road, employer=Pheast, email=forbeswallace@pheast.com, city=Lopezo, state=AK)
GulimallSearchApplicationTests.Account(account_number=136, balance=45801, firstname=Winnie, lastname=Holland, age=38, gender=M, address=198 Mill Lane, employer=Neteria, email=winnieholland@neteria.com, city=Urie, state=IL)
GulimallSearchApplicationTests.Account(account_number=345, balance=9812, firstname=Parker, lastname=Hines, age=38, gender=M, address=715 Mill Avenue, employer=Baluba, email=parkerhines@baluba.com, city=Blackgum, state=KY)
GulimallSearchApplicationTests.Account(account_number=472, balance=25571, firstname=Lee, lastname=Long, age=32, gender=F, address=288 Mill Street, employer=Comverges, email=leelong@comverges.com, city=Movico, state=MT)
年龄：38 ==> 2
年龄：28 ==> 1
年龄：32 ==> 1
平均年龄：34.0
平均薪资：25208.0
```



---




