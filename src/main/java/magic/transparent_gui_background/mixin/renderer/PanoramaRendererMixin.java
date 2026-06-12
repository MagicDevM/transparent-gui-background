package magic.transparent_gui_background.mixin.renderer;

import magic.transparent_gui_background.gui.api.PanoramaRendererExtended;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CubeMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(net.minecraft.client.renderer.PanoramaRenderer.class)
public class PanoramaRendererMixin implements PanoramaRendererExtended {
  // Get the Panorama overlay asset
  @Unique
  public static final ResourceLocation PANORAMA_OVERLAY = new ResourceLocation("textures/gui/panorama_overlay.png");
  
  @Shadow
  @Final
  private Minecraft minecraft;
  @Shadow
  @Final
  private CubeMap cubeMap;
  @Shadow
  private float spin;
  @Shadow
  private float bob;
  
  // Shadow & get the wrap function
  // NOTE: An dummy body is provided because its an private method this is overriden later by mixin
  @Shadow
  private float wrap(float a, float b) {
    throw new AssertionError();
  }
  
  @Unique
  @Override
  public void TGB$renderPanorama(GuiGraphics graphics, int width, int height, float alpha, float delta) {
    // Get panorama speed
    float panoramaSpeed = (float)((double)alpha * (Double)this.minecraft.options.panoramaSpeed().get());
    
    // Calculate & Set required values
    this.spin = wrap(this.spin + panoramaSpeed * 0.1F, 360.0F);
    this.bob = wrap(this.bob + panoramaSpeed * 0.001F, ((float)Math.PI * 2F));
    
    // render the panorama
    this.cubeMap.render(this.minecraft, 10.0F, -this.spin, delta);
    
    // Render the panorama overlay
    RenderSystem.enableBlend();
    
    graphics.setColor(1.0F, 1.0F, 1.0F, alpha);
    graphics.blit(PANORAMA_OVERLAY, 0, 0, width, height, 0.0F, 0.0F, 16, 128, 16, 128);
    graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    
    RenderSystem.disableBlend();
  }
}