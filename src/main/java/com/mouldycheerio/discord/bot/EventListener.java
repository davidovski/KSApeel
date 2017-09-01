package com.mouldycheerio.discord.bot;

import java.util.HashMap;
import java.util.Locale;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class EventListener {
    private String prefix;
    private MyBot myBot;
    private CommandController commandController;
    private String ourmention;
    private String ourmention2;
    private ChatterBot bot1;
    private ChatterBotSession bot1session;

    private HashMap<String, ChatterBotSession> sessions;

    public EventListener(String prefix, MyBot myBot) {
        this.prefix = prefix;
        this.myBot = myBot;
        commandController = new CommandController(myBot);
        ChatterBotFactory factory = new ChatterBotFactory();
        sessions = new HashMap<String, ChatterBotSession>();
        try {
            bot1 = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

    @EventSubscriber
    public void onUserJoinEvent(UserJoinEvent event) {
        event.getGuild().getGeneralChannel().sendMessage("Hi, <@" + event.getUser().getStringID() + "> and welcome to " + event.getGuild().getName());
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {

        IUser user = event.getClient().getOurUser();
        Logger.raw("==========");
        Logger.raw(user.getName() + "#" + user.getDiscriminator());
        Logger.raw(user.getStringID());
        Logger.raw("==========");
        ourmention = "<@" + myBot.getClient().getOurUser().getStringID() + ">";
        ourmention2 = "<@!" + myBot.getClient().getOurUser().getStringID() + ">";

    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) throws Exception {
        String content = event.getMessage().getContent();
        if (content.contains(ourmention)) {
            content = content.replace(ourmention, "");
            ai(event.getAuthor(), content, event.getChannel());
        }

        if (myBot.isChatChannel(event.getChannel())) {
            ai(event.getAuthor(), content, event.getChannel());
        }
        if (content.contains(ourmention2)) {
            content = content.replace(ourmention2, "");
            ai(event.getAuthor(), content, event.getChannel());
        }
        if (content.startsWith(prefix)) {
            commandController.onMessageReceivedEvent(event, prefix);
        }
    }

    public void ai(IUser user, String message, IChannel reply) {
        try {
            if (!sessions.containsKey(user.getStringID())) {
                    sessions.put(user.getStringID(), bot1.createSession(Locale.UK));
            }
            ChatterBotSession session = sessions.get(user.getStringID());
            String think = session.think(message);
            reply.sendMessage(user.mention() + " " + think);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public CommandController getCommandController() {
        return commandController;
    }

    public void setCommandController(CommandController commandController) {
        this.commandController = commandController;
    }
}
