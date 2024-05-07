package cn.cyanbukkit.plant.effect

import cn.cyanbukkit.plant.PlantSugarBeet.landBlockY
import cn.cyanbukkit.plant.effect.SprinklerHead.getBlockPlantIsSuccess
import cn.cyanbukkit.plant.noise.SpawnEntity.breakBlock
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import kotlin.math.abs

data class Region(
    val pos1: Location,
    val pos2: Location
) {

    val x = NumberPair(pos1.blockX, pos2.blockX)
    val z = NumberPair(pos1.blockZ, pos2.blockZ)


    fun line(direction: String): List<Line> {
        val list = mutableListOf<Line>()

        when (direction) {
            "x+" -> {
                val minZ = minOf(pos1.blockZ, pos2.blockZ)
                val maxZ = maxOf(pos1.blockZ, pos2.blockZ)
                val startX = minOf(pos1.blockX, pos2.blockX)
                val endX = maxOf(pos1.blockX, pos2.blockX)
                for (z in minZ..maxZ) {
                    val start = Location(pos1.world, startX + 0.5, landBlockY + 1.0, z + 0.5)
                    val end = Location(pos1.world, endX + 1.5, landBlockY + 1.0, z + 0.5)
                    list.add(Line(start, end))
                }
            }

            "x-" -> {
                val minZ = minOf(pos1.blockZ, pos2.blockZ)
                val maxZ = maxOf(pos1.blockZ, pos2.blockZ)
                val startX = maxOf(pos1.blockX, pos2.blockX)
                val endX = minOf(pos1.blockX, pos2.blockX)
                for (z in minZ..maxZ) {
                    val start = Location(pos1.world, startX + 0.5, landBlockY + 1.0, z + 0.5)
                    val end = Location(pos1.world, endX + 1.5, landBlockY + 1.0, z + 0.5)
                    list.add(Line(start, end))
                }
            }

            "z+" -> {
                val minX = minOf(pos1.blockX, pos2.blockX)
                val maxX = maxOf(pos1.blockX, pos2.blockX)
                val startZ = minOf(pos1.blockZ, pos2.blockZ)
                val endZ = maxOf(pos1.blockZ, pos2.blockZ)
                for (x in minX..maxX) {
                    val start = Location(pos1.world, x + 0.5, landBlockY + 1.0, startZ + 0.5)
                    val end = Location(pos1.world, x + 0.5, landBlockY + 1.0, endZ + 1.5)
                    list.add(Line(start, end))
                }
            }

            "z-" -> {
                val minX = minOf(pos1.blockX, pos2.blockX)
                val maxX = maxOf(pos1.blockX, pos2.blockX)
                val startZ = maxOf(pos1.blockZ, pos2.blockZ)
                val endZ = minOf(pos1.blockZ, pos2.blockZ)
                for (x in minX..maxX) {
                    val start = Location(pos1.world, x + 0.5, landBlockY + 1.0, startZ + 0.5)
                    val end = Location(pos1.world, x + 0.5, landBlockY + 1.0, endZ + 1.5)
                    list.add(Line(start, end))
                }
            }
        }

        return list
    }

    fun planting() : List<Block> {
        val list = mutableListOf<Block>()
        val minX = minOf(pos1.blockX, pos2.blockX)
        val maxX = maxOf(pos1.blockX, pos2.blockX)
        val minZ = minOf(pos1.blockZ, pos2.blockZ)
        val maxZ = maxOf(pos1.blockZ, pos2.blockZ)
        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                val block = Location(pos1.world, x + 0.5, landBlockY + 1.0, z + 0.5).block
                list.add(block)
            }
        }
        return list
    }

    /**
     * 获取植物已经成熟的方块
     */
    fun getPlantIsSuccess() : List<Block> {
        val list = mutableListOf<Block>()
        planting().forEach {
            if (it.type != Material.AIR) {
                // 获取植物的Crops
                if (getBlockPlantIsSuccess(it.location.block)) {
                    list.add(it.location.block)
                }
            }
        }
        return list
    }

    fun default() {
        // 给传中的区域填充耕地方块
        planting().forEach {
           it.breakBlock()
        }
        for (dx in x.min..x.max) {
            for (dz in z.min..z.max) {
                val block = Location(pos1.world, dx.toDouble(), landBlockY.toDouble(), dz.toDouble()).block
                block.type = org.bukkit.Material.SOIL
                block.world.playEffect(block.location, org.bukkit.Effect.STEP_SOUND, 60)
            }
        }
    }

    fun getsize(): Int {
        return x.len * z.len
    }

    fun random() : Location {
        val x = (x.min..x.max).random()
        val z = (z.min..z.max).random()
        return Location(pos1.world, x.toDouble(), landBlockY.toDouble(), z.toDouble())
    }

}

data class Line(
    val start: Location,
    val end: Location
) {
    fun getLineHasBlockSize() : Int {
        // 返回这个条线有多少个方块
        return abs(start.blockX - end.blockX) + abs(start.blockZ - end.blockZ) + 1
    }
}

class NumberPair(a: Int, b: Int) {
    val min = minOf(a, b)
    val max = maxOf(a, b)
    operator fun contains(n: Int): Boolean {
        return n in min..max
    }

    val len = max - min + 1
}