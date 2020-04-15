# senior-tempmute

This is a test plugin made for joining Senior Team.
The requirements were listed here https://pastebin.com/g1Pm2vY4.

## How the plugin works

There are two simple commands in the plugin, /tempmute and /unmute.
When a player gets muted, a record is inserted into the MySQL asynchronously, as well as an user object gets cached for faster access.
The MySQL record gets removed when the player leaves the server unmuted or gets unmuted by the command.
