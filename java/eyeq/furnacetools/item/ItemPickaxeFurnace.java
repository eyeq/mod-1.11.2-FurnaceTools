package eyeq.furnacetools.item;

import eyeq.furnacetools.item.util.FurnaceToolsUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class ItemPickaxeFurnace extends ItemPickaxe {
    protected boolean isDamage;

    public ItemPickaxeFurnace(ToolMaterial material, boolean isDamage) {
        super(material);
        this.isDamage = isDamage;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {
        super.onBlockDestroyed(stack, world, state, pos, entity);
        if(state.getBlockHardness(world, pos) == 0.0) {
            return true;
        }
        if(world.isRemote || !entity.isSneaking()) {
            return true;
        }
        if(!ForgeHooks.canToolHarvestBlock(world, pos, stack) || getStrVsBlock(stack, state) == 1.0) {
            return true;
        }
        int damage = FurnaceToolsUtils.harvestAndSmeltBlock(stack, world, state, pos, entity, 0.67F);
        if(isDamage) {
            stack.damageItem(damage, entity);
        }
        return true;
    }
}
