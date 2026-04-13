# ビルド用ステージ

FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
# gradlewに実行権限を付与し、テストをスキップしてビルド
RUN chmod +x ./gradlew
RUN ./gradlew build -x test


# 実行用ステージ（本番環境）

FROM eclipse-temurin:17-jdk
WORKDIR /app
# ビルドしたJARファイル（完成品）だけをコピー
COPY --from=build /app/build/libs/*.jar app.jar

# Spring Bootを起動する命令
ENTRYPOINT ["java", "-jar", "app.jar"]