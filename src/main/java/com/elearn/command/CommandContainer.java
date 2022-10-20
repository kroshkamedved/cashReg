package com.elearn.command;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {

    private static final Map<String, Command> commands;

    static {
        commands = new HashMap<>();

        commands.put("login", new LoginCommand());
        commands.put("addProduct", new AddProductCommand());
        commands.put("changeStock", new ChangeStockCommand());

        //....
    }


    private CommandContainer() {
    }

    public static Command getCommand(String commandName) {
        return commands.get(commandName);
    }
}
