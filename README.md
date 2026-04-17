
## コンテナ上でビルドを実行

1. クローンしたフォルダをvscodeで開き、ターミナルで以下のコマンドを叩き、Javaコンテナを起動します。
```
docker-compose up -d
```

2. コンテナを作成・起動したら、javaコンテナ内に入ります。
```
docker compose exec app bash
```
※ターミナルに以下のような文字列が表示されれば成功です。

`root@569495f96ff8:/app#`

3. ビルドを実行する
```
./gradlew build --continuous
```

## 別のターミナルから再度コンテナ内に入りSpringを起動

1. コンテナ内に入る
```
docker compose exec app bash
```

2. Springを起動
```
./gradlew bootRun
```

## VSCodeのデバッグモードで起動

最後にVSCodeのデバッグモードで起動します。

![スクリーンショット 2024-04-22 23.17.43.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/3744277/7b7d9cc9-9cf6-294f-d87f-2768b143b445.png)


この状態で▶️（開始ボタン）を押すことで、デバッグモードが起動されます。

うまく起動したら、下記のURLにアクセスしてください。

http://localhost:8080/


下記画像のように「Whitelabel Error Page」が表示されていれば、環境構築完了です。
![スクリーンショット 2024-04-22 23.13.29.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/3744277/29986c70-eaf3-31b1-6b4d-6ae98390f38f.png)

VSC右上の停止ボタン(赤い四角)を押す
./gradlew bootRunを実行したターミナルで Ctrl + C
ファイルの変更を保存

再度
./gradlew bootRun