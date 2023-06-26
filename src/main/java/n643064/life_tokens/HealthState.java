package n643064.isv;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.HashMap;

import static n643064.isv.ImprovedSurvival.MODID;

public class HealthState extends PersistentState
{

    public static final String ID = MODID + " HealthState";

    public final HashMap<String, Integer> map;
    public HealthState()
    {
         map = new HashMap<>();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt)
    {
        for (String s : map.keySet())
        {
            nbt.putInt(s, map.get(s));
        }
        return nbt;
    }

    public static HealthState fromNbt(NbtCompound nbt)
    {
        HealthState h = new HealthState();
        for (String s : nbt.getKeys())
        {
            h.map.put(s, nbt.getInt(s));
        }
        return h;
    }

    public static HealthState get(MinecraftServer server)
    {
        PersistentStateManager manager = server.getOverworld().getPersistentStateManager();
        return manager.getOrCreate(HealthState::fromNbt, HealthState::new, ID);
    }

}
