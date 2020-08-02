



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





### 4、Gateway

官网文档：https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.3.RELEASE/reference/html/

网关作为流量的入口，常用功能包括路由转发、权限校验、限流控制等。而SpringCloud Gateway 作为 SpringCloud官方推出的第二代网关框架，取代了 Zuul 网关。

使用网关可以将一些与业务无关的功能提取出来，比如鉴权、限流、日志等。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200722214328.png)



 ![](https://gitee.com/itzhouq/images/raw/master/notes/20200722215237.png)



网关提供了 API 全托管服务，丰富的 API 管理功能，辅助企业管理大规模的 API，以降低管理成本和安全风险，包括协议适协议转发、安全策略、防刷、流量、监控日志等功能。

Spring Cloud Gateway 旨在提供一种简单而有效的方式来对 API 进行路由，并为他们提供切面，如安全性，监控和弹性等。



![](https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.3.RELEASE/reference/html/images/spring_cloud_gateway_diagram.png)



![](https://gitee.com/itzhouq/images/raw/master/notes/20200722220638.png)

![](https://gitee.com/itzhouq/images/raw/master/notes/20200722220807.png)



项目中引入 Gateway。

- 新建模块 gulimall-gateway

![](https://gitee.com/itzhouq/images/raw/master/notes/20200722222317.png)



引入 Gateway 依赖，common 依赖，统一 SpringBoot 和 Spring Cloud 版本。

- 开启服务注册与发现

在启动类上添加 @EnableDiscoveryClient 注解。

- 配置 nacos 地址和模块应用名

```properties
server.port=88
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
spring.application.name=gulimall-gateway
```

- 添加配置中心文件 bootstrap

```properties
spring.application.name=gulimall-gateway

spring.cloud.nacos.config.server-addr=27.0.0.1:8848

spring.cloud.nacos.config.namespace=86fe8e82-b32d-4806-84c8-17c5433fafdf
```

在 nacos 中添加命名空间和配置

- 启动项目

- 根据文档https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.3.RELEASE/reference/html/#the-query-route-predicate-factory配置转发

application.yml

```yml
spring:
  cloud:
    gateway:
      routes:
        - id: test_route
          uri: https://www.baidu.com
          predicates:
            - Query=url, baidu
        - id: qqq
          uri: https://www.qq.com
          predicates:
            - Query=url, qq

```

项目启动后访问 http://localhost:88/hello?url=qq 会转发到 qq 页面。

#### Gateway 项目启动问题排查

- 引入 的common 依赖中包含 Spring-web 的依赖，数据库的依赖，但是 Gateway 可能不会使用数据库，也就不会添加 数据库 URL 等配置信息，这时候项目会报错。解决的办法是排除 数据库的依赖：

```xml
<exclusion>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</exclusion>
<exclusion>
  <groupId>com.baomidou</groupId>
  <artifactId>mybatis-plus-boot-starter</artifactId>
</exclusion>
```

- 还有一种解决方式是在 `@SpringBootApplication`中使用 `exclude`属性排除掉

```java
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
public class GulimallGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulimallGatewayApplication.class, args);
	}
}
```

关于 Spring Boot 自动配置的原理，可以参考：https://www.cnblogs.com/niechen/p/9027804.html

我在这里遇到一个问题就是，排除 `DataSourceAutoConfiguration`后，项目依然报错，查看报错信息， 看到还是加载了数据源。明明已经排除了。。。后面仔细看项目启动的初始化信息，注意到：

```
2020-07-26 12:30:42.125  INFO 9857 --- [           main] c.a.d.s.b.a.DruidDataSourceAutoConfigure : Init DruidDataSource
```

我猜测这里是数据库连接池 DruidDataSource 问题，尝试将这个也排除

```java
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
public class GulimallGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GulimallGatewayApplication.class, args);
	}
}
```

输入 Druid 后面就有自动提示。排除之后成功启动。

- 我开始以为是 `exclude = {DataSourceAutoConfiguration.class `这个没有生效，开源码确定生效了。

  - 点进 `@SpringBootApplication`的源码，该注解由三个注解组合而成

  ```java
  @SpringBootConfiguration
  @EnableAutoConfiguration
  @ComponentScan
  ```

  - 查看 `@EnableAutoConfiguration`

  ```java
  @AutoConfigurationPackage
  @Import({AutoConfigurationImportSelector.class})
  public @interface EnableAutoConfiguration {
      String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";
  
      Class<?>[] exclude() default {};
  
      String[] excludeName() default {};
  }
  ```

  - 这个注解 `AutoConfigurationImportSelector`是重点，其中有个方法 `exclusions` 是排除自动配置的：

  ```java
  protected AutoConfigurationImportSelector.AutoConfigurationEntry getAutoConfigurationEntry(AutoConfigurationMetadata autoConfigurationMetadata, AnnotationMetadata annotationMetadata) {
    if (!this.isEnabled(annotationMetadata)) {
      return EMPTY_ENTRY;
    } else {
      AnnotationAttributes attributes = this.getAttributes(annotationMetadata);
      List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);
      configurations = this.removeDuplicates(configurations);
      Set<String> exclusions = this.getExclusions(annotationMetadata, attributes);
      this.checkExcludedClasses(configurations, exclusions);
      configurations.removeAll(exclusions);
      configurations = this.filter(configurations, autoConfigurationMetadata);
      this.fireAutoConfigurationImportEvents(configurations, exclusions);
      return new AutoConfigurationImportSelector.AutoConfigurationEntry(configurations, exclusions);
    }
  }
  ```

  在这个地方断点，就能确定某个自动配置是否被排除。

  

---



## 六、前端基础

### 1、ES6 语法

参考：https://www.cnblogs.com/itzhouq/p/12345150.html

### 2、VUE 基础

#### MVVM 思想

- M：即 Model ，模型，包括数据和一些基本操作
- V：即 View ，视图，页面渲染结果
- VM：即 View-Model ，模型和视图间的双向操作，无需开发人员干涉

在 MVVM 之前，开发人员从后端获取需要的数据模型，然后要通过 DOM 操作 Model 渲染到 View 中，而后当用户操作视图，我们还需要通过 DOM获取 View 中的数据，然后同步到 Model中。

而 MVVM 中的 VM 要做的事情就是把 DOM操作完全封装起来，开发人员不用关心 Model 和 View 之前要如何互相影响。

#### Vue

Vue (读音 /vjuː/，类似于 **view**) 是一套用于构建用户界面的**渐进式框架**。与其它大型框架不同的是，Vue 被设计为可以自底向上逐层应用。Vue 的核心库只关注视图层，不仅易于上手，还便于与第三方库或既有项目整合。另一方面，当与[现代化的工具链](https://cn.vuejs.org/v2/guide/single-file-components.html)以及各种[支持类库](https://github.com/vuejs/awesome-vue#libraries--plugins)结合使用时，Vue 也完全能够为复杂的单页应用提供驱动。

官网教程：https://cn.vuejs.org/v2/guide/

#### HelloWord

- 安装

在用 Vue 构建大型应用时推荐使用 NPM 安装[[1\]](https://cn.vuejs.org/v2/guide/installation.html#footnote-1)。NPM 能很好地和诸如 [webpack](https://webpack.js.org/) 或 [Browserify](http://browserify.org/) 模块打包器配合使用。同时 Vue 也提供配套工具来开发[单文件组件](https://cn.vuejs.org/v2/guide/single-file-components.html)。

新建项目文件夹

```shell
$ npm init -y  # 初始化
$ npm install vue   # 最新稳定版
```

在代码中引入，编写 HelloWorld 程序。

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <div id="app">
        <h1>{{name}}, 非常帅</h1>
    </div>

    <script src="./node_modules/vue/dist/vue.js"></script>

    <script>
        let vm = new Vue({
            el: '#app',
            data: {
                name: "张三"
            }
        })
    </script>
</body>
</html>
```



#### 插件

Vue 开发中浏览器插件 vue-devtools，可以方便的调试。

官方地址：https://github.com/vuejs/vue-devtools

Google 商店：https://chrome.google.com/webstore/detail/vuejs-devtools/nhdogjmejiglipccpnnnanhbledajbpd?utm_source=chrome-ntp-icon

#### 基本语法

文档：https://cn.vuejs.org/v2/guide/syntax.html#Attribute

- 插值表达式

作用：给标签体绑定值

格式：{{xxx}}

说明：

1. 该表达式支持 JS 语法，可以调用 JS 内置函数，但是必须有返回值，没有结果的表达式不允许使用，如：

`let a = 1 + 1`

2. 可以直接获取 Vue 实例中定义的数据或函数

- 插值闪烁

使用插值表达式在网速较慢的情况下会显示原始的 {{}}，加载完毕后才显示正常的数据，该现象称为插值闪烁。

- v-text 和 v-html

给属性绑定值，v-html 会渲染标签，v-text 会原样显示

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <div id="app">
        <span v-text="msg"></span> <br>
        <span v-html="msg"></span>

        {{msg}} --- {{1 + 1}} --- {{hello()}}
    </div>

    <script src="./node_modules/vue/dist/vue.js"></script>

    <script>
        new Vue({
            el: "#app",
            data: {
                msg: "<h1>Hello</h1>"
            },
            methods: {
                hello() {
                    return "World"
                }
            }
        })
    </script>
</body>
</html>
```

访问改页面会显示：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200725140435.png)

---

- v-bind

作用：给属性绑定值

用法：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    
    <div id="app">
        <a v-bind:href="link">gogogo</a>

        <!-- class, style -->
        <span v-bind:class="{active: isActive, 'text-danger': hasError}">你好</span>
    </div>

    <script src="./node_modules/vue/dist/vue.js"></script>

    <script>
        let vm = new Vue({
            el: "#app",
            data: {
                link: "http://baidu.com",
                isActive: true,
                hasError: true
            }
        })
    </script>
</body>
</html>
```

常用在 class、style 等属性的绑定上。效果如下：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200725142617.png)

缩写：

```html
<!-- 完整语法 -->
<a v-bind:href="url">...</a>

<!-- 缩写 -->
<a :href="url">...</a>

<!-- 动态参数的缩写 (2.6.0+) -->
<a :[key]="url"> ... </a>
```



---



#### 单向绑定和双向绑定

上面几个指令都是单向绑定，页面元素改变，对应数据不一定会改变。如果需要页面元素和数据的双向绑定，需要使用 v-model 指令。

- v-model

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <div id="app">
        精通的语言：<br>
        <input type="checkbox" v-model='language' value="Java">Java <br>
        <input type="checkbox" v-model='language' value="PHP">PHP <br>
        <input type="checkbox" v-model='language' value="Python">Python <br>
        选中了 {{language.join(',')}}
    </div>
    <script src="./node_modules/vue/dist/vue.js"></script>
    <script>
        let vm = new Vue({
            el: "#app",
            data: {
                language: []
            }
        })
    </script>
</body>
</html>
```

页面数据：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200725143347.png)

---

#### 指令 v-on、v-for 和 v-if

- v-on 用于绑定事件，如 click 等



缩写：

```html
<!-- 完整语法 -->
<a v-on:click="doSomething">...</a>

<!-- 缩写 -->
<a @click="doSomething">...</a>

<!-- 动态参数的缩写 (2.6.0+) -->
<a @[event]="doSomething"> ... </a>
```



- v-for 用于遍历
- v-if 用于判断



#### 计算属性和侦听器

文档：https://cn.vuejs.org/v2/guide/computed.html

#### 组件化基础

文档：https://cn.vuejs.org/v2/guide/components.html

#### 生命周期和钩子函数

文档：https://cn.vuejs.org/v2/guide/instance.html

#### 路由

文档：https://cn.vuejs.org/v2/guide/routing.html

#### 使用 VUE 脚手架进行模块化开发

1. 全局安装 webpack

```shell
npm install webpack -g
```

2. 全局安装 Vue 脚手架

```shell
npm install -g @vue/cli-init
```

安装提示 zsh: command not found: vue ，使用下面命令安装成功：

```shell
npm install @vue/cli -g
```

3. 初始化项目

```shell
vue init webpack appname
```

初始化过程：

```shell
➜  Desktop vue init webpack vue-demo

? Project name vue-demo
? Project description A Vue.js project
? Author itzhouq <itzhouq@163.com>
? Vue build standalone
? Install vue-router? Yes
? Use ESLint to lint your code? No
? Set up unit tests No
? Setup e2e tests with Nightwatch? No
? Should we run `npm install` for you after the project has been created? (recommended) npm

   vue-cli · Generated "vue-demo".


# Installing project dependencies ...
```



4. 启动 Vue 项目

```shell
npm run dev
```

项目启动后自动打开首页。



#### 整合 ElementUI 快速开发

官网：https://element.eleme.cn/#/zh-CN

文档：https://element.eleme.cn/#/zh-CN/component/installation



#### 设置 Vue 组件模板

在 vscode 中首选项 --> 用户片段 --> 输入 vue 新建全局代码，输入以下模板：

```
{
	"生成vue模板": {
		"prefix": "vue",
		"body": [
			"<!-- $1 -->",
			"<template>",
			"<div class='$2'>$5</div>",
			"</template>",
			"",
			"<script>",
			"//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）",
			"//例如：import 《组件名称》 from '《组件路径》';",
			"",
			"export default {",
			"//import引入的组件需要注入到对象中才能使用",
			"components: {},",
			"data() {",
			"//这里存放数据",
			"return {",
			"",
			"};",
			"},",
			"//监听属性 类似于data概念",
			"computed: {},",
			"//监控data中的数据变化",
			"watch: {},",
			"//方法集合",
			"methods: {",
			"",
			"},",
			"//生命周期 - 创建完成（可以访问当前this实例）",
			"created() {",
			"",
			"},",
			"//生命周期 - 挂载完成（可以访问DOM元素）",
			"mounted() {",
			"",
			"},",
			"beforeCreate() {}, //生命周期 - 创建之前",
			"beforeMount() {}, //生命周期 - 挂载之前",
			"beforeUpdate() {}, //生命周期 - 更新之前",
			"updated() {}, //生命周期 - 更新之后",
			"beforeDestroy() {}, //生命周期 - 销毁之前",
			"destroyed() {}, //生命周期 - 销毁完成",
			"activated() {}, //如果页面有keep-alive缓存功能，这个函数会触发",
			"}",
			"</script>",
			"<style scoped>",
			"$4",
			"</style>"
		],
		"description": "生成vue模板"
	}
}
```

使用时直接输入 vue 即可。



----



## 七、商品服务

类似 coupon 模块，在 nacos 中做配置文件，保证 product 模块能正常启动。

###  1、三级分类

#### 1）递归树形结构获取

- 导入 SQL

将资料中的数据 `/guli/docs/分布式基础篇/sql/pms_catelog.sql`导入到对应数据库中执行。

- 查询商品的所有分类和子分类信息，以树形结构组装起来

在 product模块的 CategoryController 中编写代码。

```
查出所有分类以及子分类，以树形结构组装起来
```

- CategoryController

```java
/**
 * 查出所有分类以及子分类，以树形结构组装起来
 */
@RequestMapping("/list/tree")
public R list() {
  List<CategoryEntity> entities = categoryService.listWithTree();
  return R.ok().put("data", entities);
}
```

- CategoryServiceImpl

```java
@Override
public List<CategoryEntity> listWithTree() {
  // 1. 查出所有分类
  List<CategoryEntity> entities = baseMapper.selectList(null);

  // 2. 组装成父子的树形结构
  // 2.1 找到所有的一级分类
  List<CategoryEntity> level1Menus = entities.stream()
    .filter(categoryEntity ->categoryEntity.getParentCid() == 0)
    .map(menu -> {
    // 2.2 设置子菜单
    menu.setChildren(getChildren(menu, entities));
    return menu;
    // 2.3 排序
  }).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
    .collect(Collectors.toList());
  return level1Menus;
}

/**
 * 递归查找所有菜单的子菜单
 */
private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
  List<CategoryEntity> children = all.stream().filter(categoryEntity -> 
    categoryEntity.getParentCid() == root.getCatId()
  ).map(categoryEntity -> {
    // 1. 找到子菜单
    categoryEntity.setChildren(getChildren(categoryEntity, all));
    return categoryEntity;
  }).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))).collect(Collectors.toList());
  return children;
}
```

- 对于 CategoryEntity 实体，添加了一个表示自菜单的属性，但是这个属性不属于数据库字段，因此需要添加注解

```java
@TableField(exist = false)
private List<CategoryEntity> children;
```

- 测试：访问http://localhost:10000/product/category/list/tree

菜单是有层级结构的。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200726012730.png)

---



#### 2）配置网关路由和路径重写

开始编写后端管理系统的前端项目，维护三级分类的增删改查。

使用 vscode 打开 renren-fast-vue，启动后台服务 renren-fast。

前端使用 `npm run dev`启动。访问系统后，在菜单管理中手动添加“商品系统”的一级菜单。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200726090248.png)

添加“分类”子菜单：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200726090514.png)

现在需要在分类维护标签中显示三级分类信息。观察页面路由规则，路由 `sys/role`会被解析成 `sys-role`存在数据库中。页面的请求路径是 `http://localhost:8001/#/sys-role`。前端对应的机制是在对应目录`src/views/modules/sys`下的一个组件`role.vue`。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200726091210.png)

