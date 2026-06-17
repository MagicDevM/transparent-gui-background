package magic.transparent_gui_background.mixin.utils;

import magic.transparent_gui_background.gui.api.PostChainExtended;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.String;
import java.util.List;

@Mixin(PostChain.class)
public class PostChainMixin implements PostChainExtended {¬
  // get all passes
  @Shadow
  @Final
  private List<PostPass> passes;

  @Unique
  @Override
  public void TGB$setUniform(String type, float value) {
    // Just for null safety
    if (passes == null) return;

    // iterate over all passes
    for(PostPass pass : this.passes) {
      // apply blur effect through safe uniform
      pass.getEffect().safeGetUniform(type).set(value);
    }
  }
}