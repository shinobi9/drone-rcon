# a rcon plugin for drone

just test in minecraft , can't handle `Multiple-packet Responses` now , may be implement in future

```shell script
docker run -e PLUGIN_HOST=127.0.0.1        \
           -e PLUGIN_PORT=25575            \
           -e PLUGIN_PASSWORD=<password>     \
           -e PLUGIN_TIMEOUT=60000         \
           -e PLUGIN_COMMANDS="say hello,say hi"   9rubi/krcon:<tag>
```