我们安装该机制编写，在modules 下新建文件夹product，新建组件category.vue。

根据 ElementUI 的树形组件编写树形菜单：https://element.eleme.cn/#/zh-CN/component/tree

```vue
<template>
  <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    return {
      data: [],
      defaultProps: {
        children: "children",
        label: "label",
      },
    };
  },
  methods: {
    handleNodeClick(data) {
      console.log(data);
    },
    getMenus() {
        this.$http({
          url: this.$http.adornUrl('/product/category/list/tree'),
          method: 'get'
        }).then(data => {
          console.log('成功获取到数据', data)
        })
    }
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
      this.getMenus();
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {}, //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style scoped>
</style>
```

保存访问，出现路径不一致问题，我们需要修改前面的基本路径。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200726093602.png)

在前端项目中搜索基本路径可以定位到 `static/config/index.js`文件。

将该路径修改为网关地址：

```JavaScript
// api接口请求地址
window.SITE_CONFIG['baseUrl'] = 'http://localhost:8080/renren-fast';
```

```JavaScript
// api接口请求地址
window.SITE_CONFIG['baseUrl'] = 'http://localhost:88';
```

刷新页面需要重新登录，但是验证码的请求失败，原来是请求的路径不对，请求应该是到达后台系统renren-fast而不是网关。

