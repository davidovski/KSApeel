package com.mouldycheerio.discord.bot.commands;

public abstract class AdminCommand extends BotCommand {
    private int commandlvl = 0;
    private String noPermText = "You can't do that! *(yet)*";


    public int getCommandlvl() {
        return commandlvl;
    }

    public void setCommandlvl(int commandlvl) {
        this.commandlvl = commandlvl;
    }

    public String getNoPermText() {
        return noPermText;
    }

    public void setNoPermText(String noPermText) {
        this.noPermText = noPermText;
    }

}
