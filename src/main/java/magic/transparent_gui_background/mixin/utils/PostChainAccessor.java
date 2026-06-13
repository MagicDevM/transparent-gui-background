package magic.transparent_gui_background.mixin.utils;

import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PostChain.class)
public interface PostChainAccessor {
  @Invoker("load")
  void invokeLoad(TextureManager texManager, ResourceLocation shader);
}