```
Request URL: http://localhost:88/captcha.jpg?uuid=05ffa49a-adbe-4c3f-8e81-3a2bfd816701
```

现在需要网关将这个请求发送到 renren-fast 服务，所以需要将 renren-fast 服务注册到注册中心 nacos 中。

引入 common、配置应用名、配置注册中心地址、开启服务注册发现功能。

- 网关配置处理请求

使用 **断言Predicate**处理请求路径。根据 Spring Cloud Gateway 的 文档配置：[The Path Route Predicate Factory](https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.3.RELEASE/reference/html/#the-path-route-predicate-factory)

定义一个规则，只要前端请求，都带上`/api`的前缀，同时修改`static/config/index.js`文件:

```javascript
  // api接口请求地址
  window.SITE_CONFIG['baseUrl'] = 'http://localhost:88/api';
```

修改网关的配置文件：

```yml
- id: admin_route
uri: lb://renren-fast     ## load-balance 负载均衡
predicates:
- Path=/api/**
```

重启网关和后台系统，验证码依然不可用，查看路径是：

```
http://localhost:88/captcha.jpg?uuid=cc53c398-ddb0-41fa-8c38-58c0bf94ed41
```

丢失了应用名：

```
http://localhost:88/renren-fast/captcha.jpg?uuid=cc53c398-ddb0-41fa-8c38-58c0bf94ed41
```

这个时候需要使用 Gateway 的 Filter 路径重写功能。

过滤器GatewayFilter：[ The `RewritePath` `GatewayFilter` Factory](https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.3.RELEASE/reference/html/#the-rewritepath-gatewayfilter-factory)

```yml
        - id: admin_route
          uri: lb://renren-fast     ## load-balance 负载均衡
          predicates:
            - Path=/api/**
          filters:
            - RewritePath=/api/(?<segment>/?.*), /renren-fast/$\{segment}

## 前端项目， /api
## http://localhost:88/api/captcha.jpg  http://localhost:8080/captcha.jpg
```

**注意**：如果出现 503 报错，很有可能是 Gateway 中没有被引入 nacos 依赖，没有注入到注册中心。

重启项目之后可以加载验证码了，但是登录报错。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200726143824.png)

跨域问题。

#### 3）网关统一配置跨域

关于跨域：https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Access_control_CORS

跨域解决：

 - 方式一：使用 nginx 部署为统一域

 - 配置当次请求允许跨域，添加响应头

   - `Access-Controller-Allow-Origin`：支持哪些来源的请求跨域
   - `Access-Control-Allow-Methods`：支持哪些方法跨域
   - `Access-Control-Allow-Credentials`：跨域请求默认不包含 cookie，设置为 true 可以包含
   - `Access-Control-Expose-Header`：跨域请求暴露的字段

   CORS 请求时，XMLHttpRequest 对象的 getResponseHeader() 方法只能拿到 6 个基本字段：Cache-Control、Control-Language、Content-Type、Expires、Last-Modified、Paragma。如果想要拿到其他字段，就必须在 Access-Control-Expose-Headers 里面指定。

   - `Access-Control-Max-Age`：表明该响应的有效时间为多少秒，在有效时间内，浏览器无需为同一请求再次发送预检请求。

在 Gateway 中编写过滤器统一处理跨域问题。

```java
package com.atguigu.gulimall.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 跨域处理过滤器
 */
@Configuration
public class GulimallCorsConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 配置跨域
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}
```

重启项目 Gateway 和 renren-fast，出现报错信息：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200726151127.png)

