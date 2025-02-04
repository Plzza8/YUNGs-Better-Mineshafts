package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import com.yungnickyoung.minecraft.yungsapi.world.BoundingBoxHelper;
import net.minecraft.block.*;
import net.minecraft.entity.item.minecart.ChestMinecartEntity;
import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

public class BigTunnel extends MineshaftPiece {
    private final List<BlockPos> smallShaftLeftEntrances = Lists.newLinkedList();
    private final List<BlockPos> smallShaftRightEntrances = Lists.newLinkedList();
    private final List<MutableBoundingBox> sideRoomEntrances = Lists.newLinkedList();
    private final List<Integer> bigSupports = Lists.newLinkedList(); // local z coords
    private final List<Integer> smallSupports = Lists.newLinkedList(); // local z coords
    private final List<Pair<Integer, Integer>> gravelDeposits = Lists.newLinkedList(); // Pair<z coordinate, side> where side 0 = left, 1 = right
    private static final int
        SECONDARY_AXIS_LEN = 9,
        Y_AXIS_LEN = 8,
        MAIN_AXIS_LEN = 24;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public BigTunnel(TemplateManager structureManager, CompoundNBT compoundTag) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, compoundTag);

        ListNBT listTag1 = compoundTag.getList("SmallShaftLeftEntrances", 11);
        ListNBT listTag2 = compoundTag.getList("SmallShaftRightEntrances", 11);
        ListNBT listTag3 = compoundTag.getList("SideRoomEntrances", 11);
        ListNBT listTag4 = compoundTag.getList("BigSupports", 3);
        ListNBT listTag5 = compoundTag.getList("SmallSupports", 3);
        ListNBT listTag6 = compoundTag.getList("GravelDeposits", 11);

        for (int i = 0; i < listTag1.size(); ++i) {
            this.smallShaftLeftEntrances.add(new BlockPos(listTag1.getIntArray(i)[0], listTag1.getIntArray(i)[1], listTag1.getIntArray(i)[2]));
        }

        for (int i = 0; i < listTag2.size(); ++i) {
            this.smallShaftRightEntrances.add(new BlockPos(listTag2.getIntArray(i)[0], listTag2.getIntArray(i)[1], listTag2.getIntArray(i)[2]));
        }

        for (int i = 0; i < listTag3.size(); ++i) {
            this.sideRoomEntrances.add(new MutableBoundingBox(listTag3.getIntArray(i)));
        }

        for (int i = 0; i < listTag4.size(); ++i) {
            this.bigSupports.add(listTag4.getInt(i));
        }

        for (int i = 0; i < listTag5.size(); ++i) {
            this.smallSupports.add(listTag5.getInt(i));
        }

        for (int i = 0; i < listTag6.size(); ++i) {
            this.gravelDeposits.add(new Pair<>(listTag6.getIntArray(i)[0], listTag6.getIntArray(i)[1]));
        }
    }

    public BigTunnel(int chainLength, Random random, MutableBoundingBox blockBox, Direction direction, MineshaftVariantSettings settings) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, chainLength, settings);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tag) {
        super.toNbt(tag);
        ListNBT listTag1 = new ListNBT();
        ListNBT listTag2 = new ListNBT();
        ListNBT listTag3 = new ListNBT();
        ListNBT listTag4 = new ListNBT();
        ListNBT listTag5 = new ListNBT();
        ListNBT listTag6 = new ListNBT();
        smallShaftLeftEntrances.forEach(pos -> listTag1.add(new IntArrayNBT(new int[]{pos.getX(), pos.getY(), pos.getZ()})));
        smallShaftRightEntrances.forEach(pos -> listTag2.add(new IntArrayNBT(new int[]{pos.getX(), pos.getY(), pos.getZ()})));
        sideRoomEntrances.forEach(blockBox -> listTag3.add(blockBox.toNBTTagIntArray()));
        bigSupports.forEach(z -> listTag4.add(IntNBT.valueOf(z)));
        smallSupports.forEach(z -> listTag5.add(IntNBT.valueOf(z)));
        gravelDeposits.forEach(pair -> listTag6.add(new IntArrayNBT(new int[]{pair.getFirst(), pair.getSecond()})));
        tag.put("SmallShaftLeftEntrances", listTag1);
        tag.put("SmallShaftRightEntrances", listTag2);
        tag.put("SideRoomEntrances", listTag3);
        tag.put("BigSupports", listTag4);
        tag.put("SmallSupports", listTag5);
        tag.put("GravelDeposits", listTag6);
    }

    public static MutableBoundingBox determineBoxPosition(int x, int y, int z, Direction direction) {
        return BoundingBoxHelper.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);
    }

    @Override
    public void buildComponent(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        Direction direction = this.getCoordBaseMode();
        if (direction == null) {
            return;
        }

        // Extend big tunnel in same direction
        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, direction, chainLength);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ + 1, direction, chainLength);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ, direction, chainLength);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, direction, chainLength);
        }

        // Get the length of the main axis. This SHOULD be equal to the MAIN_AXIS_LEN variable.
        int pieceLen = this.getCoordBaseMode().getAxis() == Direction.Axis.Z ? this.boundingBox.getZSize() : this.boundingBox.getXSize();

        // Build side rooms
        buildSideRoomsLeft(structurePiece, list, random, direction, pieceLen);
        buildSideRoomsRight(structurePiece, list, random, direction, pieceLen);

        // Build small shafts and mark their entrances
        buildSmallShaftsLeft(structurePiece, list, random, direction, pieceLen);
        buildSmallShaftsRight(structurePiece, list, random, direction, pieceLen);

        // Decorations
        buildSupports(random);
        buildGravelDeposits(random);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator generator, Random random, MutableBoundingBox box, ChunkPos pos, BlockPos blockPos) {
        // Don't spawn if in ocean biome
        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return false;

        // Randomize blocks
        this.chanceReplaceNonAir(world, box, random, settings.replacementRate, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        // Randomize floor
        this.chanceReplaceNonAir(world, box, random, settings.replacementRate, 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, getFloorSelector());

        // Fill with air
        this.fill(world, box, 1, 1, 0, LOCAL_X_END - 1, LOCAL_Y_END - 3, LOCAL_Z_END, CAVE_AIR);
        this.fill(world, box, 2, LOCAL_Y_END - 3, 0, LOCAL_X_END - 2, LOCAL_Y_END - 2, LOCAL_Z_END, CAVE_AIR);
        this.fill(world, box, 3, LOCAL_Y_END - 1, 0, LOCAL_X_END - 3, LOCAL_Y_END - 1, LOCAL_Z_END, CAVE_AIR);

        // Fill in any air in floor with main block
        this.replaceAir(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END, getMainBlock());

        // Core structure
        generateSmallShaftEntrances(world, box, random);
        generateSideRoomOpenings(world, box, random);
        generateLegs(world, box, random);
        generateBigSupports(world, box, random);
        generateSmallSupports(world, box, random);

        // Decorations
        generateRails(world, box, random);
        generateChestCarts(world, box, random, LootTables.CHESTS_ABANDONED_MINESHAFT);
        generateTntCarts(world, box, random);
        generateGravelDeposits(world, box, random);
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, settings.vineChance, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);
        generateLanterns(world, box, random);
        generateCobwebs(world, box, random);

        return true;
    }

    private void generateLegs(ISeedReader world, MutableBoundingBox box, Random random) {
        if (settings.legVariant == 1) {
            generateLegsVariant1(world, box, random);
        } else {
            generateLegsVariant2(world, box, random);
        }
    }

    private void generateLegsVariant1(ISeedReader world, MutableBoundingBox box, Random random) {

        BlockState supportBlock = getSupportBlock();
        if (supportBlock.getBlock() instanceof WallBlock) {
            supportBlock = supportBlock.with(WallBlock.WALL_HEIGHT_NORTH, WallHeight.TALL).with(WallBlock.WALL_HEIGHT_SOUTH, WallHeight.TALL);
        } else if (supportBlock.getBlock() instanceof FourWayBlock) {
            supportBlock = supportBlock.with(FourWayBlock.NORTH, true).with(FourWayBlock.SOUTH, true);
        }

        // Left side
        generateLeg(world, random, 1, 0);
        this.replaceAir(world, box, 1, -1, 1, 1, -1, 5, supportBlock);
        this.replaceAir(world, box, 1, -2, 1, 1, -2, 3, supportBlock);
        this.replaceAir(world, box, 1, -3, 1, 1, -3, 2, supportBlock);
        this.replaceAir(world, box, 1, -5, 1, 1, -4, 1, supportBlock);
        this.replaceAir(world, box, 1, -1, 6, 1, -1, 10, supportBlock);
        this.replaceAir(world, box, 1, -2, 8, 1, -2, 10, supportBlock);
        this.replaceAir(world, box, 1, -3, 9, 1, -3, 10, supportBlock);
        this.replaceAir(world, box, 1, -5, 10, 1, -4, 10, supportBlock);
        generateLeg(world, random, 1, 11);
        generateLeg(world, random, 1, 12);
        this.replaceAir(world, box, 1, -1, 13, 1, -1, 17, supportBlock);
        this.replaceAir(world, box, 1, -2, 13, 1, -2, 15, supportBlock);
        this.replaceAir(world, box, 1, -3, 13, 1, -3, 14, supportBlock);
        this.replaceAir(world, box, 1, -5, 13, 1, -4, 13, supportBlock);
        this.replaceAir(world, box, 1, -1, 18, 1, -1, 22, supportBlock);
        this.replaceAir(world, box, 1, -2, 20, 1, -2, 22, supportBlock);
        this.replaceAir(world, box, 1, -3, 21, 1, -3, 22, supportBlock);
        this.replaceAir(world, box, 1, -5, 22, 1, -4, 22, supportBlock);
        generateLeg(world, random, 1, LOCAL_Z_END);

        // Right side
        generateLeg(world, random, LOCAL_X_END - 1, 0);
        this.replaceAir(world, box, LOCAL_X_END - 1, -1, 1, LOCAL_X_END - 1, -1, 5, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -2, 1, LOCAL_X_END - 1, -2, 3, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -3, 1, LOCAL_X_END - 1, -3, 2, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -5, 1, LOCAL_X_END - 1, -4, 1, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -1, 6, LOCAL_X_END - 1, -1, 10, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -2, 8, LOCAL_X_END - 1, -2, 10, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -3, 9, LOCAL_X_END - 1, -3, 10, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -5, 10, LOCAL_X_END - 1, -4, 10, supportBlock);
        generateLeg(world, random, LOCAL_X_END - 1, 11);
        generateLeg(world, random, LOCAL_X_END - 1, 12);
        this.replaceAir(world, box, LOCAL_X_END - 1, -1, 13, LOCAL_X_END - 1, -1, 17, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -2, 13, LOCAL_X_END - 1, -2, 15, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -3, 13, LOCAL_X_END - 1, -3, 14, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -5, 13, LOCAL_X_END - 1, -4, 13, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -1, 18, LOCAL_X_END - 1, -1, 22, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -2, 20, LOCAL_X_END - 1, -2, 22, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -3, 21, LOCAL_X_END - 1, -3, 22, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -5, 22, LOCAL_X_END - 1, -4, 22, supportBlock);
        generateLeg(world, random, LOCAL_X_END - 1, LOCAL_Z_END);
    }

    private void generateLegsVariant2(ISeedReader world, MutableBoundingBox box, Random random) {
        BlockSetSelector selector = getLegSelector();
        for (int z = 0; z <= LOCAL_Z_END; z += 7) {
            generateLeg(world, random, 2, z + 1);
            generateLeg(world, random, LOCAL_X_END - 2, z + 1);

            this.replaceAir(world, box, random, 1, -1, z, LOCAL_X_END - 1, -1, z + 2, selector);

            this.replaceAir(world, box, random, 2, -1, z + 3, 2, -1, z + 3, selector);
            this.replaceAir(world, box, random, LOCAL_X_END - 2, -1, z + 3, LOCAL_X_END - 2, -1, z + 3, selector);

            this.replaceAir(world, box, random, 3, -1, z + 3, LOCAL_X_END - 3, -1, z + 6, selector);

            this.replaceAir(world, box, random, 2, -1, z + 6, 2, -1, z + 6, selector);
            this.replaceAir(world, box, random, LOCAL_X_END - 2, -1, z + 6, LOCAL_X_END - 2, -1, z + 6, selector);

            this.replaceAir(world, box, random, 2, -2, z, 2, -2, z, selector);
            this.replaceAir(world, box, random, LOCAL_X_END - 2, -2, z, LOCAL_X_END - 2, -2, z, selector);

            this.replaceAir(world, box, random, 2, -2, z + 2, 2, -2, z + 2, selector);
            this.replaceAir(world, box, random, LOCAL_X_END - 2, -2, z + 2, LOCAL_X_END - 2, -2, z + 2, selector);

            this.replaceAir(world, box, random, 1, -2, z + 1, 1, -2, z + 1, selector);
            this.replaceAir(world, box, random, LOCAL_X_END - 1, -2, z + 1, LOCAL_X_END - 1, -2, z + 1, selector);

            this.replaceAir(world, box, random, 3, -2, z + 1, 3, -2, z + 1, selector);
            this.replaceAir(world, box, random, LOCAL_X_END - 3, -2, z + 1, LOCAL_X_END - 3, -2, z + 1, selector);
        }
    }

    private void generateGravelDeposits(ISeedReader world, MutableBoundingBox box, Random random) {
        gravelDeposits.forEach(pair -> {
            int z = pair.getFirst();
            int side = pair.getSecond();
            switch (side) {
                case 0: // Left side
                default:
                    // Row closest to wall
                    this.replaceAir(world, box, 1, 1, z, 1, 2, z + 2, getGravel());
                    this.replaceAir(world, box, 1, 3, z + 1, 1, 3 + random.nextInt(2), z + 1, getGravel());
                    this.chanceReplaceAir(world, box, random, .5f, 1, 3, z, 1, 3, z + 2, getGravel());
                    // Middle row
                    this.replaceAir(world, box, 2, 1, z + 1, 2, 2 + random.nextInt(2), z + 1, getGravel());
                    this.replaceAir(world, box, 2, 1, z, 2, 1 + random.nextInt(2), z + 2, getGravel());
                    // Innermost row
                    this.chanceReplaceAir(world, box, random, .5f, 3, 1, z, 3, 1, z + 2, getGravel());
                    break;
                case 1: // Right side
                    // Row closest to wall
                    this.replaceAir(world, box, LOCAL_X_END - 1, 1, z, LOCAL_X_END - 1, 2, z + 2, getGravel());
                    this.replaceAir(world, box, LOCAL_X_END - 1, 3, z + 1, LOCAL_X_END - 1, 3 + random.nextInt(2), z + 1, getGravel());
                    this.chanceReplaceAir(world, box, random, .5f, LOCAL_X_END - 1, 3, z, LOCAL_X_END - 1, 3, z + 2, getGravel());
                    // Middle row
                    this.replaceAir(world, box, LOCAL_X_END - 2, 1, z + 1, LOCAL_X_END - 2, 2 + random.nextInt(2), z + 1, getGravel());
                    this.replaceAir(world, box, LOCAL_X_END - 2, 1, z, LOCAL_X_END - 2, 1 + random.nextInt(2), z + 2, getGravel());
                    // Innermost row
                    this.chanceReplaceAir(world, box, random, .5f, LOCAL_X_END - 3, 1, z, LOCAL_X_END - 3, 1, z + 2, getGravel());
            }
        });
    }

    private void generateChestCarts(ISeedReader world, MutableBoundingBox box, Random random, ResourceLocation lootTableId) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextFloat() < Configuration.spawnRates.mainShaftChestMinecartSpawnRate.get()) {
                BlockPos blockPos = new BlockPos(this.getXWithOffset(LOCAL_X_END / 2, z), getYWithOffset(1), this.getZWithOffset(LOCAL_X_END / 2, z));
                if (box.isVecInside(blockPos) && !world.getBlockState(blockPos.down()).isAir()) {
                    ChestMinecartEntity chestMinecartEntity = new ChestMinecartEntity(world.getWorld(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    chestMinecartEntity.setLootTable(lootTableId, random.nextLong());
                    world.addEntity(chestMinecartEntity);
                }
            }
        }
    }

    private void generateTntCarts(ISeedReader world, MutableBoundingBox box, Random random) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextInt(400) == 0) {
                BlockPos blockPos = new BlockPos(this.getXWithOffset(LOCAL_X_END / 2, z), getYWithOffset(1), this.getZWithOffset(LOCAL_X_END / 2, z));
                if (box.isVecInside(blockPos) && !world.getBlockState(blockPos.down()).isAir()) {
                    TNTMinecartEntity tntMinecartEntity = new TNTMinecartEntity(world.getWorld(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    world.addEntity(tntMinecartEntity);
                }
            }
        }
    }

    private void generateBigSupports(ISeedReader world, MutableBoundingBox box, Random random) {
        BlockState supportBlock = getSupportBlock();
        if (supportBlock.getBlock() instanceof WallBlock) {
            supportBlock = supportBlock.with(WallBlock.WALL_HEIGHT_EAST, WallHeight.TALL).with(WallBlock.WALL_HEIGHT_WEST, WallHeight.TALL);
        } else if (supportBlock.getBlock() instanceof FourWayBlock) {
            supportBlock = supportBlock.with(FourWayBlock.EAST, true).with(FourWayBlock.WEST, true);
        }

        for (int z : bigSupports) {
            // Bottom slabs
            this.chanceFill(world, box, random, .6f, 1, 1, z, 2, 1, z + 2, getMainSlab());
            this.chanceFill(world, box, random, .6f, LOCAL_X_END - 2, 1, z, LOCAL_X_END - 1, 1, z + 2, getMainSlab());
            // Main blocks
            this.setBlockState(world, getMainBlock(), 1, 1, z + 1, box);
            this.setBlockState(world, getMainBlock(), LOCAL_X_END - 1, 1, z + 1, box);
            this.setBlockState(world, getMainBlock(), 1, 4, z + 1, box);
            this.setBlockState(world, getMainBlock(), LOCAL_X_END - 1, 4, z + 1, box);
            this.fill(world, box, 2, 5, z + 1, LOCAL_X_END - 2, 5, z + 1, getMainBlock());
            // Supports
            this.fill(world, box, 1, 2, z + 1, 1, 3, z + 1, getSupportBlock());
            this.fill(world, box, LOCAL_X_END - 1, 2, z + 1, LOCAL_X_END - 1, 3, z + 1, getSupportBlock());
            this.chanceReplaceNonAir(world, box, random, .4f, 2, 5, z + 1, LOCAL_X_END - 2, 5, z + 1, supportBlock);
        }
    }

    private void generateSmallSupports(ISeedReader world, MutableBoundingBox box, Random random) {
        BlockState supportBlock = getSupportBlock();
        if (supportBlock.getBlock() instanceof WallBlock) {
            supportBlock = supportBlock.with(WallBlock.WALL_HEIGHT_EAST, WallHeight.TALL).with(WallBlock.WALL_HEIGHT_WEST, WallHeight.TALL);
        } else if (supportBlock.getBlock() instanceof FourWayBlock) {
            supportBlock = supportBlock.with(FourWayBlock.EAST, true).with(FourWayBlock.WEST, true);
        }

        for (int z : smallSupports) {
            this.setBlockState(world, getMainBlock(), 2, 1, z, box);
            this.setBlockState(world, getMainBlock(), LOCAL_X_END - 2, 1, z, box);
            this.setBlockState(world, getSupportBlock(), 2, 2, z, box);
            this.setBlockState(world, getSupportBlock(), LOCAL_X_END - 2, 2, z, box);
            this.setBlockState(world, getMainBlock(), 2, 3, z, box);
            this.setBlockState(world, getMainBlock(), LOCAL_X_END - 2, 3, z, box);
            this.fill(world, box, 3, 4, z, LOCAL_X_END - 3, 4, z, getMainBlock());
            this.chanceReplaceNonAir(world, box, random, .5f, 3, 4, z, LOCAL_X_END - 3, 4, z, supportBlock);
            this.chanceFill(world, box, random, .4f, 2, 3, z, LOCAL_X_END - 2, 3, z, supportBlock);
            this.setBlockState(world, supportBlock, 3, 3, z, box);
            this.setBlockState(world, supportBlock, LOCAL_X_END - 3, 3, z, box);
        }
    }

    private void generateLanterns(ISeedReader world, MutableBoundingBox box, Random random) {
        BlockState LANTERN = Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, true);
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            for (int x = 3; x <= LOCAL_X_END - 3; x++) {
                if (random.nextFloat() < Configuration.spawnRates.lanternSpawnRate.get()) {
                    if (!this.getBlockStateFromPos(world, x, LOCAL_Y_END, z, box).isAir()) {
                        this.setBlockState(world, LANTERN, x, LOCAL_Y_END - 1, z, box);
                        z += 20;
                    }
                }
            }
        }
    }

    private void generateCobwebs(ISeedReader world, MutableBoundingBox box, Random random) {
        float chance = Configuration.spawnRates.cobwebSpawnRate.get().floatValue();
        smallSupports.forEach(z -> {
            this.chanceReplaceAir(world, box, random, chance, 2, 3, z - 1, LOCAL_X_END - 2, 4, z + 1, Blocks.COBWEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, chance, 3, 5, z, LOCAL_X_END - 3, 5, z, Blocks.COBWEB.getDefaultState());
        });

        bigSupports.forEach(z -> {
            this.chanceReplaceAir(world, box, random, chance, 1, 1, z, 1,  4, z + 2, Blocks.COBWEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, chance, LOCAL_X_END - 1, 1, z, LOCAL_X_END - 1,  4, z + 2, Blocks.COBWEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, chance, 2, 5, z, LOCAL_X_END - 2,  5, z + 2, Blocks.COBWEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, chance, 2, 4, z + 1, LOCAL_X_END - 2,  4, z + 1, Blocks.COBWEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, chance, 3, 6, z + 1, LOCAL_X_END - 3,  6, z + 1, Blocks.COBWEB.getDefaultState());
        });
    }

    private void generateRails(ISeedReader world, MutableBoundingBox box, Random random) {
        // Place rails in center
        this.chanceFill(world, box, random, .5f, LOCAL_X_END / 2, 1, 0, LOCAL_X_END / 2, 1, LOCAL_Z_END, Blocks.RAIL.getDefaultState());
        // Place powered rails
        int blocksSinceLastRail = 0;
        for (int n = 0; n <= LOCAL_Z_END; n++) {
            blocksSinceLastRail++;
            if (random.nextInt(20) == 0 || blocksSinceLastRail > 25) {
                this.setBlockState(world, Blocks.POWERED_RAIL.getDefaultState().with(PoweredRailBlock.POWERED, true), LOCAL_X_END / 2, 1, n, box);
                blocksSinceLastRail = 0; // reset counter
            }
        }
    }

    private void generateSmallShaftEntrances (ISeedReader world, MutableBoundingBox box, Random random) {
        smallShaftLeftEntrances.forEach(entrancePos -> {
            int x = entrancePos.getX();
            int y = entrancePos.getY();
            int z = entrancePos.getZ();

            this.fill(world, box, x, y, z, x, y, z + 2, CAVE_AIR);
            this.setBlockState(world, getSupportBlock(), x, y + 1, z, box);
            this.setBlockState(world, getSupportBlock(), x, y + 1, z + 2, box);
            this.fill(world, box, x + 1, y, z, x + 1, y + 1, z, getSupportBlock());
            this.fill(world, box, x + 1, y, z + 2, x + 1, y + 1, z + 2, getSupportBlock());
            this.chanceFill(world, box, random, .75f, x, y + 2, z, x + 1, y + 2, z + 2, getMainBlock());
            this.fill(world, box, x, y + 1, z + 1, x + 1, y + 1, z + 1, CAVE_AIR);
            this.replaceAir(world, box, x, y - 1, z, x + 1, y - 1, z + 2, getMainBlock());
        });

        smallShaftRightEntrances.forEach(entrancePos -> {
            int x = entrancePos.getX();
            int y = entrancePos.getY();
            int z = entrancePos.getZ();

            this.replaceAir(world, box, x, y - 1, z, x + 1, y - 1, z + 2, getMainBlock());
            this.fill(world, box, x + 1, y, z, x + 1, y, z + 2, CAVE_AIR);
            this.setBlockState(world, getSupportBlock(), x + 1, y + 1, z, box);
            this.setBlockState(world, getSupportBlock(), x + 1, y + 1, z + 2, box);
            this.fill(world, box, x, y, z, x, y + 1, z, getSupportBlock());
            this.fill(world, box, x, y, z + 2, x, y + 1, z + 2, getSupportBlock());
            this.chanceFill(world, box, random, .75f, x, y + 2, z, x + 1, y + 2, z + 2, getMainBlock());
            this.fill(world, box, x, y + 1, z + 1, x + 1, y + 1, z + 1, CAVE_AIR);
        });
    }

    private void generateSideRoomOpenings(ISeedReader world, MutableBoundingBox box, Random random) {
        sideRoomEntrances.forEach(entranceBox -> {
            // Ensure floor in gap between tunnel and room
            this.replaceAir(world, box, random, entranceBox.minX, 0, entranceBox.minZ, entranceBox.maxX, 0, entranceBox.maxZ, getBrickSelector());
            switch (random.nextInt(3)) {
                case 0:
                    // Completely open
                    this.fill(world, box, entranceBox.minX, entranceBox.minY, entranceBox.minZ + 2, entranceBox.maxX, entranceBox.maxY, entranceBox.maxZ - 2, CAVE_AIR);
                    return;
                case 1:
                    // A few columns for openings
                    this.fill(world, box, entranceBox.minX, entranceBox.minY, entranceBox.minZ + 2, entranceBox.maxX, entranceBox.maxY - 1, entranceBox.minZ + 2, CAVE_AIR);
                    this.fill(world, box, entranceBox.minX, entranceBox.minY, entranceBox.minZ + 4, entranceBox.maxX, entranceBox.maxY - 1, entranceBox.minZ + 5, CAVE_AIR);
                    this.fill(world, box, entranceBox.minX, entranceBox.minY, entranceBox.minZ + 7, entranceBox.maxX, entranceBox.maxY - 1, entranceBox.minZ + 7, CAVE_AIR);
                    return;
                case 2:
                    // No openings - random block removal will expose these, probably
            }
        });
    }

    private void buildGravelDeposits(Random random) {
        for (int z = 0; z <= LOCAL_Z_END - 2; z++) {
            int r = random.nextInt(20);
            int currPos = z;
            if (r == 0) { // Left side
                gravelDeposits.add(new Pair<>(currPos, 0));
                z += 5;
            }
            else if (r == 1) { // Right side
                gravelDeposits.add(new Pair<>(currPos, 1));
                z += 5;
            }
        }
    }

    private void buildSupports(Random random) {
        int counter = 0;
        final int MAX_COUNT = 10; // max number of blocks before force spawning a support

        for (int z = 0; z <= LOCAL_Z_END - 2; z++) {
            counter++;

            // Make sure we arent overlapping with small shaft entrances
            boolean blockingEntrance = false;
            for (BlockPos entrancePos : smallShaftLeftEntrances) {
                if (entrancePos.getZ() <= z + 2 && z <= entrancePos.getZ() + 2) {
                    blockingEntrance = true;
                    break;
                }
            }
            for (BlockPos entrancePos : smallShaftRightEntrances) {
                if (entrancePos.getZ() <= z + 2 && z <= entrancePos.getZ() + 2) {
                    blockingEntrance = true;
                    break;
                }
            }
            if (blockingEntrance) continue;

            int r = random.nextInt(8);
            if (r == 0 || counter >= MAX_COUNT) { // Big support
                bigSupports.add(z);
                counter = 0;
                z += 3;
            }
            else if (r == 1) { // Small support
                smallSupports.add(z);
                counter = 0;
                z += 3;
            }
        }
    }

    private void buildSideRoomsLeft(StructurePiece structurePiece, List<StructurePiece> list, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 0; n < (pieceLen - 1) - 10; n++) {
            if (random.nextFloat() < Configuration.spawnRates.workstationSpawnRate.get()) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.minX - 5, this.boundingBox.minY, this.boundingBox.maxZ - n - 9, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new MutableBoundingBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.maxX + 5, this.boundingBox.minY, this.boundingBox.minZ + n + 9, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new MutableBoundingBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.maxX - n - 9, this.boundingBox.minY, this.boundingBox.maxZ + 5, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new MutableBoundingBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.minX + n + 9, this.boundingBox.minY, this.boundingBox.minZ - 5, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new MutableBoundingBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                }
                n += 10;
            }
        }
    }

    private void buildSideRoomsRight(StructurePiece structurePiece, List<StructurePiece> list, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 0; n < (pieceLen - 1) - 10; n++) {
            if (random.nextFloat() < Configuration.spawnRates.workstationSpawnRate.get()) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.maxX + 5, this.boundingBox.minY, this.boundingBox.maxZ - n, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new MutableBoundingBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.minX - 5, this.boundingBox.minY, this.boundingBox.minZ + n, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new MutableBoundingBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.maxX - n, this.boundingBox.minY, this.boundingBox.minZ - 5, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new MutableBoundingBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.minX + n, this.boundingBox.minY, this.boundingBox.maxZ + 5, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new MutableBoundingBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                }
                n += 10;
            }
        }
    }

    private void buildSmallShaftsLeft(StructurePiece structurePiece, List<StructurePiece> list, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 0; n < (pieceLen - 1) - 4; n++) {
            if (random.nextFloat() < Configuration.spawnRates.smallShaftSpawnRate.get()) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - n, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n + 1));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + n, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n + 1));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX - n, this.boundingBox.minY, this.boundingBox.maxZ + 1, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n + 1));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX + n, this.boundingBox.minY, this.boundingBox.minZ - 1, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n + 1));
                        }
                        break;
                }

                n += random.nextInt(7) + 5;
            }
        }
    }

    private void buildSmallShaftsRight(StructurePiece structurePiece, List<StructurePiece> list, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 5; n < pieceLen; n++) {
            if (random.nextFloat() < Configuration.spawnRates.smallShaftSpawnRate.get()) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - n, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n - 3));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + n, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n - 3));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX - n, this.boundingBox.minY, this.boundingBox.minZ - 1, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n - 3));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX + n, this.boundingBox.minY, this.boundingBox.maxZ + 1, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n - 3));
                        }
                        break;
                }

                n += random.nextInt(7) + 5;
            }
        }
    }
}