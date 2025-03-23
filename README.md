# Chess HTMX

## Setup & Run locally

```bash
cd docker
docker compose up -d
```

```bash
cd ..
mvn spring-boot:run
```

## Deploy to server

```bash
mvn clean package
```

```bash
scp -i $env:CHESS_HTMX_KEY_FILE target/chess-htmx-0.0.1-SNAPSHOT.jar ubuntu@chess-htmx.leonklute.nl:workspace/chess-htmx.jar
```

```bash
ssh -i $env:CHESS_HTMX_KEY_FILE ubuntu@chess-htmx.leonklute.nl
```

```bash
sudo systemctl restart chess-htmx
```

```bash
sudo systemctl status chess-htmx
```