意思是 Access-Control-Allow-Origin 有多个值。再看响应头信息：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200726151241.png)

也是有多个。猜测可能是项目中配置了两次跨域，但是整个项目只有renren-fast 不是自己编写的，里面可能已经存在跨域配置，注释掉重启项目即可解决该问题。

---

#### 4) 树形展示三级分类数据

- 后台服务路由

跨域问题虽然解决了，但是三级分类的请求还是 404 。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200727234022.png)

这个时候请求 `http://localhost:88/api/product/category/list/tree`会被转发到 renren-fast，而不是我们想要的 product。所以需要修改 Gateway 路由设置。

在 gateway 的配置文件 `application.yml` 中添加配置:

```yml
- id: product_route
uri: lb://gulimall-product     ## load-balance 负载均衡
predicates:
- Path=/api/product/**
filters:
- RewritePath=/api/(?<segment>/?.*), /$\{segment}

- id: admin_route
uri: lb://renren-fast     ## load-balance 负载均衡
predicates:
- Path=/api/**
filters:
- RewritePath=/api/(?<segment>/?.*), /renren-fast/$\{segment}
```

注意将精细的路由写在上面优先匹配。

配置好 product 服务。重启 Gateway、renren-fast、product 服务，尝试请求：

`http://localhost:10000/product/category/list/tree`能拿到数据说明后台服务之间通信是流畅的。

