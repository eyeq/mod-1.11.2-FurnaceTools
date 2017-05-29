package eyeq.furnacetools.item;

import eyeq.util.entity.player.EntityPlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPickaxeFurnaceFire extends ItemPickaxeFurnace {
    public ItemPickaxeFurnaceFire(ToolMaterial material, boolean isDamage) {
        super(material, isDamage);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(EntityPlayerUtils.onItemPlace(player, world, itemStack, pos, facing, Blocks.FIRE.getDefaultState())) {
            itemStack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }
}
