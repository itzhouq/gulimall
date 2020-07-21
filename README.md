



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
git checkout -b dev            ## 创建一个 dev 分支并切换到该分支
git remote prune origin        ## 删除远程无效分支
```

7、数据库初始化

---



## 四、快速开发

### 1、人人开源搭建后台管理系统

后台管理系统使用【人人开源】的脚手架搭建。

前端：https://gitee.com/renrenio/renren-fast-vue

后端：https://gitee.com/renrenio/renren-fast

![](https://gitee.com/itzhouq/images/raw/master/notes/20200719091310.png)



下载上述两个项目到本地。分别删除其中的`.git`文件夹。

将后端项目拷贝到总项目根目录下，添加到模块中。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200719092047.png)

- 初始化数据库

新建数据库 gulimall-admin，执行项目下`db/mysql.sql`文件初始化数据库。

修改数据库连接配置信息。启动项目。



- 前端初始化

使用 Vscode 打开项目。确保有 node 环境。

Npm 是随 node 一起安装的包管理工具。为了加快下载依赖速度，需要配置 npm 使用淘宝镜像：

```shell
# itzhouq @ itzhouqdeMacBook-Pro in ~ [10:41:50]
$ npm config set registry https://registry.npm.taobao.org

# itzhouq @ itzhouqdeMacBook-Pro in ~ [10:42:48]
$ npm config get registry
https://registry.npm.taobao.org/
```



- 下载依赖

进入前端项目根目录下，执行以下命令：

```shell
npm install
```

依赖下载完成之后启动项目

- 启动前端项目

```shell
npm run dev
```

前端项目下载依赖的时候容易报错，注意看报错信息。我遇到一个依赖需要手动下载。

- 前后端联调

启动后端项目，前端能登录进去就说明调通了。

---

### 2、逆向工程搭建和使用

- 下载逆向工程

  下载 renren-generator 到本地  https://gitee.com/renrenio/renren-generator.git。

  删除文件夹中的 `.git/`文件夹，导入总项目。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200719154402.png)

- 修改 renren-generator 配置

修改 application.yml 的数据库连接为 pms 数据库。

修改 generator.properties 代码生成器信息。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200719155017.png)

注释掉 `resource/template/Controller.java.vm`文件中的所有的 `@RequiresPermissions`注解。

这个是跟 `Shrio`相关的注解，暂时用不上。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200719155417.png)

- 启动项目，生成代码

![](https://gitee.com/itzhouq/images/raw/master/notes/20200719155901.png)



下载解压文件，将 `main`文件夹拷贝到 `product`项目的 `src`目录下，替换原来的 `main`文件夹。下面解决报错和依赖问题。

- 创建common 模块

使用 maven 创建一个 `gulimall-common`模块，其余的模块都依赖于这个模块。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200719161005.png)

在 `product`模块中引入`common`模块的依赖：

```xml
<dependency>
  <groupId>com.atguigu.gulimall</groupId>
  <artifactId>gulimall-common</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

- 根据 `prodcut`中的报错信息，导入相关依赖。

---

### 3、配置和测试微服务基本 CRUD 功能

- 整合`mybatis-plus`

1、导入依赖

在 `product`模块中导入 `mybatis-plus`依赖：

```xml
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>mybatis-plus-boot-starter</artifactId>
  <version>3.3.1</version>
</dependency>
```

2、配置

 `product`模块中新建 `application.yml`。配置数据源和mappers文件位置、主键生成策略。

```yml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://47.96.30.109:3306/gulimall_pms?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml    # 配置 mapper 文件的位置
  global-config:
    db-config:
      id-type: auto  # 配置主键生成规则
```

在启动类上添加`@MapperScan`注解。

```java
@MapperScan("com.atguigu.gulimall.product.dao")
@SpringBootApplication
public class GulimallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }
}
```

- 测试

在 `GulimallProductApplicationTests`测试类中编写单元测试。

---

### 4、逆向工程生成所有微服务基本 CRUD 代码

采用上述类似方式生成代码，注意修改配置。

---

## 五、分布式组件

**Spring Cloud Alibaba 中文文档：https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md**

