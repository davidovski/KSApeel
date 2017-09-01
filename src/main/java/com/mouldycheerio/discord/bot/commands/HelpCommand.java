package com.mouldycheerio.discord.bot.commands;

import java.util.List;

import com.mouldycheerio.discord.bot.MyBot;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;

public class HelpCommand extends BotCommand {
    private List<Command> commands;

    public HelpCommand(List<Command> commands) {
        this.commands = commands;
        setName("help");
        setDescription(new CommandDescription("Help", "Display this message", "help"));
    }

    public void onCommand(MyBot orangepeel, IDiscordClient client, IMessage commandMessage, String[] args) {
        String stringID = commandMessage.getAuthor().getStringID();
        commandMessage.reply("I have left a message in your PMs.");

        IPrivateChannel pm = client.getOrCreatePMChannel(commandMessage.getAuthor());
        String message = "";
        message = message + "``` KSA Peel```\n";
        message = message + "`by davidovski`\n";
        message = message + "***__Commands__***\n\n";
        for (Command c : commands) {
            message = message + c.getDescription().toString() + "\n";
        }
        message = message + "https://discord.gg/zuFHJRN";
        pm.sendMessage(message);
        // https://discordapp.com/oauth2/authorize?client_id=306115875784622080&scope=bot
    }
}
