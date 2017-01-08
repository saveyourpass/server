# Development

## Requirements

You need to have

  - Wildfly 10
  - Postgresql 9.x

running.

Database `saveyourpass` with owner `saveyourpass` should exists.

If all requirements are met then you just need to run

```
mvn wildfly:deploy
```

to run saveyourpass server. The API is available under `localhost:8080/saveyourpass/api`,
for example `localhost:8080/saveyourpass/user`.
