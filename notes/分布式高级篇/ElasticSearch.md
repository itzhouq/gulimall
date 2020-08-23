

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



### 2、索引一个文档


