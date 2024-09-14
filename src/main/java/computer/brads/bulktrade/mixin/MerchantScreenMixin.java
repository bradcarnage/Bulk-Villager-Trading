package computer.brads.bulktrade.mixin;

import computer.brads.bulktrade.BulkTrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;

@Mixin(MerchantScreen.class)
public class MerchantScreenMixin {
    @Shadow private int shopItem;
    @Inject(method = "postButtonClick", at = @At("TAIL"))
    private void onPostButtonClick(CallbackInfo ci) {
        BulkTrade.selectedVillagerSlot = this.shopItem;
    }
//    @Inject(method = "mouseClicked", at = @At("TAIL"))
//    private void onMouseClicked(double p_99131_, double p_99132_, int p_99133_, CallbackInfoReturnable<Boolean> cir) {
//        BulkTrade.attemptRestock();
//    }
    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        BulkTrade.isVillagerScreenOpen = true;
        BulkTrade.selectedVillagerSlot = 0;
        BulkTrade.triggerRestock = false;
    }
}