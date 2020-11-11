package com.hbn.outvoted.entities.hunger;

import com.hbn.outvoted.config.OutvotedConfig;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import software.bernie.geckolib.animation.builder.AnimationBuilder;
import software.bernie.geckolib.animation.controller.EntityAnimationController;
import software.bernie.geckolib.entity.IAnimatedEntity;
import software.bernie.geckolib.event.AnimationTestEvent;
import software.bernie.geckolib.event.ParticleKeyFrameEvent;
import software.bernie.geckolib.event.SoundKeyframeEvent;
import software.bernie.geckolib.manager.EntityAnimationManager;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HungerEntity extends CreatureEntity implements IAnimatedEntity {
    private static final DataParameter<Boolean> BURROWED = EntityDataManager.createKey(HungerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(HungerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ENCHANTING = EntityDataManager.createKey(HungerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(HungerEntity.class, DataSerializers.VARINT);
    private Map<Enchantment, Integer> storedEnchants = new ConcurrentHashMap<Enchantment, Integer>();

    EntityAnimationManager manager = new EntityAnimationManager();
    EntityAnimationController controller = new EntityAnimationController(this, "controller", 2, this::animationPredicate);

    private <E extends HungerEntity> boolean animationPredicate(AnimationTestEvent<E> event) {
        String animname = controller.getCurrentAnimation() != null ? controller.getCurrentAnimation().animationName : "";
        if (this.burrowed()) {
            if (this.enchanting()) {
                controller.setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.bite2").addAnimation("animation.great_hunger.bite2loop", true));
            } else {
                if (controller.getCurrentAnimation() != null) {
                    if (animname.equals("animation.great_hunger.idle") || animname.equals("animation.great_hunger.attacking") || animname.equals("animation.great_hunger.bite") || animname.equals("animation.great_hunger.burrow")) {
                        controller.setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.burrow").addAnimation("animation.great_hunger.burrow2", true));
                    } else {
                        controller.setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.burrow2"));
                    }
                } else {
                    controller.setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.burrow").addAnimation("animation.great_hunger.burrow2", true));
                }
            }
        } else {
            if (controller.getCurrentAnimation() == null || animname.equals("animation.great_hunger.idle") || animname.equals("animation.great_hunger.attacking")) {
                if (this.attacking()) {
                    controller.setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.attacking"));
                } else {
                    controller.setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.idle"));
                }
            } else {
                if (this.attacking()) {
                    controller.setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.bite").addAnimation("animation.great_hunger.attacking"));
                } else {
                    controller.setAnimation(new AnimationBuilder().addAnimation("animation.great_hunger.bite").addAnimation("animation.great_hunger.idle"));
                }
            }
        }
        return true;
    }

    private <E extends Entity> SoundEvent soundListener(SoundKeyframeEvent<E> event) {
        if (event.sound.equals("chomp")) {
            return SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH;
        } else if (event.sound.equals("dig")) {
            BlockState block = world.getBlockState(new BlockPos(this.getPosX(), this.getPosY() - 1, this.getPosZ()));
            if (block.isIn(BlockTags.SAND)) {
                return SoundEvents.BLOCK_SAND_BREAK;
            } else {
                return SoundEvents.BLOCK_GRAVEL_BREAK;
            }
        } else {
            return null;
        }
    }

    private <E extends Entity> IParticleData particleListener(ParticleKeyFrameEvent<E> event) {
        if (event.effect.equals("dig")) {
            for (int i = 0; i < 2; ++i) {
                BlockPos blockpos = new BlockPos(this.getPosX(), this.getPosY() - 0.5D, this.getPosZ());
                BlockState blockstate = this.world.getBlockState(blockpos);
                this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate), this.getPosXRandom(0.5D), this.getPosYHeight(0), this.getPosZRandom(0.5D), (this.rand.nextDouble() - 0.5D) * 2.0D, this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
        return null;
    }

    public HungerEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        manager.addAnimationController(controller);
        controller.registerSoundListener(this::soundListener);
        controller.registerParticleListener(this::particleListener);
        this.experienceValue = 5;
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return CreatureEntity.registerAttributes()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 15.0D)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK)
                .createMutableAttribute(Attributes.MAX_HEALTH, OutvotedConfig.COMMON.healthhunger.get())
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 15.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new HungerEntity.BiteGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new HungerEntity.BurrowGoal(this));
        this.goalSelector.addGoal(3, new HungerEntity.FindSpotGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, LivingEntity.class, true));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SILVERFISH_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(BURROWED, Boolean.FALSE);
        this.dataManager.register(ATTACKING, Boolean.FALSE);
        this.dataManager.register(ENCHANTING, Boolean.FALSE);
        this.dataManager.register(VARIANT, 2);
    }

    public void burrowed(boolean burrowed) {
        this.dataManager.set(BURROWED, burrowed);
    }

    public boolean burrowed() {
        return this.dataManager.get(BURROWED);
    }

    public void attacking(boolean attacking) {
        this.dataManager.set(ATTACKING, attacking);
    }

    public boolean attacking() {
        return this.dataManager.get(ATTACKING);
    }

    public void enchanting(boolean enchanting) {
        this.dataManager.set(ENCHANTING, enchanting);
    }

    public boolean enchanting() {
        return this.dataManager.get(ENCHANTING);
    }

    public int variant() {
        if (this.dataManager.get(VARIANT) == 2) {
            this.dataManager.set(VARIANT, this.world.getBiome(new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ())).getCategory() == Biome.Category.DESERT ? 0 : 1);
        }
        return this.dataManager.get(VARIANT);
    }

    public ItemStack modifyEnchantments(ItemStack stack, int damage, int count) {
        ItemStack itemstack = stack.copy();
        itemstack.removeChildTag("Enchantments");
        itemstack.removeChildTag("StoredEnchantments");
        if (damage > 0) {
            itemstack.setDamage(damage);
        } else {
            itemstack.removeChildTag("Damage");
        }
        if (storedEnchants.size() < OutvotedConfig.COMMON.max_enchants.get()) {
            itemstack.setCount(count);
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack).entrySet().stream().filter((p_217012_0_) -> {
                return !p_217012_0_.getKey().isCurse();
                //return true;
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (!map.isEmpty()) {
                for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                    Enchantment key = entry.getKey();
                    Integer value = entry.getValue();
                    if (storedEnchants.containsKey(key)) {
                        if (value == key.getMaxLevel() && storedEnchants.get(key) == key.getMaxLevel()) {
                            boolean isMax = false;
                            for (Map.Entry<Enchantment, Integer> stentry : storedEnchants.entrySet()) {
                                if (stentry.getValue() == (stentry.getKey().getMaxLevel())) {
                                    isMax = true;
                                }
                            }
                            if (isMax) {
                                storedEnchants.put(key, key.getMaxLevel() + 1);
                            } else {
                                itemstack = null;
                            }
                        } else {
                            storedEnchants.put(key, value);
                        }
                    } else {
                        if (storedEnchants.size() > 0) {
                            for (Enchantment ench : storedEnchants.keySet()) {
                                if (ench instanceof ProtectionEnchantment) {
                                    if (key instanceof ProtectionEnchantment) {
                                        if (!storedEnchants.isEmpty()) {
                                            if (key.isCompatibleWith(ench)) {
                                                storedEnchants.put(key, value);
                                            } else {
                                                itemstack = null;
                                                break;
                                            }
                                        } else {
                                            storedEnchants.put(key, value);
                                        }
                                    } else {
                                        storedEnchants.put(key, value);
                                    }
                                } else if (key.isCompatibleWith(ench)) {
                                    storedEnchants.put(key, value);
                                } else if (key instanceof InfinityEnchantment || key instanceof MendingEnchantment) {
                                    storedEnchants.put(key, value);
                                } else {
                                    itemstack = null;
                                    break;
                                }
                            }
                        } else {
                            storedEnchants.put(key, value);
                            itemstack = ItemStack.EMPTY;
                        }
                    }
                    //map = new HashMap<Enchantment, Integer>();
                }
            } else {
                map = storedEnchants;
                storedEnchants = new ConcurrentHashMap<Enchantment, Integer>();
            }

        /*if (map.equals(new HashMap<Enchantment, Integer>())) {
            if (Math.random() > 0.5) {
                EnchantmentHelper.addRandomEnchantment(new Random(), itemstack, 10, true);
            } else {
                itemstack = ItemStack.EMPTY;
            }
        }*/
            if (itemstack != null) {
                if (itemstack.getItem() == Items.ENCHANTED_BOOK) {
                    itemstack = ItemStack.EMPTY;
                } else if (itemstack.getItem() == Items.BOOK) {
                    itemstack = new ItemStack(Items.ENCHANTED_BOOK);
                }

                if (map.size() == 0) {
                    itemstack = ItemStack.EMPTY;
                }

                if (itemstack != ItemStack.EMPTY) {
                    EnchantmentHelper.setEnchantments(map, itemstack);
                    itemstack.setRepairCost(0);
                }
            }


        /*for (int i = 0; i < map.size(); ++i) {
            itemstack.setRepairCost(RepairContainer.getNewRepairCost(itemstack.getRepairCost()));
        }*/
        } else {
            itemstack = ItemStack.EMPTY;
        }

        return itemstack;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick() {
        if (this.isAlive()) {
            this.setInvulnerable(this.burrowed());
            this.setSilent(this.burrowed());
        }

        super.livingTick();
    }

    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
        return super.canSpawn(worldIn, spawnReasonIn) && this.getBlockState().isIn(BlockTags.BAMBOO_PLANTABLE_ON);
    }

    public boolean canBePushed() {
        return !this.isInvulnerable() && super.canBePushed();
    }

    static class BiteGoal extends MeleeAttackGoal {
        private final HungerEntity hunger;

        public BiteGoal(HungerEntity entityIn, double speedIn, boolean useMemory) {
            super(entityIn, speedIn, useMemory);
            this.hunger = entityIn;
        }

        public boolean shouldExecute() {
            boolean exec = super.shouldExecute();
            LivingEntity livingentity = this.hunger.getAttackTarget();
            if (livingentity != null && livingentity.isAlive() && this.hunger.canAttack(livingentity) && this.hunger.world.getDifficulty() != Difficulty.PEACEFUL) {
                this.hunger.attacking(!this.hunger.enchanting() && !this.hunger.burrowed());
                return !this.hunger.enchanting() && !this.hunger.burrowed() && exec;
            } else {
                this.hunger.attacking(false);
                return false;
            }
        }
    }

    public boolean isSuitable(HungerEntity entityIn) {
        World world = entityIn.world;
        double posX = entityIn.getPosX();
        double posY = entityIn.getPosY();
        double posZ = entityIn.getPosZ();
        boolean ret = true;
        for (double k = posX - 1; k <= posX + 1; ++k) {
            if (ret) {
                for (double l = posZ - 1; l <= posZ + 1; ++l) {
                    BlockState block = world.getBlockState(new BlockPos(k, posY - 1, l));
                    if (block.isIn(BlockTags.BAMBOO_PLANTABLE_ON) && !entityIn.isInWater()) {
                        if (ret) {
                            ret = !entityIn.getLeashed();
                        }
                    } else {
                        ret = false;
                        break;
                    }
                }
            } else {
                break;
            }
        }
        return ret;
    }

    static class FindSpotGoal extends WaterAvoidingRandomWalkingGoal {
        private final HungerEntity hunger;

        public FindSpotGoal(HungerEntity entityIn) {
            super(entityIn, 1.0D, 0.1F);
            this.hunger = entityIn;
        }

        public boolean shouldExecute() {
            if (!this.mustUpdate) {
                if (this.creature.getIdleTime() >= 100) {
                    return false;
                }

                if (this.creature.getRNG().nextInt(this.executionChance) != 0) {
                    return false;
                }
            }

            Vector3d vector3d = this.getPosition();
            if (vector3d == null) {
                return false;
            } else if (!this.hunger.burrowed() && !this.hunger.isSuitable(this.hunger)) {
                this.x = vector3d.x;
                this.y = vector3d.y;
                this.z = vector3d.z;
                this.mustUpdate = false;
                return true;
            }
            return super.shouldExecute();
        }

        public boolean shouldContinueExecuting() {
            return !this.hunger.burrowed() && !this.hunger.isSuitable(this.hunger) && !this.hunger.getNavigator().noPath() && !this.hunger.isBeingRidden();
        }
    }

    static class BurrowGoal extends Goal {
        private final HungerEntity hunger;
        private int tick = 0;
        private int tick2 = 0;
        private ItemStack cacheitem = ItemStack.EMPTY;
        private Entity cacheentity = null;

        public BurrowGoal(HungerEntity entityIn) {
            this.hunger = entityIn;
        }

        public void resetTask() {
            this.tick = 0;
            this.tick2 = 0;
            this.hunger.burrowed(false);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return !this.hunger.attacking() && this.hunger.isSuitable(this.hunger);
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return !this.hunger.attacking() && this.hunger.isSuitable(this.hunger);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */

        public void tick() {
            if (this.tick % 5 == 0) {
                boolean suitable = this.hunger.isSuitable(this.hunger);
                List<Entity> entities = this.hunger.world.getEntitiesWithinAABBExcludingEntity(this.hunger, this.hunger.getBoundingBox().expand(1.0D, 1.0D, 1.0D).expand(-1.0D, 1.0D, -1.D));
                if (!entities.isEmpty()) {
                    for (Entity entity : entities) {
                        double d0 = this.hunger.getDistanceSq(entity);
                        if (d0 < 1.2D) {
                            if (entity instanceof ItemEntity) {
                                ItemStack item = ((ItemEntity) entity).getItem();
                                if (item.getTag() != null) {
                                    if (!this.hunger.enchanting()) {
                                        if (!item.getTag().contains("Bitten")) {
                                            this.cacheitem = item.copy();
                                            this.cacheentity = entity;
                                            this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), SoundEvents.ENTITY_STRIDER_EAT, this.hunger.getSoundCategory(), 0.8F, 0.9F);
                                            entity.remove();
                                            this.hunger.enchanting(true);
                                        }
                                        this.hunger.burrowed(suitable);
                                    }
                                } else {
                                    this.hunger.burrowed(suitable);
                                }
                            } else if (entity instanceof LivingEntity && !this.hunger.enchanting()) {
                                if (entity.isAlive() && this.hunger.canAttack((LivingEntity) entity)) {
                                    if (!this.hunger.enchanting()) {
                                        this.hunger.burrowed(false);
                                        this.hunger.attacking(true);
                                        this.hunger.enchanting(false);
                                    }
                                }
                            } else {
                                this.hunger.burrowed(suitable);
                            }
                        } else {
                            this.hunger.burrowed(suitable);
                        }
                    }
                }
                if (this.cacheitem != ItemStack.EMPTY) {
                    this.tick2++;

                    if (this.tick2 % 4 == 0) {
                        ItemStack noench = this.hunger.modifyEnchantments(cacheitem, cacheitem.getDamage(), 1);
                        if (noench == null) {
                            noench = cacheitem;
                            this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), SoundEvents.ENTITY_FOX_SPIT, this.hunger.getSoundCategory(), 0.8F, 0.8F);
                            noench.getOrCreateChildTag("Bitten");
                            this.hunger.world.addEntity(new ItemEntity(cacheentity.world, cacheentity.getPosX(), cacheentity.getPosY(), cacheentity.getPosZ(), noench));
                        } else if (noench != ItemStack.EMPTY) {
                            this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, this.hunger.getSoundCategory(), 0.8F, 0.6F);
                            noench.getOrCreateChildTag("Bitten");
                            this.hunger.world.addEntity(new ItemEntity(cacheentity.world, cacheentity.getPosX(), cacheentity.getPosY(), cacheentity.getPosZ(), noench));
                        } else {
                            this.hunger.world.playSound(null, this.hunger.getPosX(), this.hunger.getPosY(), this.hunger.getPosZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, this.hunger.getSoundCategory(), 0.8F, 0.6F);
                        }
                        this.cacheitem = ItemStack.EMPTY;
                        this.tick2 = 0;
                    }
                    this.hunger.burrowed(true);
                    this.hunger.enchanting(true);
                } else {
                    this.hunger.burrowed(suitable);
                    this.hunger.enchanting(false);
                }
            }
            this.tick++;
            super.tick();
        }
    }

    @Override
    public EntityAnimationManager getAnimationManager() {
        return manager;
    }
}
