package hybridworld.world;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.CharBuffer;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import static hybridworld.HybridWorldMod.*;

public class HybridWorldSettings {
	public static final String NAME = "hybrid_settings.json";
	public String worldTypeVanilla = "Customized";
	
    public static File getPresetFolder(ISaveHandler saveHandler) {
        return new File(saveHandler.getWorldDirectory(),
                "/data/" + MODID + "/");
    }

    public static File getPresetFile(ISaveHandler saveHandler) {
        return new File(getPresetFolder(saveHandler),NAME);
    }

	public static HybridWorldSettings defaults() {
		return new HybridWorldSettings();
	}

    
    public static HybridWorldSettings load(World world) {
    	if(world.isRemote)
    		throw new IllegalStateException("Settings shall not be loaded from client worlds.");
    	String jsonString = loadJsonStringFromSaveFolder(world.getSaveHandler());
    	if(jsonString==null) {
    		return defaults();
    	}
    	Gson gson = gson();
    	return gson.fromJson(jsonString, HybridWorldSettings.class);
    }
    
    @Nullable
    public static String loadJsonStringFromSaveFolder(ISaveHandler saveHandler) {
        File externalGeneratorPresetFile = getPresetFile(saveHandler);
        if (externalGeneratorPresetFile.exists()) {
            try (FileReader reader = new FileReader(externalGeneratorPresetFile)){
                CharBuffer sb = CharBuffer.allocate((int) externalGeneratorPresetFile.length());
                reader.read(sb);
                sb.flip();
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static Gson gson() {
        return gsonBuilder().create();
    }

    public static GsonBuilder gsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.serializeSpecialFloatingPointValues();
        builder.enableComplexMapKeySerialization();
        builder.registerTypeAdapter(HybridWorldSettings.class, new Serializer());
        return builder;
    }
    
    private static class Serializer implements JsonDeserializer<HybridWorldSettings>, JsonSerializer<HybridWorldSettings> {

        private static final Field[] fields = HybridWorldSettings.class.getFields();

        static {
            for (Field f : fields) {
                f.setAccessible(true);
            }
        }

        private static final HybridWorldSettings defaults = HybridWorldSettings.defaults();

        @Override public HybridWorldSettings deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return deserializeWithDefault(json, defaults, context);
        }

        private HybridWorldSettings deserializeWithDefault(JsonElement element, HybridWorldSettings def, JsonDeserializationContext ctx) {
            try {
                JsonObject root = element.getAsJsonObject();
                HybridWorldSettings ret = new HybridWorldSettings();

                for (Field field : fields) {
                        if (root.has(field.getName())) {
                            JsonElement e = root.get(field.getName());
                            field.set(ret, ctx.deserialize(e, field.getGenericType()));
                        } else {
                            field.set(ret, field.get(def));
                        }
                }
                return ret;
            } catch (IllegalAccessException e) {
                throw new Error(e);
            } catch (RuntimeException e) {
                throw new JsonParseException(e);
            }
        }

        @Override public JsonElement serialize(HybridWorldSettings src, Type typeOfSrc, JsonSerializationContext context) {
            return serializeWithDefault(src, defaults, context);
        }

        private JsonElement serializeWithDefault(HybridWorldSettings src, HybridWorldSettings def, JsonSerializationContext ctx) {
            try {
                JsonObject root = new JsonObject();
                for (Field field : fields) {
                    Object value = field.get(src);
                      root.add(field.getName(), ctx.serialize(value));
                }
                return root;
            } catch (IllegalAccessException e) {
                throw new Error(e);
            }
        }
    }
}
