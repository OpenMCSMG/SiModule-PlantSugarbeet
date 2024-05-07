package cn.cyanbukkit.plant.effect

import cn.cyanbukkit.plant.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import org.bukkit.Material
import org.bukkit.TreeSpecies
import org.bukkit.block.Block
import org.bukkit.material.Sapling
import java.util.*
import java.util.logging.Level

@Suppress("DEPRECATION")
object Materials {
    enum class EMaterial {
        Air, Water, Bone_Meal, Sea_Grass, Sea_Pickle, Soil, Pumpkin, Pumpkin_Stem,
        Melon, Melon_Stem, Wheat_Seeds, Carrot, Potato, Grass, Sugar_Cane, Cactus, Brown_Mushroom, Red_Mushroom, Beetroot_Seeds,
        Oak_Sapling, Dark_Oak_Sapling, Spruce_Sapling, Jungle_Sapling, Birch_Sapling, Acacia_Sapling, Sapling,
        Warped_Fungus, Crimson_Fungus, Fern, Bamboo, Kelp, Tall_Grass, Tall_Fern, Cocoa_Beans, Sweet_Berry_Bush,
        Weeping_Vines, Twisting_Vines, Azalea_Bush, Cave_Vines, Flowering_Azalea, Azalea, UNKNOWN
    }

    private val MaterialByType = HashMap<String, EMaterial>()
    private val TreeTypesL = HashMap<String, EMaterial>()

    fun IsSimilar(mat: String, type: EMaterial): Boolean {
        return MaterialByType[mat] == type
    }

    fun IsSimilar(mat: Block, type: EMaterial): Boolean {
        val matType = TypeConverter(mat)
        return MaterialByType.containsKey(matType) && MaterialByType[matType] == type
    }

    fun TypeConverter(mat: Block): String {
        val output = mat.toString()
        var type = ""
        var data = ""
        for (str in output.split(",")) {
            val dataType = str.split("=")[0]
            val dataData = str.split("=")[1]
            if (dataType == "type") type = dataData
            if (dataType == "data") data = dataData.replace("}", "")
        }
        return if (type == "SAPLING") "$type|$data" else type
    }

    fun GetTypes(mat: Block): List<EMaterial> {
        val returnV = ArrayList<EMaterial>()
        val type = TypeConverter(mat)
        for ((key, value) in MaterialByType) {
            if (type.equals(key, ignoreCase = true)) {
                returnV.add(value)
            }
        }
        return returnV
    }

    fun GetType(mat: Block): EMaterial {
        val type = TypeConverter(mat)
        return GetType(type)
    }

    fun GetType(type: String): EMaterial {
        for ((key, value) in MaterialByType) {
            if (type.equals(key, ignoreCase = true)) {
                return value
            }
        }
        return EMaterial.UNKNOWN
    }

    fun GetValueForType(type: EMaterial): String {
        for ((key, value) in MaterialByType) {
            if (value == type) {
                return key
            }
        }
        return "Air"
    }

    fun SetType(mat: Block, type: EMaterial): Boolean {
        var placed = false
        if (MaterialByType.containsValue(type)) {
            for ((key, value) in MaterialByType) {
                if (value == type) {
                    try {
                        mat.type = Material.valueOf(key)
                        placed = true
                        return placed
                    } catch (ex: Exception) {
                        placed = false
                    }
                }
            }
            if (!placed) {
                cyanPlugin.logger.log(Level.SEVERE, "Could not place material: $type")
            }
        }
        return placed
    }

    private fun registerMaterial(material: EMaterial, vararg aliases: String) {
        for (alias in aliases) {
            MaterialByType[alias] = material
        }
    }

    private fun registerTreeSpecies(material: EMaterial, vararg aliases: String) {
        for (alias in aliases) {
            TreeTypesL[alias] = material
        }
    }

