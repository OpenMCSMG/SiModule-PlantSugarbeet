package cn.cyanbukkit.plant.effect

import cn.cyanbukkit.plant.PlantSugarBeet
import cn.cyanbukkit.plant.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.plant.effect.Materials.EMaterial
import cn.cyanbukkit.plant.effect.Materials.EMaterial.*
import cn.cyanbukkit.plant.effect.Materials.IsSimilar
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.BlockState
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.material.CocoaPlant
import org.bukkit.material.CocoaPlant.CocoaPlantSize
import org.bukkit.material.Crops
import org.bukkit.material.MaterialData
import org.bukkit.util.Vector
import java.util.*


object SprinklerHead : Listener {
    fun use(player: Player) {
        player.launchProjectile(Snowball::class.java).apply {
            shooter = player
            setMetadata(
                "sprinkler",
                org.bukkit.metadata.FixedMetadataValue(cyanPlugin, true)
            )
        }
        player.playSound(player.location, Sound.ENTITY_ENDEREYE_LAUNCH, 1f, 1f)
    }



    @EventHandler
    fun onSnowballHit(e: org.bukkit.event.entity.ProjectileHitEvent) {
        if (e.entity.hasMetadata("sprinkler")) {
            val reg = PlantSugarBeet.region.planting()
            val willPut = e.entity.location.block
            if (reg.contains(willPut)) {
                plantCrop(willPut)
            }
        }
    }

    /**
     * TODO：支持兼容自定义种植
     */
    fun applyBoneMeal(block: Block): Boolean {
        val rand = Random()
        var particles = true
        when (Materials.GetType(block)) {
            Wheat_Seeds, Potato, Carrot -> {
                particles = block.getData() !== 7.toByte()
                if (particles) updateCropState(block, true)
            }

            Beetroot_Seeds -> {
                particles = block.getData() !== 3.toByte()
                if (particles) updateCropState(block, true)
            }

            Sweet_Berry_Bush -> return false
            Twisting_Vines -> growBlockInAir(block, rand.nextInt(21 - 18) + 18, false)
            Weeping_Vines -> growBlockInAir(block, rand.nextInt(21 - 18) + 18, true)
            Cave_Vines -> {}
            Cocoa_Beans -> {
                particles = (block.state.data as CocoaPlant).size != CocoaPlantSize.LARGE
                if (particles) updateCropState(block, false)
            }

            Pumpkin_Stem, Melon_Stem -> {
                particles = block.getData() !== 7.toByte()
                if (particles) {
                    updateRawData(block, true, 7)
                } else particles = true
            }

            Grass, Fern -> return false
            Sea_Grass -> return false
            Sea_Pickle -> return true
            Kelp -> {
                growBlockInWater(block, rand.nextInt(21 - 18) + 18)
                return true
            }

            Bamboo -> return false
            Cactus, Sugar_Cane -> {
                growBlockInAir(block, 2, false)
                return true
            }

            else -> return false
        }
        return particles
    }


    fun plantCrop(willPut: Block) {
        willPut.world.playSound(willPut.location, Sound.BLOCK_GRASS_PLACE, 1f, 1f)
        willPut.world.playSound(willPut.location, Sound.BLOCK_CLOTH_STEP, 1f, 1f)
        if (willPut.type != Material.BEETROOT_BLOCK) {
            willPut.type = Material.BEETROOT_BLOCK
        } else {
            updateCropState(willPut, true)
            willPut.world.spawnParticle(Particle.VILLAGER_HAPPY, willPut.location.add(0.5, 0.5, 0.5), 10, 0.5, 0.5, 0.5)
        }
    }

    fun Block.leftAndRight(target: Location) : List<Block> {
        val blocks = mutableListOf<Block>()
        // 计算出目标方向
        val targetDirection = target.toVector().subtract(this.location.toVector()).normalize()
        // 计算出左右两个方向
        val leftDirection = Vector(-targetDirection.z, 0.0, targetDirection.x)
        val rightDirection = Vector(targetDirection.z, 0.0, -targetDirection.x)
        // 获取左右两个方向的方块
        val leftBlock = this.getRelative(leftDirection.blockX - 1, leftDirection.blockY, leftDirection.blockZ)
        val rightBlock = this.getRelative(rightDirection.blockX + 1, rightDirection.blockY, rightDirection.blockZ)
        blocks.add(leftBlock)
        blocks.add(this)
        blocks.add(rightBlock)
        return blocks
    }

    fun Block.getEdge(size: Int) : List<Block> {
        val blocks = mutableListOf<Block>()
        for (x in -size..size) {
            for (z in -size..size) {
                blocks.add(this.world.getBlockAt(this.x + x, this.y, this.z + z))
            }
        }
        return blocks
    }


    private fun growBlockInWater(block: Block, layers: Int) {
        println("Growing a block within the water: $block")
        val x = block.location.x
        val y = block.location.y
        val z = block.location.z
        for (i in 1..layers) {
            val b1 = Location(block.location.world, x, y + i, z).block
            if (IsSimilar(b1, Water)) { // && Materials.IsSimilar(b2, EMaterial.Water)
                b1.type = block.type
                break
            }
        }
    }

    private fun growBlockInAir(block: Block, layers: Int, growDown: Boolean) {
        println("Growing a block within the air: $block\n Is Down: $growDown")
        val x = block.location.x
        val y = block.location.y
        val z = block.location.z
        for (i in 1..layers) {
            var b1: Block = Location(block.location.world, x, y + i, z).block
            if (growDown) b1 = Location(block.location.world, x, y - i, z).block
            if (IsSimilar(b1, Air)) { // && Materials.IsSimilar(b2, EMaterial.Air)
                b1.type = block.type
                break
            }
        }
    }

    /**
     * 植物已经成熟了
     */
    fun getBlockPlantIsSuccess(block: Block) : Boolean {
        val bs: BlockState = block.state
        val state: MaterialData = bs.data
        if (state is Crops) {
            return state.state == CropState.RIPE
        }
        return false
    }

    private fun updateCropState(block: Block, useRandom: Boolean) {
        val bs: BlockState = block.state
        val state: MaterialData = bs.data
        if (state is Crops) {
            val crop = state
            val curState = crop.state
            val rand = Random()
            var data = curState.data + 1
            if (useRandom) data += rand.nextInt(3)
            if (data > 7) data = 7
            crop.state = CropState.getByData(data.toByte())
        } else if (state is CocoaPlant) {
            val plant = state
            if (plant.size == CocoaPlantSize.SMALL) plant.size = CocoaPlantSize.MEDIUM
            else if (plant.size == CocoaPlantSize.MEDIUM) plant.size = CocoaPlantSize.LARGE
        } else println("Crop State invalid: $state")
        bs.data = state
        bs.update(true)
    }

    private fun updateRawData(block: Block, useRandom: Boolean, dataLimit: Int) {
        println("Modifying Raw Data for: $block")
        val bs: BlockState = block.state
        val rand = Random()
        var data: Int = bs.data.data + 1
        if (useRandom) data += rand.nextInt(3)
        if (data > dataLimit) data = dataLimit
        bs.rawData = data.toByte()
        bs.update(true)
    }

}
