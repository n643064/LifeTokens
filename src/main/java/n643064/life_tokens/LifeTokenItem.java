package n643064.life_tokens;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Objects;

import static n643064.life_tokens.Config.CONFIG;


public class LifeTokenItem extends Item
{
    public LifeTokenItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        final ItemStack stack = user.getStackInHand(hand);
        if (world.isClient)
        {
            return TypedActionResult.fail(stack);
        }
        if (user.getMaxHealth() < CONFIG.maxLife())
        {
            final HealthState state = HealthState.get(Objects.requireNonNull(user.getServer()));
            final String name = user.getEntityName();
            final int v;
            if (state.map.containsKey(name))
            {
                v = state.map.get(user.getEntityName()) + CONFIG.lifeIncrement();
            } else
            {
                v = CONFIG.starterLife() + CONFIG.lifeIncrement();
            }
            state.map.put(name, v);
            state.markDirty();
            Objects.requireNonNull(user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(v);
            stack.decrement(1);
            user.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.8f, 2f);
            return TypedActionResult.success(stack);
        }
        user.sendMessage(Text.translatable("life_tokens.limit_reached").formatted(Formatting.DARK_RED), true);
        return TypedActionResult.fail(stack);
    }

    @Override
    public boolean hasGlint(ItemStack stack)
    {
        return true;
    }
}
