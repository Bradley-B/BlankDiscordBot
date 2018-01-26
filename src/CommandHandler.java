import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import java.util.*;

public class CommandHandler {

    // A static map of commands mapping from command string to the functional impl
    private static Map<String, Command> commandMap = new HashMap<>();
    
    // Statically populate the commandMap with the intended functionality
    public CommandHandler() {

    	//a test command to ping the bot
        commandMap.put("ping", (event, args) -> {
        	BotUtils.sendMessage(event.getChannel(), "I'm here! Don't worry! I can do this! Just hold on!");
        });
        
        commandMap.put("info", (event, args) -> {
        	BotUtils.sendMessage(event.getChannel(), "Don't worry, I fly the ship. Just follow my instructions and we'll all be okay.");
        });
         
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){

        // Note for error handling, you'll probably want to log failed commands with a logger or sout
        // In most cases it's not advised to annoy the user with a reply incase they didn't intend to trigger a
        // command anyway, such as a user typing ?notacommand, the bot should not say "notacommand" doesn't exist in
        // most situations. It's partially good practice and partially developer preference

        // Given a message "/test arg1 arg2", argArray will contain ["/test", "arg1", "arg"]
        String[] argArray = event.getMessage().getContent().split(" ");
        
        // First ensure at least the invocation and command are present
        if(argArray.length < 2)
            return;
        
        // Check if the first arg (the bot invocation) starts with the prefix and name defined in the utils class
        if(!argArray[0].startsWith(BotUtils.BOT_PREFIX+BotUtils.BOT_NAME))
            return;
        
        //get the command
        String commandStr = argArray[1];
        
        // Load the rest of the args in the array into a List for safer access
        List<String> argsList = new ArrayList<>(Arrays.asList(argArray));
        argsList.remove(0); // Remove the invocation
        
        // Instead of delegating the work to a switch, automatically do it via calling the mapping if it exists
        if(commandMap.containsKey(commandStr)) {
        	Command command = commandMap.get(commandStr);
        	command.runCommand(event, argsList);
        }

    }
}