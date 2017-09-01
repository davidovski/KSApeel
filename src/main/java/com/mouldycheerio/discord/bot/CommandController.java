package com.mouldycheerio.discord.bot;

import java.util.ArrayList;
import java.util.List;

import com.mouldycheerio.discord.bot.commands.AddCustomCommand;
import com.mouldycheerio.discord.bot.commands.AdminCommand;
import com.mouldycheerio.discord.bot.commands.Command;
import com.mouldycheerio.discord.bot.commands.HelpCommand;
import com.mouldycheerio.discord.bot.commands.HeyCommand;
import com.mouldycheerio.discord.bot.commands.ResponseTimeCommand;
import com.mouldycheerio.discord.bot.commands.ServersCommand;
import com.mouldycheerio.discord.bot.commands.SetChatChannelCommand;
import com.mouldycheerio.discord.bot.commands.SetPlayingTextCommand;
import com.mouldycheerio.discord.bot.commands.SimpleCustomCmd;
import com.mouldycheerio.discord.bot.commands.UpTimeCommand;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandController {
    private List<Command> commands;
    private MyBot myBot;
    private SimpleCustomCmd toadd;

    public CommandController(MyBot myBot) {
        this.myBot = myBot;
        commands = new ArrayList<Command>();
        commands.add(new UpTimeCommand());
        commands.add(new ResponseTimeCommand());
        commands.add(new ServersCommand());
        commands.add(new AddCustomCommand());
        commands.add(new HeyCommand());
        commands.add(new SetChatChannelCommand());
        commands.add(new SetPlayingTextCommand());
        commands.add(new HelpCommand(commands));

    }

    public void onMessageReceivedEvent(MessageReceivedEvent event, String prefix) {
        String msg = event.getMessage().getContent();
        String[] parts = msg.split(" ");
        String commandname = parts[0].substring(prefix.length());
        for (Command c : commands) {
            if (commandname.equalsIgnoreCase(c.getName())) {
                if (c instanceof AdminCommand) {
                    if (myBot.getAdmins().has(event.getAuthor().getStringID())) {
                        if (((AdminCommand) c).getCommandlvl() <= myBot.getAdmins().getInt(event.getAuthor().getStringID())) {
                            c.onCommand(myBot, myBot.getClient(), event.getMessage(), parts);
                        } else {
                            event.getMessage().reply(((AdminCommand) c).getNoPermText());
                        }
                    } else {
                        event.getMessage().reply(((AdminCommand) c).getNoPermText());

                    }
                } else {
                    c.onCommand(myBot, myBot.getClient(), event.getMessage(), parts);
                }
            }
            if (toadd != null) {
                commands.add(toadd);
                toadd = null;
            }
        }
    }

    public void addCommand(Command c) {
        commands.add(c);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public void setToadd(SimpleCustomCmd toadd) {
        this.toadd = toadd;
    }

}
