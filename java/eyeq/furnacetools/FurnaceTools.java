package eyeq.furnacetools;

import eyeq.furnacetools.item.*;
import eyeq.util.client.model.UModelCreator;
import eyeq.util.client.model.UModelLoader;
import eyeq.util.client.model.gson.ItemmodelJsonFactory;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import eyeq.util.creativetab.UCreativeTab;
import eyeq.util.item.UItemAxe;
import eyeq.util.item.UItemSword;
import eyeq.util.item.crafting.FuelHandler;
import eyeq.util.item.crafting.UCraftingManager;
import eyeq.util.oredict.UOreDictionary;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.io.File;

import static eyeq.furnacetools.FurnaceTools.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
@Mod.EventBusSubscriber
public class FurnaceTools {
    public static final String MOD_ID = "eyeq_furnacetools";

    @Mod.Instance(MOD_ID)
    public static FurnaceTools instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static Item burningFurnace;
    public static Item burningCube;
    public static final CreativeTabs TAB_FURNACE_TOOLS = new UCreativeTab("FurnaceTools", () -> new ItemStack(burningCube));

    public static Item furnaceAxe;
    public static Item furnacePickaxe;
    public static Item furnaceSpade;
    public static Item furnaceHoe;
    public static Item furnaceSword;
    public static Item burningFurnaceAxe;
    public static Item burningFurnacePickaxe;
    public static Item burningFurnaceSpade;
    public static Item burningFurnaceHoe;
    public static Item burningFurnaceSword;
    public static Item overheatFurnaceAxe;
    public static Item overheatFurnacePickaxe;
    public static Item overheatFurnaceSpade;
    public static Item overheatFurnaceHoe;
    public static Item overheatFurnaceSword;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        addRecipes();
        if(event.getSide().isServer()) {
            return;
        }
        renderItemModels();
        createFiles();
    }

    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
        burningFurnace = new Item().setUnlocalizedName("burningFurnace").setCreativeTab(TAB_FURNACE_TOOLS);
        burningCube = new Item().setUnlocalizedName("burningCube").setCreativeTab(TAB_FURNACE_TOOLS);

        Item.ToolMaterial materialFurnace = EnumHelper.addToolMaterial("FURNACE", 1, 1048, 4.0F, 1.0F, 5);
        Item.ToolMaterial materialBurningFurnace = EnumHelper.addToolMaterial("FURNACE_DRY", 1, 786, 6.0F, 2.0F, 15);
        Item.ToolMaterial materialOverheatFurnace = EnumHelper.addToolMaterial("FURNACE_LAVA", 2, 131, 12.0F, 4.0F, 10);

        materialFurnace.setRepairItem(new ItemStack(Blocks.FURNACE));
        materialBurningFurnace.setRepairItem(new ItemStack(burningFurnace));
        materialOverheatFurnace.setRepairItem(new ItemStack(burningCube));

        furnaceAxe = new UItemAxe(materialFurnace).setUnlocalizedName("furnaceAxe").setCreativeTab(TAB_FURNACE_TOOLS);
        furnacePickaxe = new ItemPickaxe(materialFurnace) {}.setUnlocalizedName("furnacePickaxe").setCreativeTab(TAB_FURNACE_TOOLS);
        furnaceSpade = new ItemSpade(materialFurnace).setUnlocalizedName("furnaceSpade").setCreativeTab(TAB_FURNACE_TOOLS);
        furnaceHoe = new ItemHoe(materialFurnace).setUnlocalizedName("furnaceHoe").setCreativeTab(TAB_FURNACE_TOOLS);
        furnaceSword = new ItemSword(materialFurnace).setUnlocalizedName("furnaceSword").setCreativeTab(TAB_FURNACE_TOOLS);
        burningFurnaceAxe = new ItemAxeFurnace(materialBurningFurnace, true).setUnlocalizedName("burningFurnaceAxe").setCreativeTab(TAB_FURNACE_TOOLS);
        burningFurnacePickaxe = new ItemPickaxeFurnace(materialBurningFurnace, true).setUnlocalizedName("burningFurnacePickaxe").setCreativeTab(TAB_FURNACE_TOOLS);
        burningFurnaceSpade = new ItemSpadeFurnace(materialBurningFurnace, true).setUnlocalizedName("burningFurnaceSpade").setCreativeTab(TAB_FURNACE_TOOLS);
        burningFurnaceHoe = new ItemHoeFurnace(materialBurningFurnace, true).setUnlocalizedName("burningFurnaceHoe").setCreativeTab(TAB_FURNACE_TOOLS);
        burningFurnaceSword = new ItemSword(materialBurningFurnace).setUnlocalizedName("burningFurnaceSword").setCreativeTab(TAB_FURNACE_TOOLS);
        overheatFurnaceAxe = new ItemAxeFurnaceFire(materialOverheatFurnace, false).setUnlocalizedName("overheatFurnaceAxe").setCreativeTab(TAB_FURNACE_TOOLS);
        overheatFurnacePickaxe = new ItemPickaxeFurnaceFire(materialOverheatFurnace, false).setUnlocalizedName("overheatFurnacePickaxe").setCreativeTab(TAB_FURNACE_TOOLS);
        overheatFurnaceSpade = new ItemSpadeFurnaceFire(materialOverheatFurnace, false).setUnlocalizedName("overheatFurnaceSpade").setCreativeTab(TAB_FURNACE_TOOLS);
        overheatFurnaceHoe = new ItemHoeFurnaceFire(materialOverheatFurnace, false).setUnlocalizedName("overheatFurnaceHoe").setCreativeTab(TAB_FURNACE_TOOLS);
        overheatFurnaceSword = new UItemSword(materialOverheatFurnace).setAttackFire(5, 1.0F).setUnlocalizedName("overheatFurnaceSword").setCreativeTab(TAB_FURNACE_TOOLS);

        GameRegistry.register(burningFurnace, resource.createResourceLocation("burning_furnace"));
        GameRegistry.register(burningCube, resource.createResourceLocation("burning_cube"));
        GameRegistry.register(furnaceAxe, resource.createResourceLocation("furnace_axe"));
        GameRegistry.register(furnacePickaxe, resource.createResourceLocation("furnace_pickaxe"));
        GameRegistry.register(furnaceSpade, resource.createResourceLocation("furnace_spade"));
        GameRegistry.register(furnaceHoe, resource.createResourceLocation("furnace_hoe"));
        GameRegistry.register(furnaceSword, resource.createResourceLocation("furnace_sword"));
        GameRegistry.register(burningFurnaceAxe, resource.createResourceLocation("burning_furnace_axe"));
        GameRegistry.register(burningFurnacePickaxe, resource.createResourceLocation("burning_furnace_pickaxe"));
        GameRegistry.register(burningFurnaceSpade, resource.createResourceLocation("burning_furnace_spade"));
        GameRegistry.register(burningFurnaceHoe, resource.createResourceLocation("burning_furnace_hoe"));
        GameRegistry.register(burningFurnaceSword, resource.createResourceLocation("burning_furnace_sword"));
        GameRegistry.register(overheatFurnaceAxe, resource.createResourceLocation("overheat_furnace_axe"));
        GameRegistry.register(overheatFurnacePickaxe, resource.createResourceLocation("overheat_furnace_pickaxe"));
        GameRegistry.register(overheatFurnaceSpade, resource.createResourceLocation("overheat_furnace_spade"));
        GameRegistry.register(overheatFurnaceHoe, resource.createResourceLocation("overheat_furnace_hoe"));
        GameRegistry.register(overheatFurnaceSword, resource.createResourceLocation("overheat_furnace_sword"));
    }

    public static void addRecipes() {
        GameRegistry.addRecipe(new ShapedOreRecipe(burningFurnace, "XXX", "XYX", "XXX",
                'X', UOreDictionary.OREDICT_COBBLESTONE, 'Y', UOreDictionary.OREDICT_COAL_BLOCK));
        GameRegistry.addRecipe(new ShapedOreRecipe(burningCube, "XXX", "XYX", "XXX",
                'X', Items.LAVA_BUCKET, 'Y', burningFurnace));
        GameRegistry.addRecipe(UCraftingManager.getRecipeAxe(new ItemStack(furnaceAxe), Blocks.FURNACE, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipePickaxe(new ItemStack(furnacePickaxe), Blocks.FURNACE, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipeSpade(new ItemStack(furnaceSpade), Blocks.FURNACE, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipeHoe(new ItemStack(furnaceHoe), Blocks.FURNACE, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipeSword(new ItemStack(furnaceSword), Blocks.FURNACE, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipeAxe(new ItemStack(burningFurnaceAxe), burningFurnace, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipePickaxe(new ItemStack(burningFurnacePickaxe), burningFurnace, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipeSpade(new ItemStack(burningFurnaceSpade), burningFurnace, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipeHoe(new ItemStack(burningFurnaceHoe), burningFurnace, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipeSword(new ItemStack(burningFurnaceSword), burningFurnace, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipeAxe(new ItemStack(overheatFurnaceAxe), burningCube, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipePickaxe(new ItemStack(overheatFurnacePickaxe), burningCube, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipeSpade(new ItemStack(overheatFurnaceSpade), burningCube, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipeHoe(new ItemStack(overheatFurnaceHoe), burningCube, UOreDictionary.OREDICT_LOG));
        GameRegistry.addRecipe(UCraftingManager.getRecipeSword(new ItemStack(overheatFurnaceSword), burningCube, UOreDictionary.OREDICT_LOG));

        GameRegistry.registerFuelHandler(new FuelHandler(burningFurnace, 16000));
        GameRegistry.registerFuelHandler(new FuelHandler(burningCube, 176000));
    }

    @SideOnly(Side.CLIENT)
    public static void renderItemModels() {
        UModelLoader.setCustomModelResourceLocation(burningFurnace);
        UModelLoader.setCustomModelResourceLocation(burningCube);
        UModelLoader.setCustomModelResourceLocation(furnaceAxe);
        UModelLoader.setCustomModelResourceLocation(furnacePickaxe);
        UModelLoader.setCustomModelResourceLocation(furnaceSpade);
        UModelLoader.setCustomModelResourceLocation(furnaceHoe);
        UModelLoader.setCustomModelResourceLocation(furnaceSword);
        UModelLoader.setCustomModelResourceLocation(burningFurnaceAxe);
        UModelLoader.setCustomModelResourceLocation(burningFurnacePickaxe);
        UModelLoader.setCustomModelResourceLocation(burningFurnaceSpade);
        UModelLoader.setCustomModelResourceLocation(burningFurnaceHoe);
        UModelLoader.setCustomModelResourceLocation(burningFurnaceSword);
        UModelLoader.setCustomModelResourceLocation(overheatFurnaceAxe);
        UModelLoader.setCustomModelResourceLocation(overheatFurnacePickaxe);
        UModelLoader.setCustomModelResourceLocation(overheatFurnaceSpade);
        UModelLoader.setCustomModelResourceLocation(overheatFurnaceHoe);
        UModelLoader.setCustomModelResourceLocation(overheatFurnaceSword);
    }

    public static void createFiles() {
        File project = new File("../1.11.2-FurnaceTools");

        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, TAB_FURNACE_TOOLS, "Furnace Tools");
        language.register(LanguageResourceManager.JA_JP, TAB_FURNACE_TOOLS, "炉ツール");

        language.register(LanguageResourceManager.EN_US, burningFurnace, "Burning Furnace");
        language.register(LanguageResourceManager.JA_JP, burningFurnace, "着火かまど");
        language.register(LanguageResourceManager.EN_US, burningCube, "Burning Cube");
        language.register(LanguageResourceManager.JA_JP, burningCube, "燃焼キューブ");

        language.register(LanguageResourceManager.EN_US, furnaceAxe, "Furnace Axe");
        language.register(LanguageResourceManager.JA_JP, furnaceAxe, "炉斧");
        language.register(LanguageResourceManager.EN_US, furnacePickaxe, "Furnace Pickaxe");
        language.register(LanguageResourceManager.JA_JP, furnacePickaxe, "炉ツルハシ");
        language.register(LanguageResourceManager.EN_US, furnaceSpade, "Furnace Spade");
        language.register(LanguageResourceManager.JA_JP, furnaceSpade, "炉シャベル");
        language.register(LanguageResourceManager.EN_US, furnaceHoe, "Furnace Hoe");
        language.register(LanguageResourceManager.JA_JP, furnaceHoe, "炉クワ");
        language.register(LanguageResourceManager.EN_US, furnaceSword, "Furnace Sword");
        language.register(LanguageResourceManager.JA_JP, furnaceSword, "炉剣");

        language.register(LanguageResourceManager.EN_US, burningFurnaceAxe, "Burning Furnace Axe");
        language.register(LanguageResourceManager.JA_JP, burningFurnaceAxe, "稼働炉斧");
        language.register(LanguageResourceManager.EN_US, burningFurnacePickaxe, "Burning Furnace Pickaxe");
        language.register(LanguageResourceManager.JA_JP, burningFurnacePickaxe, "炉稼働ツルハシ");
        language.register(LanguageResourceManager.EN_US, burningFurnaceSpade, "Burning Furnace Spade");
        language.register(LanguageResourceManager.JA_JP, burningFurnaceSpade, "稼働炉シャベル");
        language.register(LanguageResourceManager.EN_US, burningFurnaceHoe, "Burning Furnace Hoe");
        language.register(LanguageResourceManager.JA_JP, burningFurnaceHoe, "稼働炉クワ");
        language.register(LanguageResourceManager.EN_US, burningFurnaceSword, "Burning Furnace Sword");
        language.register(LanguageResourceManager.JA_JP, burningFurnaceSword, "稼働炉剣");

        language.register(LanguageResourceManager.EN_US, overheatFurnaceAxe, "Overheat Furnace Axe");
        language.register(LanguageResourceManager.JA_JP, overheatFurnaceAxe, "溶岩炉斧");
        language.register(LanguageResourceManager.EN_US, overheatFurnacePickaxe, "Overheat Furnace Pickaxe");
        language.register(LanguageResourceManager.JA_JP, overheatFurnacePickaxe, "溶岩炉ツルハシ");
        language.register(LanguageResourceManager.EN_US, overheatFurnaceSpade, "Overheat Furnace Spade");
        language.register(LanguageResourceManager.JA_JP, overheatFurnaceSpade, "溶岩炉シャベル");
        language.register(LanguageResourceManager.EN_US, overheatFurnaceHoe, "Overheat Furnace Hoe");
        language.register(LanguageResourceManager.JA_JP, overheatFurnaceHoe, "溶岩炉クワ");
        language.register(LanguageResourceManager.EN_US, overheatFurnaceSword, "Overheat Furnace Sword");
        language.register(LanguageResourceManager.JA_JP, overheatFurnaceSword, "溶岩炉剣");

        ULanguageCreator.createLanguage(project, MOD_ID, language);

        UModelCreator.createItemJson(project, burningFurnace, ItemmodelJsonFactory.ItemmodelParent.GENERATED);
        UModelCreator.createItemJson(project, burningCube, ItemmodelJsonFactory.ItemmodelParent.GENERATED);
        UModelCreator.createItemJson(project, furnaceAxe, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, furnacePickaxe, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, furnaceSpade, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, furnaceHoe, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, furnaceSword, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, burningFurnaceAxe, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, burningFurnacePickaxe, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, burningFurnaceSpade, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, burningFurnaceHoe, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, burningFurnaceSword, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, overheatFurnaceAxe, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, overheatFurnacePickaxe, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, overheatFurnaceSpade, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, overheatFurnaceHoe, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
        UModelCreator.createItemJson(project, overheatFurnaceSword, ItemmodelJsonFactory.ItemmodelParent.HANDHELD);
    }
}