根据官网，使用之前先使用版本管理限定版本。在 common 模块中引入：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>2.1.0.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```



### 1、Nacos：注册中心

官网：https://nacos.io/zh-cn/docs/quick-start.html

GitHub实例：https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/nacos-example/nacos-discovery-example/readme-zh.md 。

- 引入依赖 Nacos Discovery Starter

在 common 中引入：

```xml
<dependency>
     <groupId>com.alibaba.cloud</groupId>
     <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
 </dependency>
```

- 配置 Nacos Server 的地址

如果我们需要将 coupon 模块注册到注册中心，需要在 coupon 模块的配置文件中添加配置。

```yml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
```

- 使用 @EnableDiscoveryClient 注解开启服务注册与发现功能

在 coupon 的启动类上添加该注解。

```java
@MapperScan("com.atguigu.gulimall.coupon.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallCouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallCouponApplication.class, args);
    }
}
```

- 启动 Nacos Server

根据官网下载压缩包解压后，启动 Nacos Server。

> 官网下载太慢，可以使用这个地址：https://gitee.com/itzhouq/software

1. Linux/Unix/Mac 操作系统，执行命令 `sh startup.sh -m standalone`
2. Windows 操作系统，执行命令 `cmd startup.cmd`

- 应用启动

增加配置，在 coupon 模块的配置文件中添加基本配置信息

```
spring:
  application:
    name: gulimall-coupon
server:
  port: 7000
```

**注意**：如果被注册的模块没有服务名，则不会显示。

- 验证

访问 127.0.0.1:8848/nacos， 使用 nacos/nacos 登录。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200720235345.png)

再注册一个 product 试试。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200720235556.png)

服务注册成功时会提示：

```
nacos registry, gulimall-product 192.168.0.104:10000 register finished
```

- 注意：如果模块启动报错，可以尝试更换`spring cloud` 的版本。

我开始使用的 `2.2.0.RELEASE`版本报错：

```
nacos Caused by: java.lang.NoClassDefFoundError: org/springframework/cloud/client/discovery/ReactiveDiscoveryClient
```

换成 `2.1.0.RELEASE`后成功。



### 2、OpenFeign：声明式远程调用

Feign 是一个声明式的 HTTP 客户端，他的目的就是让远程调用更加简单。Feign 提供了 HTTP 请求模板，通过编写简答的接口和插入注解，就可以定义好 HTTP 请求的参数、格式、地址等信息。

Feign 整合了 Ribbon（负载均衡）和 Hytrix（服务熔断）,可以让我们不再需要显式地使用这两个组件。

SpringCloudFeign 在 Nexflix 的基础上扩展了对 Spring MVC 注解的支持，在其实现下，我们只需要创建一个接口并采用注解的方式来配置它，即可完成对服务提供方的接口绑定。简化了 SpringCloudRibbon 自行封装服务客户调用客户端的开发量。

假设会员想要从优惠券服务中获取所有的优惠券信息。我们使用这个需求尝试使用 OpenFeign。

- 引入依赖

首先 member 项目中已经引用了 OpenFeign：

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

这样 member 就可以调用别的服务了。

- 编写测试接口

首先在 coupon 中编写测试接口：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200721004042.png)

- 在 member 中编写远程调用接口

![](https://gitee.com/itzhouq/images/raw/master/notes/20200721004508.png)

- 开启远程调用

在 member 启动类上添加注解 `@EnableFeignClients`。

```java
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.atguigu.gulimall.member.feign")
@MapperScan("com.atguigu.gulimall.member.dao")
public class GulimallMemberApplication {
	public static void main(String[] args) {
		SpringApplication.run(GulimallMemberApplication.class, args);
	}
}
```

- 编写测试

![](https://gitee.com/itzhouq/images/raw/master/notes/20200721005117.png)

- 启动测试

重启项目访问：http://localhost:8000/member/member/coupons

![](https://gitee.com/itzhouq/images/raw/master/notes/20200721005336.png)

成功。

---



### 3、Nacos 作为配置中心

文档：https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/nacos-example/nacos-config-example/readme-zh.md

- 引入依赖

各个模块都需要使用配置中心，所以在 common 模块中添加依赖：

```xml
<dependency>
     <groupId>com.alibaba.cloud</groupId>
     <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
 </dependency>
