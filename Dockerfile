# 1º estágio
# chamado `build-image`, imagem JDK temporária para compilar o código
FROM maven:3-openjdk-17 as build-image
# define o diretório de trabalho
WORKDIR /to-build-app
# copia o código-fonte para o diretório de trabalho
COPY pom.xml .
RUN mvn dependency:go-offline
# executa o comando de empacotamento do maven
RUN ./mvnw clean package
# 2º estágio
# com imagem JRE, limpa e mais leve
FROM eclipse-temurin:17-jre-alpine
# define o diretório de trabalho
WORKDIR /app
# copia o jar gerado no primeiro estágio para o diretório de trabalho
COPY --from=build-image /to-build-app/target/*.jar ./target/
# expõe a porta 8080
EXPOSE 8080
# define o comando de execução da aplicação
ENTRYPOINT ["/bin/sh", "-c", "java -jar target/agrix*.jar"]
