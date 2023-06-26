package n643064.life_tokens;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.Objects;

import static n643064.life_tokens.Config.CONFIG;

public class LifeTokens implements ModInitializer
{
    public static final String MODID = "life_tokens";

    public static final Item LIFE_TOKEN_BIT = new Item(new FabricItemSettings().maxCount(64).rarity(Rarity.UNCOMMON));
    public static final Item LIFE_TOKEN_SHARD = new Item(new FabricItemSettings().maxCount(64).rarity(Rarity.RARE));
    public static final LifeTokenItem LIFE_TOKEN = new LifeTokenItem(new FabricItemSettings().maxCount(64).rarity(Rarity.EPIC));

    @Override
    public void onInitialize()
    {
        final Identifier SPAWNER_LOOT = Blocks.SPAWNER.getLootTableId();
        final List<Identifier> DUNGEON_LOOT = List.of(
                LootTables.ABANDONED_MINESHAFT_CHEST,
                LootTables.SIMPLE_DUNGEON_CHEST,
                LootTables.BURIED_TREASURE_CHEST,
                LootTables.END_CITY_TREASURE_CHEST,
                LootTables.DESERT_PYRAMID_CHEST,
                LootTables.JUNGLE_TEMPLE_CHEST,
                LootTables.WOODLAND_MANSION_CHEST
        );
        final List<Identifier> ARCHAEOLOGY_LOOT = List.of(
                LootTables.DESERT_PYRAMID_ARCHAEOLOGY,
                LootTables.DESERT_WELL_ARCHAEOLOGY,
                LootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY,
                LootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY,
                LootTables.TRAIL_RUINS_COMMON_ARCHAEOLOGY,
                LootTables.TRAIL_RUINS_RARE_ARCHAEOLOGY
        );

        Config.setup();

        Registry.register(Registries.ITEM, new Identifier(MODID, "life_token_bit"), LIFE_TOKEN_BIT);
        Registry.register(Registries.ITEM, new Identifier(MODID, "life_token_shard"), LIFE_TOKEN_SHARD);
        Registry.register(Registries.ITEM, new Identifier(MODID, "life_token"), LIFE_TOKEN);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries ->
        {
            entries.add(new ItemStack(LIFE_TOKEN_BIT));
            entries.add(new ItemStack(LIFE_TOKEN_SHARD));
            entries.add(new ItemStack(LIFE_TOKEN));
        });

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) ->
        {
            if (CONFIG.addLifeTokenBitsToSpawnerLoot() && SPAWNER_LOOT.equals(id))
            {
                LootPool.Builder builder = new LootPool.Builder().rolls(ConstantLootNumberProvider.create(1f))
                        .with(ItemEntry.builder(LIFE_TOKEN_BIT).weight(1)).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f, 3f)));
                tableBuilder.pool(builder);
            } else if (CONFIG.addLifeTokenBitsToDungeonLoot() && DUNGEON_LOOT.contains(id))
            {
                LootPool.Builder builder = new LootPool.Builder().rolls(ConstantLootNumberProvider.create(1f))
                        .with(ItemEntry.builder(LIFE_TOKEN_BIT).weight(1));
                tableBuilder.pool(builder);
            } else if (CONFIG.addLifeTokenBitsToArchaeologyLoot() && ARCHAEOLOGY_LOOT.contains(id))
            {
                tableBuilder.modifyPools((builder) -> builder.with(ItemEntry.builder(LIFE_TOKEN_BIT)));
            }
        });


        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) ->
        {
            HealthState state = HealthState.get(newPlayer.server);
            String s = newPlayer.getEntityName();
            if (!state.map.containsKey(s))
            {
                state.map.put(s, CONFIG.starterLife());
                state.markDirty();
                Objects.requireNonNull(newPlayer.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(CONFIG.starterLife());
                newPlayer.setHealth(CONFIG.starterLife());
            } else
            {
                int v;
                if (CONFIG.resetOnDeath())
                {
                    v = CONFIG.starterLife();
                    state.map.put(s, v);
                } else
                {
                     v = state.map.get(s);
                }
                Objects.requireNonNull(newPlayer.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(v);
                newPlayer.setHealth(v);
            }
        });
    }
}
