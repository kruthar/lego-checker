# lego-checker-cli
A small project to check for a specific hot lego set and notify when it's available.

## Build
To build the cli simply run:

```
mvn clean install
```

This builds a runnable cli application at `target/lego-cli/bin/lego-cli`

## Common Config
Any commands or options that require some configuration will look for that configuration in a `lego-cli.properties` file where you run the CLI from.

See commands and options for specific configuration required.

## Commands
### Check
The check command checks a lego sets availability and notifies you via the configured notifier.

A simple example:
```
lego-cli check -s lego-titanic-10294 -n alexa
```

#### Flags
| short | long | description |
| ----- | ---- | ----------- |
| `-s` | `--set-name` | Specifies the Lego set name as shown in the Lego.com URL |
| `-n- | `--notifier` | Specifies how to notify you when the set is available. Choices: [Alexa] |

#### Alexa Notifications
This CLI uses the Notify Me Alexa skill. This is a free service and can be setup with these instructions: https://www.thomptronics.com/about/notify-me.

When you have your API key place it in your lego-cli.properties file:
```
notifiers.alexa.accessCode=amzn1.ask.account.XXXXXXXXXXXXXXXXXX
```