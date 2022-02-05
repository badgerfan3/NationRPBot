package commands;

import Sql.SQLFunctions;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.Scanner;

public class ChangeVal implements MessageCreateListener {
    String prefix;
    SQLFunctions sqlFunctions;
    boolean waitOnVal = false;
    MessageAuthor author = null;
    String key = null;
    Number userID = null;
    String userString = null;


    public ChangeVal(String prefix, SQLFunctions sqlFunctions){
        this.prefix = prefix;
        this.sqlFunctions = sqlFunctions;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        Scanner scanner = new Scanner(event.getMessageContent());
        String arg = null;

        if(event.getMessageAuthor().isUser()){
            if(scanner.hasNext()){
                arg = scanner.next();
            }
        }

        if(!waitOnVal && arg != null && arg.equalsIgnoreCase(prefix+"change") && scanner.hasNext()){
            userString = scanner.next();

            if (userString.length() == 22 && userString.substring(0, 3).equalsIgnoreCase("<@!") && scanner.hasNext()) {
                String key = scanner.next().toUpperCase();
                userString = userString.substring(3,21);
                if (key.equals("ID") || key.equals("NATION_NAME") || key.equals("FLAG") || key.equals("POP_SCORE") || key.equals("POP") || key.equals("ECONOMY_SCORE") || key.equals("STABILITY")){
                    {
                        author = event.getMessageAuthor();
                        userID = Long.parseLong(userString);
                        this.key = key;
                        waitOnVal = true;
                        System.out.println("Changing value");
                        event.getChannel().sendMessage("Okay " + author.getDisplayName() + "we are changing the " + key + " of " + userString);
                        event.getChannel().sendMessage("Type " + prefix + "change [VALUE] to complete the change");
                        event.getChannel().sendMessage("Example: " + prefix + "change 0");
                    }
                }
            }
        } else if(waitOnVal && arg != null && arg.equalsIgnoreCase(prefix+"change") && scanner.hasNext() && author.getId() == event.getMessageAuthor().getId()){
            waitOnVal = false;
            StringBuilder val = new StringBuilder();
            if(key.equals("NATION_NAME")){
                while(scanner.hasNext()){
                    val.append(scanner.next()).append(" ");
                }
            } else {
                val = new StringBuilder(scanner.next());
            }
            sqlFunctions.changeStat(key, userID, val.toString());
            event.getChannel().sendMessage("Changed <@!"+userID+">'s " + key + " to " + val);
            System.out.println("Changed " + userID + " val in table");
            key = null;
            userID = null;
            userString =null;
            author = null;
        }
    }
}
