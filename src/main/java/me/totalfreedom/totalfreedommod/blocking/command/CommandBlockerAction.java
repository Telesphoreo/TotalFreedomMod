package me.totalfreedom.totalfreedommod.blocking.command;

public enum CommandBlockerAction
{
    BLOCK("b"),
    BLOCK_AND_EJECT("a"),
    BLOCK_UNKNOWN("u");
    private final String token;

    CommandBlockerAction(String token)
    {
        this.token = token;
    }

    public static CommandBlockerAction fromToken(String token)
    {
        for (CommandBlockerAction action : CommandBlockerAction.values())
        {
            if (action.getToken().equalsIgnoreCase(token))
            {
                return action;
            }
        }
        return null;
    }

    public String getToken()
    {
        return this.token;
    }
}
