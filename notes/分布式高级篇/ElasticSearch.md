

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






