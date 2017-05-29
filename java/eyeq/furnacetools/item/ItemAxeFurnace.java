package eyeq.furnacetools.item;

import eyeq.furnacetools.item.util.FurnaceToolsUtils;
import eyeq.util.item.UItemAxe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class ItemAxeFurnace extends UItemAxe {
    protected boolean isDamage;

    public ItemAxeFurnace(ToolMaterial material, boolean isDamage) {
        super(material);
        this.isDamage = isDamage;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {
        super.onBlockDestroyed(itemStack, world, state, pos, entity);
        if(state.getBlockHardness(world, pos) == 0.0) {
            return true;
        }
        if(world.isRemote || !entity.isSneaking()) {
            return true;
        }
        if(!ForgeHooks.canToolHarvestBlock(world, pos, itemStack) || getStrVsBlock(itemStack, state) == 1.0) {
            return true;
        }
        int damage = FurnaceToolsUtils.harvestAndSmeltBlock(itemStack, world, state, pos, entity, 0.67F);
        if(isDamage) {
            itemStack.damageItem(damage, entity);
        }
        return true;
    }
}
