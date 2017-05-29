package eyeq.furnacetools.item;

import eyeq.furnacetools.item.util.FurnaceToolsUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class ItemHoeFurnace extends ItemHoe {
    protected boolean isDamage;

    public ItemHoeFurnace(ToolMaterial material, boolean isDamage) {
        super(material);
        this.isDamage = isDamage;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {
        super.onBlockDestroyed(stack, world, state, pos, entity);
        if(world.isRemote || !entity.isSneaking()) {
            return true;
        }
        if(!(state.getBlock() instanceof IPlantable)) {
            return true;
        }
        int damage = FurnaceToolsUtils.harvestAndSmeltBlock(stack, world, state, pos, entity, 0.67F);
        if(isDamage) {
            stack.damageItem(damage, entity);
        }
        return true;
    }
}
