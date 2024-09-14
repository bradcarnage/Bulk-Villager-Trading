package computer.brads.bulktrade.mixin;

import computer.brads.bulktrade.BulkTrade;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.TimeUnit;

@Mixin(MerchantMenu.class)
public abstract class MerchantMenuMixin {
    @Shadow public abstract void tryMoveItems(int p_40073_);
    @Shadow public abstract void setSelectionHint(int p_40064_);
    @Shadow public abstract MerchantOffers getOffers();

    @Shadow protected abstract void playTradeSound();

    @Shadow @Final private Merchant trader;

    @Inject(method = "playTradeSound", at = @At("TAIL"))
    private void onPlayTradeSound(CallbackInfo ci) {
        BulkTrade.LOGGER.info("MerchantMenu:playTradeSound");
        if (BulkTrade.isVillagerScreenOpen && !BulkTrade.triggerRestock) {
            BulkTrade.triggerRestock = true;
            BulkTrade.scheduler.schedule(() -> {
                // only commit the trade if in stock, after delay. (delay to check if instock)
                if (!getOffers().get(BulkTrade.selectedVillagerSlot).isOutOfStock()) {
                    BulkTrade.LOGGER.info("Performing automatic restock :O");
                    setSelectionHint(BulkTrade.selectedVillagerSlot);
                    tryMoveItems(BulkTrade.selectedVillagerSlot);
                    Minecraft.getInstance().getConnection().send(new ServerboundSelectTradePacket(BulkTrade.selectedVillagerSlot));
                }
                BulkTrade.triggerRestock = false;
            }, 150, TimeUnit.MILLISECONDS);
        }
    }
}
