package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A collection of static maps of mineshaft type to the corresponding BlockSetSelector.
 * It's ugly, but it works.
 * I'm too lazy to reimplement the sexy architecture from 1.12.2, so this is what you get instead.
 *
 * Sorry.
 */
public class BlockSetSelectors {
    /**
     * Main block selectors.
     */
    public static Map<BetterMineshaftStructure.Type, BlockSetSelector> MAIN_SELECTOR = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.MESA,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.ICE,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            new BlockSetSelector(Blocks.STONE.getDefaultState()}
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        }
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockSetSelector) data[1]));

    /**
     * Floor block selectors.
     */
    public static Map<BetterMineshaftStructure.Type, BlockSetSelector> FLOOR_SELECTOR = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.MESA,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.ICE,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            new BlockSetSelector(Blocks.STONE.getDefaultState()}
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        }
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockSetSelector) data[1]));

    /**
     * Brick selectors.
     */
    public static Map<BetterMineshaftStructure.Type, BlockSetSelector> BRICK_SELECTOR = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.MESA,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.ICE,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            new BlockSetSelector(Blocks.STONE.getDefaultState())
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            new BlockSetSelector(Blocks.STONE.getDefaultState()}
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        }       
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockSetSelector) data[1]));

    /**
     * Leg selectors.
     */
    public static Map<BetterMineshaftStructure.Type, BlockSetSelector> LEG_SELECTOR = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            BlockSetSelector.from(Blocks.OAK_PLANKS.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructure.Type.MESA,
            BlockSetSelector.from(Blocks.DARK_OAK_PLANKS.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            BlockSetSelector.from(Blocks.JUNGLE_PLANKS.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            BlockSetSelector.from(Blocks.SPRUCE_PLANKS.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructure.Type.ICE,
            new BlockSetSelector(Blocks.SPRUCE_PLANKS.getDefaultState())
                .addBlock(Blocks.SPRUCE_PLANKS.getDefaultState(), .5f)
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            new BlockSetSelector(Blocks.ACACIA_PLANKS.getDefaultState())
                .addBlock(Blocks.ACACIA_PLANKS.getDefaultState(), .15f)
                .addBlock(Blocks.ACACIA_PLANKS.getDefaultState(), .15f)
                .addBlock(Blocks.ACACIA_PLANKS.getDefaultState(), .1f)
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            new BlockSetSelector(Blocks.ACACIA_PLANKS.getDefaultState())
                .addBlock(Blocks.SMOOTH_ACACIA_PLANKS.getDefaultState(), .15f)
                .addBlock(Blocks.ACACIA_PLANKS.getDefaultState(), .15f)
                .addBlock(Blocks.ACACIA_PLANKS.getDefaultState(), .1f)
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            BlockSetSelector.from(Blocks.ACACIA_PLANKS.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            BlockSetSelector.from(Blocks.OAK_PLANKS.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockSetSelector) data[1]));

    /**
     * Main Blocks.
     */
    public static Map<BetterMineshaftStructure.Type, BlockState> MAIN_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            Blocks.OAK_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MESA,
            Blocks.DARK_OAK_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            Blocks.JUNGLE_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            Blocks.SPRUCE_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.ICE,
            Blocks.SPRUCE_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            Blocks.ACACIA_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            Blocks.ACACIA_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            Blocks.ACACIA_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            Blocks.OAK_PLANKS.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Support Blocks.
     */
    public static Map<BetterMineshaftStructure.Type, BlockState> SUPPORT_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            Blocks.OAK_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MESA,
            Blocks.DARK_OAK_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            Blocks.JUNGLE_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            Blocks.SPRUCE_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.ICE,
            Blocks.SPRUCE_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            Blocks.ACACIA_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            Blocks.ACACIA_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            Blocks.ACACIA_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            Blocks.OAK_FENCE.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Slab Blocks.
     */
    public static Map<BetterMineshaftStructure.Type, BlockState> SLAB_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            Blocks.OAK_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MESA,
            Blocks.DARK_OAK_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            Blocks.JUNGLE_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            Blocks.SPRUCE_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.ICE,
            Blocks.SPRUCE_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            Blocks.ACACIA_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            Blocks.ACACIA_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            Blocks.ACACIA_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            Blocks.OAK_SLAB.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Gravel Blocks.
     */
    public static Map<BetterMineshaftStructure.Type, BlockState> GRAVEL_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            Blocks.GRAVEL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MESA,
            Blocks.RED_SAND.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            Blocks.GRAVEL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            Blocks.GRAVEL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.ICE,
            Blocks.GRAVEL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            Blocks.SAND.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            Blocks.RED_SAND.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            Blocks.GRAVEL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            Blocks.GRAVEL.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Stone Wall Blocks.
     */
    public static Map<BetterMineshaftStructure.Type, BlockState> STONE_WALL_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            Blocks.COBBLESTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MESA,
            Blocks.COBBLESTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            Blocks.COBBLESTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            Blocks.COBBLESTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.ICE,
            Blocks.COBBLESTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            Blocks.COBBLESTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            Blocks.COBBLESTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            Blocks.COBBLESTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            Blocks.COBBLESTONE_WALL.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Stone Slab Blocks.
     */
    public static Map<BetterMineshaftStructure.Type, BlockState> STONE_SLAB_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            Blocks.STONE_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructure.Type.MESA,
            Blocks.STONE_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
                Blocks.STONE_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            Blocks.STONE_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.ICE,
            Blocks.STONE_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            Blocks.STONE_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            Blocks.STONE_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            Blocks.STONE_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            Blocks.STONE_SLAB.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockState) data[1]));
}
