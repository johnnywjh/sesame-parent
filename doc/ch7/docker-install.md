> centos 7.x

#### 1.设置yum镜像
> 国外的镜像安装速度很慢，采用清华大学 TUNA 镜像源。 
用root用户新建 /etc/yum.repos.d/docker.repo 文件，内容如下：
```
[dockerrepo]
name=Docker Repository
baseurl=https://mirrors.tuna.tsinghua.edu.cn/docker/yum/repo/centos7
enabled=1
gpgcheck=1
gpgkey=https://mirrors.tuna.tsinghua.edu.cn/docker/yum/gpg
```

#### 2.安装
> 执行命令：
```
sudo yum makecache
sudo yum install docker-engine
```

#### 3.启动docker服务
> 执行命令：
```
systemctl start docker.service
```

#### 4.测试docker服务
> 执行命令：
```
docker run hello-world
```
> 屏幕上如果输出以下类似信息，说明docker安装正常。
```
Unable to find image 'hello-world:latest' locally
latest: Pulling from library/hello-world
c04b14da8d14: Pull complete 
Digest: sha256:0256e8a36e2070f7bf2d0b0763dbabdd67798512411de4cdcf9431a1feb60fd9
Status: Downloaded newer image for hello-world:latest

Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker Hub account:
 https://hub.docker.com

For more examples and ideas, visit:
 https://docs.docker.com/engine/userguide/
```

#### 5.安装docker-compose
> 执行命令：
```
sudo curl -L https://github.com/docker/compose/releases/download/1.8.1/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
```
```
chmod +x /usr/local/bin/docker-compose
```

