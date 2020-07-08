package lumaceon.mods.aeonicraft.client.model;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.client.model.temporal_hourglass.ModelTemporalHourglass;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NullableProblems")
public class AeonicraftModelLoader implements ICustomModelLoader
{
    private IResourceManager manager;
    private final Map<ResourceLocation, OBJModel> cache = new HashMap<>();
    private final Map<ResourceLocation, Exception> errors = new HashMap<>();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.manager = resourceManager;
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        //Aeonicraft.logger.info(modelLocation.toString());
        //if(modelLocation.getResourcePath().endsWith("temporal_hourglass.obj") || modelLocation.getResourcePath().endsWith("temporal_hourglass"))
        //    return true;
        return modelLocation.getResourcePath().endsWith("temporal_hourglass");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception
    {
        ResourceLocation file = new ResourceLocation(modelLocation.getResourceDomain(), "models/item/obj/temporal_hourglass.obj");
        if (!cache.containsKey(file))
        {
            IResource resource = null;
            try
            {
                try
                {
                    resource = manager.getResource(file);
                }
                catch (FileNotFoundException e)
                {
                    if (modelLocation.getResourcePath().startsWith("models/block/"))
                        resource = manager.getResource(new ResourceLocation(file.getResourceDomain(), "models/item/" + file.getResourcePath().substring("models/block/".length())));
                    else if (modelLocation.getResourcePath().startsWith("models/item/"))
                        resource = manager.getResource(new ResourceLocation(file.getResourceDomain(), "models/block/" + file.getResourcePath().substring("models/item/".length())));
                    else throw e;
                }
                OBJModel.Parser parser = new OBJModel.Parser(resource, manager);
                OBJModel model = null;
                try
                {
                    model = parser.parse();
                    model = new ModelTemporalHourglass(model.getMatLib(), resource.getResourceLocation());
                }
                catch (Exception e)
                {
                    errors.put(modelLocation, e);
                }
                finally
                {
                    cache.put(modelLocation, model);
                }
            }
            finally
            {
                IOUtils.closeQuietly(resource);
            }
        }
        OBJModel model = cache.get(modelLocation);
        if (model == null) throw new ModelLoaderRegistry.LoaderException("Error loading model previously: " + file, errors.get(modelLocation));
        return model;
    }
}
