package com.elearn.fp.command;

import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
    /**
     * Contain map with all available commands.
     */
    private static final Map<String, Command> commands;

    static {
        commands = new HashMap<>();
        commands.put("login", new LoginCommand());
        commands.put("addProduct", new AddProductCommand());
        commands.put("changeStock", new ChangeStockCommand());
        commands.put("deleteItem", new DeleteItemCommand());
        commands.put("changeLanguage", new ChangeLanguageCommand());
        commands.put("addProductToCart", new AddProductToCartCommand());
        commands.put("deleteItemFromOrder", new DeleteItemFromOrderCommand());
        commands.put("deleteWholeOrder", new DeleteWholeOrderCommand());
        commands.put("zReport", new CreateZReportCommand());
        commands.put("xReport", new CreateXReportCommand());
        commands.put("setItemQuantity", new SetItemQuantityCommand());
        commands.put("confirmCheck", new ConfirmCheckCommand());
    }

    private CommandContainer() {
    }

    public static Command getCommand(String commandName) {
        return commands.get(commandName);
    }
}
