package eyeq.furnacetools.item;

import eyeq.util.entity.player.EntityPlayerUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHoeFurnaceFire extends ItemHoeFurnace {
    public ItemHoeFurnaceFire(ToolMaterial material, boolean isDamage) {
        super(material, isDamage);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() != Blocks.SAND) {
            return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        }
        world.setBlockToAir(pos);
        if(EntityPlayerUtils.onItemPlace(player, world, itemStack, pos.offset(facing.getOpposite()), facing, Blocks.WATER.getDefaultState())) {
            itemStack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }
        world.setBlockState(pos, state);
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }
}
