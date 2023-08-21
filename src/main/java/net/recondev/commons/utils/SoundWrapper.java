package net.recondev.commons.utils;


import net.recondev.commons.support.XSound;
import org.bukkit.entity.Player;

public class SoundWrapper {
    private final XSound sound;
    private final int volume;
    private final int pitch;

    public SoundWrapper(final XSound sound, final int volume, final int pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void sendSound(final Player player) {
        this.sound.playSound(player, (float)this.volume, (float)this.pitch);
    }

    public XSound getSound() {
        return this.sound;
    }

    public int getVolume() {
        return this.volume;
    }

    public int getPitch() {
        return this.pitch;
    }
}

