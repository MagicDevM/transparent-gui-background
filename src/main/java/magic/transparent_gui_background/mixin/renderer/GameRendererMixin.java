package magic.transparent_gui_background.mixin.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import magic.transparent_gui_background.gui.api.GameRendererExtended;
import magic.transparent_gui_background.gui.api.PostChainExtended;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.String;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements GameRendererExtended {
  // Get the blur shader json file
  @Unique
  private static final ResourceLocation blurShader = new ResourceLocation("transparent-gui-background").tryBuild("transparent-gui-background", "post_effect/blur.json");
  
  // Get some essential variables
  @Shadow
  @Final
  static Logger LOGGER;
  @Shadow
  @Final
  Minecraft minecraft;
  
  // get post effects
  @Unique
  private PostChain blurEffect;
  
  @Unique
  @Override
  public void TGB$renderBlur(float radius, float delta) {
    // verify that the radius is more than or isequal to 1.0F
    if (radius >= 1.0F) {
      // Apply blur effect
      ((PostChainExtended) this.blurEffect).TGB$setUniform("Radius", radius);
      // run our initialized blur shader
      this.blurEffect.process(delta);
    }
  }
  
  @Unique
  private void loadBlurEffect() {
    // check if it is already initialized
    if (this.blurEffect != null) {
      // reinitialize it
      this.blurEffect.close();
    }

    try {
      // create an new PostChain pass
      this.blurEffect = new PostChain(this.minecraft.getTextureManager(), this.minecraft.getResourceManager(), this.minecraft.getMainRenderTarget(), blurShader);
      // Add our shader into the pass
      this.blurEffect.load(this.minecraft.getTextureManager(), blurShader);
    } catch (IOException e) {
      // Catch Weird exceptions
      LOGGER.warn("Failed to load shader: {}", blurShader, e);
    } catch (JsonSyntaxException e) {
      // Catch shader file syntax errors
      LOGGER.warn("Failed to parse shader: {}", blurShader, e);
    }
  }

  // Add our blur method in the reload section so it loads/reloads with other shaders
  // This fires on mojang reloading/loading screen
  @Inject(method = "reloadShaders", at =
  @At(
    value = "INVOKE",
    target = "Ljava/util/List;add(ILjava/util/E;)V",
    // insert right after the last method
    ordinal = 58,
    shift = At.Shift.AFTER
  ))
  private void insertBlur(CallbackInfo ci) {
    this.loadBlurEffect();
  }

  // Correctly close our created object so it doesnt become an strangling constructor
  @Inject(method = "close()V", at = @At("TAIL"))
  public void afterClose(CallbackInfo ci) {
    if (blurEffect != null) {
      blurEffect.close();
    }
  }
}