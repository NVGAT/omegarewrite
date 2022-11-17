# The Omega SMP plugin
## Use scenarios
---

The Omega SMP plugin is used in (obviously) the Omega SMP, and is publicly available for everyone to use.


## What does it do?
---
The Omega SMP plugin forces a five-lives system for every player on the server. Each player starts out with five lives, which they lose upon dying to a player. When they lose all of their lives they get banned, leaving behind an OP item and a resurrection shard that can be used in conjunction with four diamond blocks and four totems of undying to create the Revival item, used to revive any dead player, unbanning them from the server.

## Why should I use this over other plugins?

---
This plugin is completely original and never seen before on any other SMPs. It's maintained by an experienced Spigot plugin developer that always strives to bring the best quality products to everyone, all while keeping it open-source. It definitely sounds too good to be true, but it's not! You can download the plugin right now, you can look at the source code right now and you can edit the source right now!

## Licensing
---
To use this plugin, you need to comply with the Creative Commons BY-NC-SA 4.0 license that this project is licensed with. You have the right to share adaptations of this project and modify it however you'd like, the only things you can't do is make money off of it and use different licenses for adaptations of this project.\
 To be completely clear, here's some random text that I copied off of a license generator :)
### Omega SMP plugin © 2022 by NotVeryGoodAtThis is licensed under CC BY-NC-SA 4.0

## You've got my attention. How do I use this?
---
### Important note: This plugin's native Minecraft server software is Spigot for 1.19.2. Everything else, such as other server softwares (PaperMC, PurpurMC, MagmaMC) and other Minecraft versions (everything before and after 1.19.2) might work, but you should be aware that you're using the product AS-IS. I will not be held responsible for any damage done to your server caused by improper handling of the plugin.
### It has ONLY been tested on Spigot 1.19.2 and PaperMC 1.19.2.
---
    
Great! Now that you've definitely read the important note, let's proceed. The first step should be to install your desired server software. For me that's going to be 1.19.2. We download the server.jar from [PaperMC's official website](https://papermc.io/downloads), run it with Java 17, agree to the EULA and restart the server. Once the server is up, we drop the plugin jar file into the plugins directory and enter the ```reload confirm``` command into the server console and then ```stop```.\
### Important: If you're going to be using an anti combat-logging plugin, I highly recommend PVPManager because the Omega SMP plugin can work with it. If you use any other ant combat-log plugins, you will probably face issues when players get banned. You have been warned. \
 Now we should edit the config files. This is the config that is used in the Omega SMP:
```
reward-mats:
  - NETHERITE_CHESTPLATE
  - NETHERITE_LEGGINGS
  - NETHERITE_HELMET
  - NETHERITE_BOOTS
  - NETHERITE_SWORD
  - NETHERITE_AXE
  - ENCHANTED_GOLDEN_APPLE
spawn-cords: [0.0, 76.0, -10.0]
punish-on-spawn-kill: true
```
After all of this, you can finally start the server and enjoy the plugin. Make sure to leave me a note if something's wrong.

---

## I want to contact you, how do I go about that?
First of all, thank you for making me feel important to another human being instead of just coding in the dark all day :)\
 Second of all, if you found a bug in the plugin you should create an issue on this GitHub page. I check my GitHub a lot, so I should see it. After I respond it might take a bit to roll out another update. That's solely because I'm a 15 year old hobbyist, not a full-time developer. Sorry about that, but I do have a life outside of this plugin and I'd kindly ask you to respect that.\
 If you want a closer, more personal type of connection due to your parasocial relationship, you can talk to me in [my discord server.](https://dsc.gg/nvgat) \
 That's all from me, hope to see you soon!