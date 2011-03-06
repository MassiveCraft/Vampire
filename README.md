Vampire - A vampire plugin for the bukkit minecraft server mod
====================
This plugin add the horror of vampirism to your mincraft bukkit survival multiplayer server.<br>
Any player may turn into a vampire if they contract the infection and doesn't manage (or doesn't care) to cure themselves in time.

Vampires...

 * Burn in sunlight.
 * Take no fall damage.
 * Can breath underwater... or rather does not need to breathe at all.
 * Has a reservoir of blood between 0 and 100.
 * Must drink blood from humans or animals to not die from starvation.
 * Can not eat normal food.
 * Regenerates health automatically. (costs blood)
 * Won't be attacked by monsters unless the vampire attacks first.
 * Can dash at high speed using a feather. (costs blood)
 * Deal more damage in close combat.
 * Take less damage in close combat exept for attacks from wooden weapons wich hurt them a lot.
 * Occatinally infects others in close combat.

Usage
---------
<b>Read the full userguide here: [http://mcteam.org/vampire](http://mcteam.org/vampire)</b>

The base chat console command is:

 * `/vampire`

That command can be used by vampires to display their current blood reservoir.
There are also the following subcomands for server operators (OP).

* `/vampire infect [playername] *[amount from 0 to 100]`<br>The player turns into vampire at infection 100. 0 means no infection.
* `/vampire turn [playername]`<br> Instantly turn a player into a vampire
* `/vampire cure [playername]`<br> Instantly cure a player from vampirism.
* `/vampire list`<br> List all vampires on the server.
* `/vampire load [config|players|all]`<br> Load data from disk. 
* `/vampire save [config|players|all]`<br> Save data to disk.
* `/vampire settime [ticks from 0 to 23999]`<br> Set the time. 0 means sunrise. 1 ingame hour is 1000 ticks.
* `/vampire version`<br> Find out which version you are running.


Installing
----------
1. Download the latest release: [https://github.com/oloflarsson/Vampire/tree/master/releases](https://github.com/oloflarsson/Vampire/tree/master/releases)<br>
1. Put Vampire.jar in the plugins folder.

A default config file will be created on the first run.

License
----------
This project has a LGPL license just like the Bukkit project.<br>
This project uses [GSON](http://code.google.com/p/google-gson/) which has a [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0 ).

