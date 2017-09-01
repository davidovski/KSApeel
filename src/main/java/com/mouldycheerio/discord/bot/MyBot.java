package com.mouldycheerio.discord.bot;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mouldycheerio.discord.bot.commands.Command;
import com.mouldycheerio.discord.bot.commands.CommandDescription;
import com.mouldycheerio.discord.bot.commands.SimpleCustomCmd;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IChannel;

public class MyBot {
    private EventListener eventListener;
    private IDiscordClient client;
    public EventDispatcher dispatcher;

    private long uptime = 0;
    private long creation;

    private Random random;

    private JSONObject admins;
    private String playingText = "a game";
    private long playingtextindex = 0;
    private BotStatus status = BotStatus.INACTIVE;

    private JSONArray chatchannels;

    private String prefix;


    public MyBot(String token, String prefix) throws Exception {
        this.prefix = prefix;
        // ChatterBotFactory chatterBotFactory = new ChatterBotFactory();
        // chatterBot = chatterBotFactory.create(ChatterBotType.CLEVERBOT);
        // chatsession = chatterBot.createSession(Locale.ENGLISH);

        random = new Random();

        admins = new JSONObject();
        chatchannels = new JSONArray();
        status = BotStatus.ACTIVE;

        eventListener = new EventListener(prefix, this);

        creation = System.currentTimeMillis();
        client = ClientFactory.createClient(token, true);
        dispatcher = client.getDispatcher();
        dispatcher.registerListener(eventListener);

        loadAll();

    }

    public boolean isChatChannel(IChannel c) {
        for (int i = 0; i < chatchannels.length(); i++) {
            if (chatchannels.getString(i).equals(c.getStringID())) {
                return true;
            }
        }
        return false;
    }

    public void loadAll() {
        try {

            JSONTokener parser = new JSONTokener(new FileReader("Bot.opf"));

            JSONObject obj = (JSONObject) parser.nextValue();
            if (obj.has("admins")) {
                admins = obj.getJSONObject("admins");
            }

            if (obj.has("playing")) {
                playingText = obj.getString("playing");
            }

            if (obj.has("channels")) {
                chatchannels = obj.getJSONArray("channels");
            }

            if (obj.has("commands")) {
                JSONArray b = obj.getJSONArray("commands");

                for (int i = 0; i < b.length(); i++) {
                    JSONObject cmd = b.getJSONObject(i);
                    String cmdname = cmd.getString("command");
                    String cmddesc = cmd.getString("description");
                    String cmdtext = cmd.getString("text");

                    boolean doit = true;
                    CommandDescription desc = new CommandDescription(cmdname, cmddesc, cmdname);
                    for (Command command : eventListener.getCommandController().getCommands()) {
                        if (command.getName().equals(cmdname)) {
                            doit = false;
                        }
                    }
                    if (doit) {
                        SimpleCustomCmd scc = new SimpleCustomCmd(cmdname, desc, cmdtext);
                        scc.setShowInHelp(false);
                        eventListener.getCommandController().getCommands().add(scc);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveAll() {
        JSONObject obj = new JSONObject();
        obj.put("admins", admins);
        obj.put("playing", playingText);
        obj.put("channels", chatchannels);

        JSONArray array = new JSONArray();
        for (Command command : getEventListener().getCommandController().getCommands()) {
            if (command instanceof SimpleCustomCmd) {
                JSONObject cmd = new JSONObject();
                cmd.put("command", command.getName());
                cmd.put("description", command.getDescription().getText());
                cmd.put("text", ((SimpleCustomCmd) command).getText());
                array.put(cmd);
            }
        }
        obj.put("commands", array);

        try {
            FileWriter file = new FileWriter("Bot.opf");
            file.write(obj.toString(1));
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void loop(long alpha) throws InterruptedException {
        uptime = alpha;
        updatePlaying(alpha);

    }

    private void updatePlaying(long alpha) {
        if (alpha % 400 == 0 && client.isReady()) {

            playingtextindex++;
            if (playingtextindex == 1) {
                client.changePlayingText(playingText);
            } else if (playingtextindex == 2) {
                client.changePlayingText(prefix + "help");
            } else if (playingtextindex == 3) {
                client.changePlayingText("on " + client.getGuilds().size() + " servers!");
            } else {
                playingtextindex = 0;
            }

        }
    }

    public IDiscordClient getClient() {
        return client;
    }

    public void setClient(IDiscordClient client) {
        this.client = client;
    }

    public long getUptime() {
        return System.currentTimeMillis() - creation;
    }

    public JSONObject getAdmins() {
        return admins;
    }

    public void setAdmins(JSONObject admins) {
        this.admins = admins;
    }

    public String getPlayingText() {
        return playingText;
    }

    public void setPlayingText(String playingText) {
        client.changePlayingText(playingText);
        this.playingText = playingText;
    }

    public BotStatus getStatus() {
        return status;
    }

    public void setStatus(BotStatus status) {
        this.status = status;
    }

    public EventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public JSONArray getChatchannels() {
        return chatchannels;
    }

    public void setChatchannels(JSONArray chatchannels) {
        this.chatchannels = chatchannels;
    }
}
