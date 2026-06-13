# Estágio 1: Build isolado do código
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compila o projeto ignorando os testes para agilizar o processo
RUN mvn clean package -DskipTests

# Estágio 2: Ambiente de Runtime Leve (Cloud-Native)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia apenas o artefato final do estágio anterior
COPY --from=builder /app/target/*.jar app.jar

# Expondo a porta dinamicamente e iniciando a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]