![](https://gitee.com/itzhouq/images/raw/master/notes/20200728000922.png)

---

- 前端数据获取展示

前端获取的数据在 data.data 中，使用解构表达式解构到 data 中：

```JavaScript
getMenus() {
  this.$http({
    url: this.$http.adornUrl("/product/category/list/tree"),
    method: "get"
  }).then(({data}) => {
    console.log("成功获取到菜单数据", data.data);
    this.menus = data.data;
  });
}
```

![](https://gitee.com/itzhouq/images/raw/master/notes/20200728001521.png)

菜单中要展示的菜单名称和子菜单属性名可以看参考文档https://element.eleme.cn/#/zh-CN/component/tree，这里的菜单名和子菜单属性名分别为 `name` 和 `children`。



![](https://gitee.com/itzhouq/images/raw/master/notes/20200728002147.png)

vue 代码：category.vue 文件

```javascript
<template>
  <el-tree :data="menus" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
</template>

<script>
export default {
  components: {},
  data() {
    return {
      menus: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
    };
  },
  computed: {},
  watch: {},
  methods: {
    getMenus() {
      this.$http({
        url: this.$http.adornUrl("/product/category/list/tree"),
        method: "get"
      }).then(({data}) => {
          console.log("成功获取到菜单数据", data.data);
          this.menus = data.data;
      });
    }
  },
  created() {
      this.getMenus();
  },
  mounted() {},
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
};
</script>
<style scoped>
</style>
```

页面效果：

![](https://gitee.com/itzhouq/images/raw/master/notes/20200728002328.png)

---

#### 5) 删除-页面效果

可以删除的菜单：没有子菜单并且没有被别的菜单引用。

根据 elementUI 的Tree 组件文档编写：https://element.eleme.cn/#/zh-CN/component/tree

![](https://gitee.com/itzhouq/images/raw/master/notes/20200730212036.png)

复制示例代码到 tree 标签中：

```vue
<el-tree :data="menus" :props="defaultProps" @node-click="handleNodeClick">
  <span class="custom-tree-node" slot-scope="{ node, data }">
    <span>{{ node.label }}</span>
<span>
      <el-button type="text" size="mini" @click="() => append(node, data)">Append</el-button>
<el-button type="text" size="mini" @click="() => remove(node, data)">Delete</el-button>
</span>
</span>
</el-tree>
```

node 指的是当前节点，data 存的是数据。

编写点击事件的方法：

```javascript
append(data) {
  console.log("append", node, data)
},
remove(node, data) {
  console.log("remove", node, data)
},
```

![](https://gitee.com/itzhouq/images/raw/master/notes/20200730214441.png)

- expand-on-click-node：是否在点击节点的时候展开或者收缩节点， 默认值为 true，如果为 false，则只有点箭头图标的时候才会展开或者收缩节点。
- 一级和二级菜单才显示Append，没有子菜单才显示 Delete：添加判断
- show-checkbox：节点是否可被选择
- node-key：每个树节点用来作为唯一标识的属性，整棵树应该是唯一的

添加以上属性

```vue
<el-tree
    :data="menus"
    :props="defaultProps"
    :expand-on-click-node="false"
    show-checkbox
    node-key="catId"
  >
    <span class="custom-tree-node" slot-scope="{ node, data }">
      <span>{{ node.label }}</span>
      <span>
        <el-button
          v-if="node.level <= 2"
          type="text"
          size="mini"
          @click="() => append(node, data)"
        >Append</el-button>
        <el-button
          v-if="node.childNodes.length == 0"
          type="text"
          size="mini"
          @click="() => remove(node, data)"
        >Delete</el-button>
      </span>
    </span>
  </el-tree>
```

#### 6) 删除-逻辑删除

- 测试 product 中 category 的删除功能。

mybatis-plus 自动生成的delete方法可以根据传入的菜单Id的数组，批量删除菜单。

```java
/**
* 删除
* @RequestBody ：获取请求体,必须发送Post请求
* SpringMVC自动将请求体的数据json转为对应的对象
*/
@RequestMapping("/delete")
public R delete(@RequestBody Long[] catIds){
  categoryService.removeByIds(Arrays.asList(catIds));
  return R.ok();
}
```

但是这里有两个问题：一是这样删除的方式是物理删除，删除之后不能找回；二是在删除之前需要检查被删除菜单是否被其他菜单引用，没有引用的菜单才能被删除。

一般系统中的删除都采用逻辑删除。意思是通过一个字段来标识删除的状态，不是真的删除一条记录。mybatis也提供了逻辑删除的功能，我们通过官方文档编写实现。

文档：https://mp.baomidou.com/guide/logic-delete.html

- 配置 application.yml

```yml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: flag  # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
```

- 实体类字段上加上`@TableLogic`注解

```java
@TableLogic
private Integer deleted;
```

我们选择category 表的show_status字段作为标识字段，所以在改字段添加这个注解。

```java
@TableLogic(value = "1", delval = "0")
private Integer showStatus;
```

因为删除的标识和全局刚好是反的，所以这里通过value和delval属性自定义删除标识，只在改表有效。

配置文件中添加配置显示 SQL 语句：

```yml
logging:
  level:
    com.atguigu.gulimall: debug
```

重启项目。

再次发送删除的请求，相关记录还在，只是吸怪了show_status 的值为 0，说明逻辑删除生效。

```
localhost:88/api/product/category/delete
```

发送的 SQL：

```sql
Preparing: UPDATE pms_category SET show_status=0 WHERE cat_id IN ( ? ) AND show_status=1
```

---







