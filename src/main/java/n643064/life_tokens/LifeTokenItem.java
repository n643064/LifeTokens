package n643064.isv.item;

import n643064.isv.HealthState;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Objects;

import static n643064.isv.Config.CONFIG_INSTANCE;

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
        if (user.getMaxHealth() < CONFIG_INSTANCE.maxLife())
        {
            final HealthState state = HealthState.get(Objects.requireNonNull(user.getServer()));
            final String name = user.getEntityName();
            final int v;
            if (state.map.containsKey(name))
            {
                v = state.map.get(user.getEntityName()) + CONFIG_INSTANCE.lifeTokenIncrement();
            } else
            {
                v = CONFIG_INSTANCE.starterLife() + CONFIG_INSTANCE.lifeTokenIncrement();
            }
            state.map.put(name, v);
            state.markDirty();
            Objects.requireNonNull(user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(v);
            stack.decrement(1);
            user.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 0.6f);
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public boolean hasGlint(ItemStack stack)
    {
        return true;
    }
}
