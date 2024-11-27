# Fase de construção
FROM maven:3.8.4-openjdk-17 AS build

# Diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração
COPY pom.xml .
COPY src ./src

# Baixar dependências e compilar o projeto
RUN mvn clean package -DskipTests

# Fase de execução
FROM openjdk:17-jdk-alpine

# Diretório de trabalho
WORKDIR /app

# Copiar o JAR construído da fase anterior
COPY --from=build /app/target/*.jar app.jar

# Expor a porta em que a aplicação vai rodar
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java","-jar","/app/app.jar"]