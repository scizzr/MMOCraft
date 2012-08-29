package com.scizzr.bukkit.mmocraft.effects2;

import java.util.Random;

public class RandomPitchSoundEffect extends SoundEffect {
    private final float randomPitch;
    
    public RandomPitchSoundEffect(SoundEffect sound, float randomPitch) {
        super(sound, sound.getDefaultVolume(), sound.getDefaultPitch());
        this.randomPitch = randomPitch;
    }
    
    @Override
    public SoundEffect adjust(float volume, float pitch) {
        return new RandomPitchSoundEffect(super.adjust(volume, pitch), this.randomPitch);
    }
    
    @Override
    public SoundEffect randomPitch(float amount) {
        return new RandomPitchSoundEffect(this, this.randomPitch + amount);
    }
    
    @Override
    public float getDefaultPitch() {
        Random random = new Random();
        return super.getDefaultPitch() + random.nextFloat() * this.randomPitch;
    }
}
