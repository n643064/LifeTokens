package n643064.isv;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public record Config(
        boolean modifySpawnerLootTable,
        boolean addLifeTokenBitsToDungeonLoot,
        boolean addLifeTokenBitsToArchaeologyLoot,
        boolean generateWildCrops,
        boolean everythingAlwaysEdible,
        int maxLife,
        int starterLife,
        int lifeTokenIncrement,
        int bandageDuration,
        int bandageUseTime,
        int bandageAmplifier,
        int bandageCooldown
)
{
    public static Config CONFIG_INSTANCE = new Config(
            true,
            true,
            true,
            true,
            true,
            60,
            20,
            1,
            120,
            60,
            0,
            40
    );
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();
    static final String CONFIG_PATH = "config" + File.separator + "isv.json";

    public static void create() throws IOException
    {
        Path p = Path.of("config");
        if (Files.exists(p))
        {
            if (Files.isDirectory(p))
            {
                FileWriter writer = new FileWriter(CONFIG_PATH);
                writer.write(GSON.toJson(CONFIG_INSTANCE));
                writer.flush();
                writer.close();
            }
        } else
        {
            Files.createDirectory(p);
            create();
        }
    }

    public static void read() throws IOException
    {
        FileReader reader = new FileReader(CONFIG_PATH);
        CONFIG_INSTANCE = GSON.fromJson(reader, Config.class);
        reader.close();
    }

    public static void setup()
    {
        try
        {
            if (Files.exists(Path.of(CONFIG_PATH)))
            {
                read();
            } else
            {
                create();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
