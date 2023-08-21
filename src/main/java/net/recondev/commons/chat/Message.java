package net.recondev.commons.chat;


import net.recondev.commons.builders.PlaceholderReplacer;
import net.recondev.commons.support.XSound;
import net.recondev.commons.utils.ColorUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
@SuppressWarnings("unused")
public class Message {
    private boolean soundEnabled;
    private boolean titleEnabled;
    private boolean messageEnabled;
    private String sound;
    private String actionBar;
    private String title;
    private String subTitle;
    private int volume;
    private int pitch;
    private int fadeInTicks;
    private int stayTicks;
    private int fadeOutTicks;
    private List<String> messages;

    public Message(final MessageCache messageCache, final String path) {
        final FileConfiguration config = messageCache.getConfig();
        this.soundEnabled = config.getBoolean(path + ".Sound.Enabled", false);
        this.sound = config.getString(path + ".Sound.Value", "ENTITY_PLAYER_LEVELUP");
        this.volume = config.getInt(path + ".Sound.Volume", 1);
        this.pitch = config.getInt(path + ".Sound.Pitch", 1);
        this.titleEnabled = config.getBoolean(path + ".Title.Enabled", false);
        this.title = config.getString(path + ".Title.Title", "");
        this.subTitle = config.getString(path + ".Title.Sub-Title");
        this.fadeInTicks = config.getInt(path + ".Title.Advanced.Fade-In-Ticks", 20);
        this.stayTicks = config.getInt(path + ".Title.Advanced.Stay-Ticks", 20);
        this.fadeOutTicks = config.getInt(path + ".Title.Advanced.Fade-Out-Ticks", 20);
        this.messageEnabled = config.getBoolean(path + ".Message.Enabled", false);
        this.messages = config.getStringList(path + ".Message.Value");
    }

    public Message(final FileConfiguration config, final String path) {
        this.soundEnabled = config.getBoolean(path + ".Sound.Enabled", false);
        this.sound = config.getString(path + ".Sound.Value", "ENTITY_PLAYER_LEVELUP");
        this.volume = config.getInt(path + ".Sound.Volume", 1);
        this.pitch = config.getInt(path + ".Sound.Pitch", 1);
        this.title = config.getString(path + ".Title.Title", "");
        this.subTitle = config.getString(path + ".Title.Sub-Title");
        this.fadeInTicks = config.getInt(path + ".Title.Advanced.Fade-In-Ticks", 20);
        this.stayTicks = config.getInt(path + ".Title.Advanced.Stay-Ticks", 20);
        this.fadeOutTicks = config.getInt(path + ".Title.Advanced.Fade-Out-Ticks", 20);
        this.messageEnabled = config.getBoolean(path + ".Message.Enabled", false);
        this.messages = config.getStringList(path + ".Message.Value");
    }

    public void send (final CommandSender sender) {
        this.send(sender, new PlaceholderReplacer());
    }

    public void send(final CommandSender sender, final PlaceholderReplacer placeholders) {
        if (this.messageEnabled) {
            for (final String message : this.messages) {
                sender.sendMessage(placeholders.parse((OfflinePlayer) sender, ColorUtil.colorize(message)));
            }
        }
        if (!(sender instanceof Player)) {
            return;
        }
        final Player player = (Player) sender;
        if (this.soundEnabled) {
            final Optional<XSound> xSoundOptional = XSound.matchXSound(this.sound);
            xSoundOptional.ifPresent(xSound -> xSound.playSound((Player) sender, (float) this.volume, (float) this.pitch));
        }
        if (this.titleEnabled) {
            Title.sendTitle(player, this.fadeInTicks, this.stayTicks, this.fadeOutTicks, placeholders.parse(this.title), placeholders.parse(this.subTitle));
        }
    }

    public boolean isSoundEnabled() {
        return this.soundEnabled;
    }

    public boolean isTitleEnabled() {
        return this.titleEnabled;
    }

    public boolean isMessageEnabled() {
        return this.messageEnabled;
    }

    public String getSound() {
        return this.sound;
    }

    public String getActionBar() {
        return this.actionBar;
    }

    public String getTitle() {
        return this.title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public int getVolume() {
        return this.volume;
    }

    public int getPitch() {
        return this.pitch;
    }

    public int getFadeInTicks() {
        return this.fadeInTicks;
    }

    public int getStayTicks() {
        return this.stayTicks;
    }

    public int getFadeOutTicks() {
        return this.fadeOutTicks;
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public void setSoundEnabled(final boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public void setTitleEnabled(final boolean titleEnabled) {
        this.titleEnabled = titleEnabled;
    }

    public void setMessageEnabled(final boolean messageEnabled) {
        this.messageEnabled = messageEnabled;
    }

    public void setSound(final String sound) {
        this.sound = sound;
    }

    public void setActionBar(final String actionBar) {
        this.actionBar = actionBar;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setSubTitle(final String subTitle) {
        this.subTitle = subTitle;
    }

    public void setVolume(final int volume) {
        this.volume = volume;
    }

    public void setPitch(final int pitch) {
        this.pitch = pitch;
    }

    public void setFadeInTicks(final int fadeInTicks) {
        this.fadeInTicks = fadeInTicks;
    }

    public void setStayTicks(final int stayTicks) {
        this.stayTicks = stayTicks;
    }

    public void setFadeOutTicks(final int fadeOutTicks) {
        this.fadeOutTicks = fadeOutTicks;
    }

    public void setMessages(final List<String> messages) {
        this.messages = messages;
    }

}