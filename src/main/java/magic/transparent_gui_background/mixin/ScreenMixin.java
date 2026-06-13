package magic.transparent_gui_background.mixin;

import magic.transparent_gui_background.gui.api.GameRendererExtended;
import magic.transparent_gui_background.gui.api.PanoramaRendererExtended;

import net.minecraft.client.gui.screens.Screen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
  @Shadow
  protected Minecraft minecraft;
  @Shadow
  public int width;
  @Shadow
  public int height;
  
  // Generate CubeMap for panorama
  @Unique
  private static final CubeMap cubeMap = new CubeMap(new ResourceLocation("textures/gui/title/background/panorama"));
  @Unique
  private static PanoramaRenderer panoramaRenderer;
  
  // get the blur shader
  @Unique
  private static final ResourceLocation blurShader = new ResourceLocation("transparent-gui-background").tryBuild("transparent-gui-background", "blur");
  
  // Get necessary menu texture overlays
  @Unique
  private static final ResourceLocation MENU_BACKGROUND = new ResourceLocation("transparent-gui-background").tryBuild("transparent-gui-background", "textures/gui/menu_background.png");
  @Unique
  private static final ResourceLocation INWORLD_MENU_BACKGROUND = new ResourceLocation("transparent-gui-background").tryBuild("transparent_gui_background", "textures/gui/inworld_menu_background.png");
  
  // Create a new panoroma renderer instance
  @Inject(method = "<clinit>", at = @At("HEAD"))
  private static void onStatic(CallbackInfo ci) {
    panoramaRenderer = new PanoramaRenderer(cubeMap);
  }
  
  @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
  private void renderBackground(GuiGraphics graphics, CallbackInfo ci) {
    // Get current delta value
    float delta = minecraft.getFrameTime();

    // Check if player is in a world
    if (this.minecraft.level == null) {
      // load transparent panoroma
      this.renderPanorama(graphics, delta);
    }

    // render the blurred panoroma
    this.renderBlurredBackground(delta);
    this.renderMenuBackground(graphics);
    
    // Cancel the entire method
    ci.cancel();
  }
  
  @Unique
  protected void renderPanorama(GuiGraphics graphics, float delta) {
    // render panoroma through our implementation
    ((PanoramaRendererExtended) panoramaRenderer).TGB$renderPanorama(graphics, this.width, this.height, delta, 0.0F);
  }

  @Unique
  public void renderBlurredBackground(float delta) {
    // render blurred panoroma through our implementation
    ((GameRendererExtended) this.minecraft.gameRenderer).TGB$renderBlur(5.0F, delta);
  }
  
  @Unique
  protected void renderMenuBackground(final GuiGraphics graphics) {
    // render the panoroma overlay
    this.renderMenuBackground(graphics, 0, 0, this.width, this.height);
  }

  @Unique
  protected void renderMenuBackground(final GuiGraphics graphics, final int x, final int y, final int width, final int height) {
    // Verify if play is in a minecraft world or gui
    renderMenuBackgroundTexture(graphics, this.minecraft.level == null ? MENU_BACKGROUND : INWORLD_MENU_BACKGROUND, x, y, 0.0F, 0.0F, width, height);
  }
  
  @Unique
  private static void renderMenuBackgroundTexture(final GuiGraphics graphics, final ResourceLocation menuBackground, final int x, final int y, final float u, final float v, final int width, final int height) {
    int size = 32;

    // Render panoroma overlay
    RenderSystem.enableBlend();
    graphics.blit(menuBackground, x, y, u, v, width, height, size, size);
    RenderSystem.disableBlend();
  }
}