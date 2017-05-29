package eyeq.furnacetools.item.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class FurnaceToolsUtils {
    public static int harvestAndSmeltBlock(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity, float chance) {
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
        List<ItemStack> drops = state.getBlock().getDrops(world, pos, state, fortune);
        if(entity instanceof EntityPlayer) {
            chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(drops, world, pos, state, fortune, chance, false, (EntityPlayer) entity);
        }

        int smelt = 0;
        for(ItemStack drop : drops) {
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(drop);
            if(result.isEmpty()) {
                Block.spawnAsEntity(world, pos, drop);
            } else {
                smelt++;
                if(world.rand.nextFloat() <= chance) {
                    Block.spawnAsEntity(world, pos, result.copy());
                }
            }
        }
        world.setBlockToAir(pos);
        return smelt;
    }
}
