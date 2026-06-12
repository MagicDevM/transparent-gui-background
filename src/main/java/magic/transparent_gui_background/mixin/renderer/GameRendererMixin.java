package magic.transparent_gui_background.mixin.renderer;

import magic.transparent_gui_background.gui.api.GameRendererExtended;
import magic.transparent_gui_background.gui.api.PostChainExtended;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.String;
import java.util.List;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements GameRendererExtended {
  // get post effects
  @Shadow
  PostChain postEffect;
  
  @Unique
  @Override
  public void TGB$renderBlur(float radius, float delta) {
    // verify that the radius is more than or isequal to 1.0F
    if (radius >= 1.0F) {
      // Apply blur effect
      ((PostChainExtended) this.postEffect).TGB$setUniform("Radius", radius);
      this.postEffect.process(delta);
    }
  }
}