```



- 创建配置文件 `bootstrap.properties`

选择一个模块测试，这里选择 coupon 模块。在其 `resources`文件夹下新建配置文件`bootstrap.properties`，该文件的优先级高于 `application.yml`。

配置 Nacos Config 元数据：

```properties
spring.application.name=gulimall-coupon
spring.cloud.nacos.config.server-addr=127.0.0.1:8848
```

以前我们读取配置都是通过配置文件 `application.properties`配置，然后在代码中使用`@Value`注解读取。

比如：在 coupon 的 `application.properties`中添加配置：

```properties
coupon.user.name=zhangsan
coupon.user.age=20
```

然后在代码中读取：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200721205601.png)

启动项目，可以读取到配置的数据。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200721205641.png)

但是这样的问题是，每次我们修改配置需要重启服务才能生效。这种问题在服务比较多和服务不能随便停止的项目中弊端凸显。为此需要使用配置中心。



- 配置数据集

当项目中引入 Nacos-Config 依赖之后，项目启动时会去配置中心加载配置，从启动信息中可以看到：

```
Located property source: CompositePropertySource {name='NACOS', propertySources=[NacosPropertySource {name='gulimall-coupon.properties'}]}
```

可以看到数据源默认是`应用名.properties`。

我们去 Nacos 的控制台添加配置测试。

- 添加配置

在 Nacos 配置管理 --> 配置列表 --> 新增配置 中添加如下配置，重新项目，可以读取到配置。 

![](https://gitee.com/itzhouq/images/raw/master/notes/20200721210416.png)



- 动态获取配置

第一次可以读取到配置，但是我们修改的配置不会再次被读取到。根据官方文档，需要在对应的 Controller 类上添加 @RefreshScope 打开动态刷新功能。再次测试可以动态刷新。



#### Nacos 进阶

- 命名空间

主要用于配置隔离。默认新增的配置都在 public 空间。我们可以创建多个命名空间，比如 dev、prod、test等。在配置中间中对应配置。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200721213520.png)

```properties
spring.cloud.nacos.config.namespace=23374e75-f3b6-420c-bd9a-9e5c4bb97ac3
```

该维度也用于不同微服务使用不同命名空间，每个微服务只读取自己命名空间下的所有配置。

- 配置集

一组相关或者不相关的配置项的集合称之为配置集。在系统中，一个配置文件通常就是一个配置集，包含了系统各个方面的配置。例如，一个配置集可能包含了数据源、线程池、日志级别等配置项。

- 配置集 ID

Data id 类似于文件名。

- 配置分组

默认所有的配置集都属于 DEFAULT_GROUP。

对应的配置：

```properties
spring.cloud.nacos.config.group=dev
```



**本项目：每个微服务创建自己的命名空间，使用配置分组区分环境，dev、test、prod。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200721233557.png)

`bootstrap.properties`的配置：

```properties
spring.application.name=gulimall-coupon
spring.cloud.nacos.config.server-addr=127.0.0.1:8848

spring.cloud.nacos.config.namespace=23374e75-f3b6-420c-bd9a-9e5c4bb97ac3
spring.cloud.nacos.config.group=dev
```

- 同时加载多个配置集

微服务任何配置信息，任何配置文件都可以放在配置中心中。

只需要在 `bootstrap.properties`中说明加载配置中心的哪些配置即可

以前 SpringBoot 任何从配置文件中获取值的方式都可以使用，配置中心有的配置优先使用配置中心的。

下面提取`application.yml`中的所有配置到配置中心：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200722001836.png)

在 `bootstrap.properties`中引入这些配置，删除 `application.yml`后，重启项目还是可以正常启动和读取配置信息。

项目启动的控制台信息中可以看到读取的是哪个配置：

```
2020-07-22 00:17:51.141  INFO 7070 --- [           main] c.a.c.n.c.NacosPropertySourceBuilder     : Loading nacos data, dataId: 'datasource.yml', group: 'dev'
2020-07-22 00:17:51.157  INFO 7070 --- [           main] c.a.c.n.c.NacosPropertySourceBuilder     : Loading nacos data, dataId: 'mybatis.yml', group: 'dev'
2020-07-22 00:17:51.160  INFO 7070 --- [           main] c.a.c.n.c.NacosPropertySourceBuilder     : Loading nacos data, dataId: 'other.yml', group: 'dev'
2020-07-22 00:17:51.164  INFO 7070 --- [           main] c.a.c.n.c.NacosPropertySourceBuilder     : Loading nacos data, dataId: 'gulimall-coupon.properties', group: 'dev'
```

这种配置的方式更加灵活。





























