



## 一、项目架构图

![架构图](https://gitee.com/itzhouq/images/raw/master/notes/%E8%B0%B7%E7%B2%92%E5%95%86%E5%9F%8E-%E5%BE%AE%E6%9C%8D%E5%8A%A1%E6%9E%B6%E6%9E%84%E5%9B%BE.jpg)

--------------

![](https://gitee.com/itzhouq/images/raw/master/notes/20200718120349.png)





## 二、分布式基础概念

### 1、微服务

微服务架构风格，就像是把一个**单独的应用程序**开发为一套**小服务**，每个**小服务**运行在**自己的进程**中，并使用轻量级机制通信，通常是 HTTP API。这些服务围绕业务能力来构建，并通过完全自动化部署机制来独立部署。这些服务使用不同的编程语言书写，以及不同数据储存技术，并保持最低限度的集中式管理。

**简而言之：拒绝大型单体应用，基于业务边界进行微服务化拆分，各个服务独立部署运行。**



### 2、集群、分布式、节点

集群是个物理形态，分布式是个工作方式。

只要是一堆机器，就可以叫集群，他们是不是一起协作干活，这个谁也不知道。《分布式系统原理与范型》定义：分布式系统是若干独立计算机的集合，这些计算机对于用户来说就像单个相关系统，分布式系统（distribute system）是建立在网络智商的软件系统。分布式是指将不同的业务分布在不同的地方。集群指的是将几台机器集中在一起，实现同一业务。

例如：**京东是一个分布式系统，众多业务运行在不同的机器，所有业务构成一个大型的业务集群。**每一个小的业务，比如用户系统，访问压力大的时候一台服务器是不够的。我们就应该将用户系统部署到多个服务器，也就是每一个业务系统也可以做集群化。

**分布式的每一个节点，都可以做集群。而集群并不一定就是分布式的。**

节点：集群中的一个服务器。



### 3、远程调用

在分布式系统中，各个服务可能处于不同的主机，但是服务之间不可避免的徐亚 互相调用，我们称之为远程调用。

Spring Cloud 中使用的 HTTP + JSON 的方式完成远程调用。

![远程调用](https://gitee.com/itzhouq/images/raw/master/notes/20200718112318.png)

### 4、负载均衡

![负载均衡](https://gitee.com/itzhouq/images/raw/master/notes/20200718113233.png)

分布式系统中，A 服务需要调用 B 服务，B 服务在多台服务器中都存在，A 调用任意一个服务器均可完成功能。

为了是每一个服务器都不要太忙或者太闲，我们可以负载均衡的调用每一个服务器，提升网站的健壮性。

常见的负载均衡算法：

- **轮询**：为每一个请求选择健康池中的第一个后端服务器，然后按照顺序往后依次选择，直到最后一个，然后选循环。
- **最小连接**：优先选择连接数最小，也就是压力最小的后端服务器，在会话较长的情况下可以考虑这种方式。
- 散列：根绝请求源的 IP 的散列（hash）来选择要转发的服务器。这种方式可以一定程度上保证特定用户能连接到相同的服务器。如果你的应用需要处理状态而要求用户能连接到和之前相同的服务器，可以考虑采取这种方式。



### 5、服务注册、服务发现、注册中心

A 服务调用 B 服务，A 服务并不知道 B 服务当前在哪几台服务器有，哪些正常的，哪些服务已经下线。解决这个问题就可以引入注册中心。

![注册中心](https://gitee.com/itzhouq/images/raw/master/notes/20200718113438.png)



如果某些服务下线，我们其他人可以实时的感知到其他服务的状态，从而避免调用不可用的服务。



### 6、配置中心

![配置中心](https://gitee.com/itzhouq/images/raw/master/notes/20200718113725.png)

每一个服务最终都有大量的配置，并且每个服务器都有可能部署在多台服务器上，我们经常需要变更配置，我们可以让每个服务在配置中心获取自己的配置。

配置中心用来集中管理微服务的配置信息。



### 7、服务熔断、服务降级

在微服务架构中，微服务之间通过网络进行通信，存在互相依赖，当其中一个服务不可用时，有可能会造成雪崩效应。要防止这种的情况，必须要有容错机制来保护服务。



![雪崩效应](https://gitee.com/itzhouq/images/raw/master/notes/20200718114625.png)

- **服务熔断**：

  设置服务的超时，当被调用的服务经常失败到达某个阈值，我们可以开启断路保护机制，后来的请求不再去调用这个服务。本地直接返回默认的数据。

- 服务降级

  在运维期间，当系统处于高峰期，系统资源紧张，我们可以让非核心业务降级运行。降级：某些服务不处理，或者简单处理（比如：抛异常、返回 null、调用 Mock 数据、调用 Faceback 处理逻辑）。



### 8、API 网关

在微服务架构中，API Gateway 作为整体架构的重要组件，他**抽象了微服务中都需要的公共功能**，同时提供了**客户端负载均衡、服务自动熔断、灰度发布、统一认证、限流流控、日志统计**等丰富的功能，帮助我们解决很多 API 管理难题。

![网关](https://gitee.com/itzhouq/images/raw/master/notes/20200718115443.png)



## 三、环境搭建

### 1、安装 Linux 虚拟机

- 下载安装 Virtual Box ，开启虚拟化: https://www.virtualbox.org/wiki/Downloads
- 下载安装 Vagrant ：Vagrant 可以快速下载镜像文件整合 Virtual Box。
  - Vagrant 下载：https://www.vagrantup.com/downloads.html
  - Vagrant 官方仓库：https://app.vagrantup.com/boxes/search

测试 Vagrant 是否安装成功：在终端中输入 vagrant 命令

```shell
# itzhouq @ itzhouqdeMacBook-Pro in ~ [12:11:23]
$ vagrant
Usage: vagrant [options] <command> [<args>]

    -h, --help                       Print this help.

Common commands:
     box             manages boxes: installation, removal, etc.
     cloud           manages everything related to Vagrant Cloud
     destroy         stops and deletes all traces of the vagrant machine
     global-status   outputs status Vagrant environments for this user
     halt            stops the vagrant machine
     help            shows the help for a subcommand
     init            initializes a new Vagrant environment by creating a Vagrantfile
     login
```

不报错说明安装成功。

- 初始化一个centos 7 系统

```shell
$ vagrant init centos/7
A `Vagrantfile` has been placed in this directory. You are now
ready to `vagrant up` your first virtual environment! Please read
the comments in the Vagrantfile as well as documentation on
`vagrantup.com` for more information on using Vagrant.
```

在家目录下会创建一个 Vagrantfile 文件。

- 启动环境

根据提示输入：

```shell
$ vagrant up
Bringing machine 'default' up with 'virtualbox' provider...
==> default: Box 'centos/7' could not be found. Attempting to find and install...
    default: Box Provider: virtualbox
    default: Box Version: >= 0
==> default: Loading metadata for box 'centos/7'
    default: URL: https://vagrantcloud.com/centos/7
==> default: Adding box 'centos/7' (v2004.01) for provider: virtualbox
    default: Downloading: https://vagrantcloud.com/centos/boxes/7/versions/2004.01/providers/virtualbox.box
Download redirected to host: cloud.centos.org
Progress: 5% (Rate: 9883/s, Estimated time remaining: 18:45:04)
```

有个下载的过程。我本机下载太慢了。考虑使用外部下载。先使用 control + C 终止下载。

- 外部下载

复制链接：https://vagrantcloud.com/centos/boxes/7/versions/2004.01/providers/virtualbox.box。使用迅雷下载。

**下载后复制路径【换成自己的】，添加本地 box**。

```shell
$ vagrant box add centos/7 /Users/itzhouq/Downloads/virtualbox.box
==> box: Box file was not detected as metadata. Adding it directly...
==> box: Adding box 'centos/7' (v0) for provider:
    box: Unpacking necessary files from: file:///Users/itzhouq/Downloads/virtualbox.box
==> box: Successfully added box 'centos/7' (v0) for 'virtualbox'!
```

Successfully 成功了。

再使用 `vagrant up`启动：

```shell
$ vagrant up
Bringing machine 'default' up with 'virtualbox' provider...
An action 'up' was attempted on the machine 'default',
but another process is already executing an action on the machine.
Vagrant locks each machine for access by only one process at a time.
Please wait until the other Vagrant process finishes modifying this
machine, then try again.

If you believe this message is in error, please check the process
listing for any "ruby" or "vagrant" processes and kill them. Then
try again.
```

又报错了。意思是之前运行的程序没停止冲突了。

```shell
$ ps -ef | grep ruby
  501  1449  1448   0 12:19下午 ttys000    0:18.46 ruby /opt/vagrant/embedded/gems/2.2.9/gems/vagrant-2.2.9/bin/vagrant up
  501  1462  1449   0 12:19下午 ttys000    0:01.46 /opt/vagrant/embedded/bin/curl -q --fail --location --max-redirs 10 --verbose --user-agent Vagrant/2.2.9 (+https://www.vagrantup.com; ruby2.6.6)  --continue-at - --output /Users/itzhouq/.vagrant.d/tmp/box59ea1c1664883a8671659ef6c8355b0ed54a3954 https://vagrantcloud.com/centos/boxes/7/versions/2004.01/providers/virtualbox.box
  501  1810  1350   0  1:29下午 ttys000    0:00.00 grep --color=auto --exclude-dir=.bzr --exclude-dir=CVS --exclude-dir=.git --exclude-dir=.hg --exclude-dir=.svn --exclude-dir=.idea --exclude-dir=.tox ruby

# itzhouq @ itzhouqdeMacBook-Pro in ~ [13:29:26]
$ ps -ef | grep vagrant
  501  1448  1350   0 12:19下午 ttys000    0:00.01 vagrant up
  501  1449  1448   0 12:19下午 ttys000    0:18.46 ruby /opt/vagrant/embedded/gems/2.2.9/gems/vagrant-2.2.9/bin/vagrant up
  501  1462  1449   0 12:19下午 ttys000    0:01.46 /opt/vagrant/embedded/bin/curl -q --fail --location --max-redirs 10 --verbose --user-agent Vagrant/2.2.9 (+https://www.vagrantup.com; ruby2.6.6)  --continue-at - --output /Users/itzhouq/.vagrant.d/tmp/box59ea1c1664883a8671659ef6c8355b0ed54a3954 https://vagrantcloud.com/centos/boxes/7/versions/2004.01/providers/virtualbox.box
  501  1818  1350   0  1:29下午 ttys000    0:00.00 grep --color=auto --exclude-dir=.bzr --exclude-dir=CVS --exclude-dir=.git --exclude-dir=.hg --exclude-dir=.svn --exclude-dir=.idea --exclude-dir=.tox vagrant
```

尝试了网上一个办法：

```shell
kenorb commented on 27 Jun 2016 • 
Try either (when ruby process exists):

killall ruby
or:

vagrant global-status --prune
when ruby process doesn't exist, to fix the above issue.
```

还是不行，然后我重启，好了~~~

`vagrant up` 启动成功后会自动启动虚拟机。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200718133856.png)

- 连接虚拟机

vagrant 创建虚拟机的时候，创建了 SSH 连接，可以使用本地终端远程连接虚拟机。

```shell
# itzhouq @ itzhouqdeMacBook-Pro in ~ [13:36:33]
$ vagrant ssh
[vagrant@localhost ~]$
```

这样就是远程连接成功了。

系统 root 用户的密码是 vagrant。

- vagrant 其他常用命令：
  - vagrant ssh：自动使用 vagrant 用户连接虚拟机
  - vagrant upload source [ destination ] [ name | id] 上传文件

---

- 虚拟机网络设置

修改 Vagrantfile 固定 IP 地址。

https://blog.csdn.net/qq_29695701/article/details/86750556

本机开虚拟机发热太厉害，使用阿里云的服务器。

---

#### 1.1 阿里云主机

- 远程登录云主机

```shell
## 类似下面这种，IP 换成自己的
ssh -p 22 root@111.111.111.111
```

- 测试连通性
  - 获取本机外网 IP：在百度中输入 IP 地址可以得到

  - 使用阿里云的外网 IP 和本机的外网 IP 互相能 ping 通就没问题。



### 2、Linux 安装 Docker

Docker 虚拟化容器技术 Docker 基于镜像，可以秒级启动各种容器，每一种容器都是一个完整的运行环境，容器之间互相隔离。

![](https://docs.docker.com/engine/images/architecture.svg)





Docker 安装文档：https://docs.docker.com/engine/install/centos/

- 卸载旧版本

```shell
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
```

- 安装依赖

```shell
sudo yum install -y yum-utils
```

- 安装仓库

```shell
sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
```

- 安装 Docker

```shell
sudo yum install docker-ce docker-ce-cli containerd.io
```

- 查看版本

```shell
[root@itzhouc yum.repos.d]# docker -v
Docker version 19.03.12, build 48a66213fe
```

- 设置 Docker 开机自启

```shell
sudo systemctl enable docker
```

- 配置镜像

为了加快速度，需要配置阿里云镜像。登录阿里云之后在这个https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors链接中可以看到。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200718151955.png)

```shell
sudo mkdir -p /etc/docker

sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://as5ebylp.mirror.aliyuncs.com"]
}
EOF

sudo systemctl daemon-reload

sudo systemctl restart docker
```



### 3、Docker 安装 MySQL

- 拉取 MySQL5.7 的镜像

```shell
[root@itzhouc ~]# docker pull mysql:5.7
[root@itzhouc ~]# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
mysql               5.7                 d05c76dbbfcf        4 days ago          448MB
```

- 创建实例并启动

```shell
docker run -p 3306:3306 --name mysql \   ## 将容器的3306端口映射到主机的3306端口
-v /mydata/mysql/log:/var/log/mysql \    ## 将配置文件夹/var/log/mysql挂载到主机
-v /mydata/mysql/data:/var/lib/mysql \   ## 将配置文件夹/var/lib/mysql挂载到主机
-v /mydata/mysql/conf:/etc/mysql \			 ## 将配置文件夹/etc/mysql挂载到主机
-e MYSQL_ROOT_PASSWORD=123456 \          ## 初始化root用户密码
-d mysql:5.7
```

- 查看是否启动

```shell
[root@itzhouc ~]# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                               NAMES
4628e0a359b4        mysql:5.7           "docker-entrypoint.s…"   16 seconds ago      Up 15 seconds       0.0.0.0:3306->3306/tcp, 33060/tcp   mysql
```



- Docker 容器文件挂载与端口映射

![](https://gitee.com/itzhouq/images/raw/master/notes/20200718160225.png)

- 进入容器内部、查看 MySQL 的位置

```shell
[root@itzhouc ~]# docker exec -it mysql /bin/bash
root@4628e0a359b4:/# whereis mysql
mysql: /usr/bin/mysql /usr/lib/mysql /etc/mysql /usr/share/mysql
root@4628e0a359b4:/#
```

- 修改 MySQL 配置文件

退出容器，在挂载文件中修改配置文件：

```shell
vi /mydata/mysql/conf/my.cnf
```

```shell
[client]
default-character-set=utf-8

[mysql]
default-character-set=utf-8

[mysqld]
init_connect='SET collation_connection=utf8_unicode_ci'
init_connect='SET NAMES utf8'
default-set-server=utf-8
collation-server=utf8_unicode_ci
skip-cahracter-set-client-handshake
skip-name-resolve
```

- 重启 MySQL

```shell
docker restart mysql
```

启动成功即可。



### 4、 安装 Redis

- 下载镜像文件

```shell
docker pull redis
```

- 创建实例并启动

```shell
mkdir -p /mydata/redis/conf
touch /mydata/redis/conf/redis.conf

docker run -p 6379:6379 --name redis \
-v /mydata/redis/data:/data \
-v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf \
-d redis redis-server /etc/redis/redis.conf
```

- 连接

```shell
[root@itzhouc ~]# docker exec -it redis redis-cli
127.0.0.1:6379> ping
PONG
127.0.0.1:6379>
```

- Redis 配置

配置文件可以参考官网：

修改 Redis 持久化配置：

```shell
vi /mydata/redis/conf/redis.conf
添加配置 appendonly yes
docker restart redis
```



---



### 5、统一开发环境

- JDK：1.8 及以上

```shell
$ java -version
java version "1.8.0_241"
Java(TM) SE Runtime Environment (build 1.8.0_241-b07)
Java HotSpot(TM) 64-Bit Server VM (build 25.241-b07, mixed mode)
```

- maven：3.6.1 以上

- maven 配置

配置阿里云镜像

```xml
<mirror>
  <id>alimaven</id>
  <mirrorOf>central</mirrorOf>
  <name>aliyun maven</name>
  <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
</mirror>
```

配置使用 JDK 1.8 编译项目

```xml
<profile>  
  <id>jdk-1.8</id>  
  <activation>  
    <activeByDefault>true</activeByDefault>  
    <jdk>1.8</jdk>  
  </activation>  
  <properties>  
    <maven.compiler.source>1.8</maven.compiler.source>  
    <maven.compiler.target>1.8</maven.compiler.target>  
    <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>  
  </properties>   
</profile> 
```



- 前端开发工具：VScode，安装一些插件
  - Auto Close Tag
  - Auto Rename Tag
  - Chinese (Simplified) Language Pack for Visual Studio Code
  - ESLint
  - HTML CSS Support
  - HTML Snippets
  - JavaScript (ES6) code snippets
  - Live Server
  - open in browser
  - Vetur

6、配置 Git

7、项目结构创建、提交到码云/GitHub

- 创建一个项目下载到本地使用 IDEA 打开，创建模块

![](https://gitee.com/itzhouq/images/raw/master/notes/20200719001031.png)



依次创建 商品模块（product）、优惠券模块（coupon）、订单模块（order）、仓储模块（ware）、用户模块（user）。每个模块中默认选中 Spring Web 和 OpenFeign 组件。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200719002430.png)

- 设置聚合工程

随便复制一个 pom 文件到根目录下，修改为如下：

![](https://gitee.com/itzhouq/images/blob/master/notes/20200719004618.png)

- 添加总的服务

![](https://gitee.com/itzhouq/images/raw/master/notes/20200719004056.png)

- 修改忽略文件

```shell
**/mvnw
**/mvnw.cmd
**/.mvn

**/target/

.idea

**/.gitignore

*.iws
*.iml
*.ipr
log/

.DS_Store
```

6、代码提交

```shell
git status  ## 查看代码状态
git add .   ## 将没有被忽略的文件都添加到版本控制
git commit -m "project init"   ## 提交代码到本地仓库
git push origin master         ## 推送代码到远程 master 分支
git checkout -b dev              ## 创建一个 dev 分支并切换到该分支
```

7、数据库初始化