    fun InitializeMaterials() {
        //Crops/Blocks
        registerMaterial(EMaterial.Grass, "GRASS", "LONG_GRASS")
        registerMaterial(EMaterial.Tall_Grass, "TALL_GRASS", "LARGE_GRASS")
        registerMaterial(EMaterial.Fern, "FERN")
        registerMaterial(EMaterial.Tall_Fern, "TALL_FERN", "LARGE_FERN")
        registerMaterial(EMaterial.Potato, "POTATOES", "POTATO")
        registerMaterial(EMaterial.Carrot, "CARROTS", "CARROT")
        registerMaterial(EMaterial.Wheat_Seeds, "WHEAT", "CROPS")
        registerMaterial(EMaterial.Beetroot_Seeds, "BEETROOTS", "BEETROOT_BLOCK")
        registerMaterial(EMaterial.Sweet_Berry_Bush, "BEE_GROWABLES", "SWEET_BERRY_BUSH")
        registerMaterial(EMaterial.Pumpkin_Stem, "PUMPKIN_STEM", "PUMPKIN_SEEDS")
        registerMaterial(EMaterial.Melon_Stem, "MELON_STEM", "MELON_SEEDS")
        registerMaterial(EMaterial.Cocoa_Beans, "COCOA_BEANS", "COCOA")
        registerMaterial(EMaterial.Pumpkin, "PUMPKIN")
        registerMaterial(EMaterial.Melon, "MELON_BLOCK")
        registerMaterial(EMaterial.Sugar_Cane, "SUGAR_CANE", "SUGAR_CANE_BLOCK")
        registerMaterial(EMaterial.Cactus, "CACTUS")
        registerMaterial(EMaterial.Soil, "SOIL", "FARMLAND")
        registerMaterial(EMaterial.Air, "AIR")
        registerMaterial(EMaterial.Water, "WATER")
        registerMaterial(EMaterial.Sea_Grass, "SEAGRASS")
        registerMaterial(EMaterial.Sea_Pickle, "SEA_PICKLE")
        registerMaterial(EMaterial.Kelp, "KELP", "KELP_PLANT")
        registerMaterial(EMaterial.Twisting_Vines, "TWISTING_VINES", "TWISTING_VINES_PLANT")
        registerMaterial(EMaterial.Weeping_Vines, "WEEPING_VINES", "WEEPING_VINES_PLANT")
        registerMaterial(EMaterial.Cave_Vines, "CAVE_VINES", "CAVE_VINES_PLANT")
        registerMaterial(EMaterial.Bamboo, "BAMBOO_SAPLING", "BAMBOO")
        registerMaterial(EMaterial.Sapling,
            "OAK_SAPLING", "SAPLING|0",
            "SPRUCE_SAPLING", "SAPLING|1",
            "BIRCH_SAPLING", "SAPLING|2",
            "JUNGLE_SAPLING", "SAPLING|3",
            "ACACIA_SAPLING", "SAPLING|4",
            "DARK_OAK_SAPLING", "SAPLING|5",
            "RED_MUSHROOM", "BROWN_MUSHROOM",
            "WARPED_FUNGUS", "CRIMSON_FUNGUS",
            "FLOWERING_AZALEA", "AZALEA")
        //Trees
        registerTreeSpecies(EMaterial.Oak_Sapling, "OAK_SAPLING", "SAPLING|0")
        registerTreeSpecies(EMaterial.Spruce_Sapling, "SPRUCE_SAPLING", "SAPLING|1")
        registerTreeSpecies(EMaterial.Birch_Sapling, "BIRCH_SAPLING", "SAPLING|2")
        registerTreeSpecies(EMaterial.Jungle_Sapling, "JUNGLE_SAPLING", "SAPLING|3")
        registerTreeSpecies(EMaterial.Acacia_Sapling, "ACACIA_SAPLING", "SAPLING|4")
        registerTreeSpecies(EMaterial.Dark_Oak_Sapling, "DARK_OAK_SAPLING", "SAPLING|5")
        registerTreeSpecies(EMaterial.Red_Mushroom, "RED_MUSHROOM")
        registerTreeSpecies(EMaterial.Brown_Mushroom, "BROWN_MUSHROOM")
        registerTreeSpecies(EMaterial.Warped_Fungus, "WARPED_FUNGUS")
        registerTreeSpecies(EMaterial.Crimson_Fungus, "CRIMSON_FUNGUS")
        registerTreeSpecies(EMaterial.Flowering_Azalea, "FLOWERING_AZALEA")
        registerTreeSpecies(EMaterial.Azalea, "AZALEA")
    }

    fun InitExtra() {
        MaterialByType["MELON"] = EMaterial.Melon
    }

    fun GetTreeType(mat: Block): EMaterial? {
        val type = TypeConverter(mat)
        for ((key, value) in TreeTypesL) {
            if (type.equals(key, ignoreCase = true)) {
                return value
            }
        }
        return null
    }

    fun IsTreeSimilar(mat: Block, type: EMaterial): Boolean {
        val matType = GetTreeType(mat)
        return matType == type
    }

    fun SetTree(mat: Block, type: EMaterial, treeType: TreeTypes.ETreeType) {
        var placed = false
        if (TreeTypesL.containsValue(type)) {
            for ((key, value) in TreeTypesL) {
                if (value == type) {
                    try {
                        if (key.contains("|")) {
                            mat.type = Material.valueOf("SAPLING")
                            val bs = mat.state
                            val state = bs.data
                            if (state is Sapling) {
                                val typeX = TreeTypes.GetType(treeType)
                                state.species = TreeSpecies.valueOf(typeX.toString())
                            }
                            bs.data = state
                            bs.update(true)
                        } else
                            mat.type = Material.valueOf(key)
                        placed = true
                        return
                    } catch (ex: Exception) {
                        placed = false
                    }
                }
            }
            if (!placed) {
                cyanPlugin.logger.log(Level.SEVERE, "Could not place sapling: $type")
            }
        }
    }

    fun ContainsType(type: EMaterial): Boolean {
        return MaterialByType.containsValue(type)
    }

    fun isTypeAllowed(type: EMaterial): Boolean {
        return type == EMaterial.Beetroot_Seeds || type == EMaterial.Wheat_Seeds || type == EMaterial.Potato || type == EMaterial.Carrot
    }
}