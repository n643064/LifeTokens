This simple mod adds consumable tokens, which can be used to increase a player's max health, obtainable through looting, breaking spawners and archaeology. The mechanics of life tokens and their addition to loot tables can be configured in the **life_tokens.json** file located in the config directory/folder.

```
// Default configuration
{
  "addLifeTokenBitsToSpawnerLoot": true,
  "addLifeTokenBitsToDungeonLoot": true,
  "addLifeTokenBitsToArchaeologyLoot": true,
  "maxLife": 60,
  "starterLife": 20,
  "lifeIncrement": 1,
  "resetOnDeath": false,
  "lifeLostOnDeath": 0
}
```

if ```addLifeTokenBitsToSpawnerLoot``` is set to true, spawners will drop 1-3 Life Token Bits when broken,
```addLifeTokenBitsToDungeonLoot``` and ```addLifeTokenBitsToArchaeologyLoot``` add Bits to dungeon chest and archaeology loot tables, respectively.  
The modified chest loot tables are:
- abandoned mineshaft (minecart chests)
- simple dungeon chests (monster spawner box)
- buried treasure
- end city treasure 
- desert pyramid chests
- jungle temple chests
- woodland mansion chests

[Downloads](https://modrinth.com/mod/life-tokens)