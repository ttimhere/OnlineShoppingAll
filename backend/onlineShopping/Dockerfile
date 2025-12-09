# 使用 JDK 17 轻量运行环境（与你项目匹配）
FROM eclipse-temurin:17-jre

# 作者信息
MAINTAINER zst

# 设置容器内的工作目录（随便取，不影响运行）
WORKDIR /usr/local/java

# 设置时区为上海
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 暴露端口（你的项目端口是 8080）
EXPOSE 8080

# 定义 jar 包名称（与你本地 jar 完全一致）
ARG JAR_FILE=onlineShopping-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar

# 复制外部配置（如果你需要）
ADD application.properties /usr/local/java/application.properties
# 用 sh -c 执行带重定向的命令
ENTRYPOINT ["java","-jar","/usr/local/java/app.jar"]