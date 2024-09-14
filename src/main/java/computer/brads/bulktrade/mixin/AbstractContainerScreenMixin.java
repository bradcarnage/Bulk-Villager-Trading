package computer.brads.bulktrade.mixin;

import computer.brads.bulktrade.BulkTrade;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    @Inject(method = "onClose", at = @At("TAIL"))
    private void onClose(CallbackInfo ci) {
        if (BulkTrade.isVillagerScreenOpen) {
            BulkTrade.isVillagerScreenOpen = false;
        }
    }
}
