# 後端 Dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

# 複製 Maven 配置文件
COPY pom.xml ./

# 複製源碼
COPY src ./src

# 安裝 Maven 並建置
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# 暴露端口
EXPOSE 8080

# 啟動應用
CMD ["java", "-jar", "target/portfolio-api-0.0.1-SNAPSHOT.jar"] 