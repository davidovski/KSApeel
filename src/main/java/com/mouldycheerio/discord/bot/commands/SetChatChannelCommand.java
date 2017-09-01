package com.mouldycheerio.discord.bot.commands;

import com.mouldycheerio.discord.bot.MyBot;
import com.mouldycheerio.discord.bot.PeelingUtils;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

public class SetChatChannelCommand extends BotCommand {
    public SetChatChannelCommand() {
        setName("setChatChannel");
        setDescription(new CommandDescription("setChatChannel", "Set the chatting channel.", "setChatChannel #channel"));
    }

    public void onCommand(MyBot bot, IDiscordClient client, IMessage commandMessage, String[] args) {
        if (commandMessage.getAuthor().getStringID().equals(commandMessage.getGuild().getOwner().getStringID()) || commandMessage.getAuthor().getPermissionsForGuild(commandMessage.getGuild()).contains(Permissions.ADMINISTRATOR)) {
            IChannel ch = PeelingUtils.channelMentionToId(args[1], commandMessage.getGuild());
            bot.getChatchannels().put(ch.getStringID());
            commandMessage.reply("oki, I will reply to all messages there.");
            bot.saveAll();
        }

    }
}
