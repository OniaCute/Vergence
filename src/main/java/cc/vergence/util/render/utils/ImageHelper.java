package cc.vergence.util.render.utils;


import cc.vergence.util.render.other.SkiaContext;
import io.github.humbleui.skija.ColorType;
import io.github.humbleui.skija.Data;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.SurfaceOrigin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ImageHelper {

    private Map<String, Image> images = new HashMap<>();
    private Map<Integer, Image> textures = new HashMap<>();

    public boolean load(int texture, float width, float height, SurfaceOrigin origin) {

        if (!textures.containsKey(texture)) {
            Image image = Image.adoptGLTextureFrom(SkiaContext.getContext(), texture, GL11.GL_TEXTURE_2D, (int) width,
                    (int) height, GL11.GL_RGBA8, origin, ColorType.RGBA_8888);
            textures.put(texture, image);
        }

        return true;
    }

    public boolean load(Identifier identifier) {

        if (!images.containsKey(identifier.getPath())) {
            ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
            Resource resource;
            try {
                resource = resourceManager.getResourceOrThrow(identifier);
                try (InputStream inputStream = resource.getInputStream()) {

                    byte[] imageData = inputStream.readAllBytes();
                    Image image = Image.makeDeferredFromEncodedBytes(imageData);
                    if (image == null) {
                        return false;
                    }
                    images.put(identifier.getPath(), image);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
        return true;
    }

    public boolean load(String filePath) {
        if (!images.containsKey(filePath)) {
            Optional<byte[]> encodedBytes = convertToBytes(filePath);
            if (encodedBytes.isPresent()) {
                images.put(filePath, Image.makeDeferredFromEncodedBytes(encodedBytes.get()));
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean load(File file) {

        if (!images.containsKey(file.getName())) {

            try {
                byte[] encoded = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
                images.put(file.getName(), Image.makeDeferredFromEncodedBytes(encoded));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public Image get(String path) {

        if (images.containsKey(path)) {
            return images.get(path);
        }

        return null;
    }

    public Image get(int texture) {

        if (textures.containsKey(texture)) {
            return textures.get(texture);
        }

        return null;
    }

    public static Optional<byte[]> convertToBytes(String path) {
        try (InputStream inputStream = getResourceAsStream(path)) {
            return Optional.of(inputStream.readAllBytes());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static Optional<Data> convertToData(String path) {
        return convertToBytes(path).map(Data::makeFromBytes);
    }

    public static InputStream getResourceAsStream(String path) {
        InputStream inputStream = ImageHelper.class.getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
        return inputStream;
    }
}
