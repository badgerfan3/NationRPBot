package commands.SlashCommands;

import Sql.SQLFunctions;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

public class SlashChangeVal implements SlashCommandCreateListener {
    String prefix;
    SQLFunctions sqlFunctions;
    boolean waitOnVal = false;
    MessageAuthor author = null;
    Number userID = null;
    String userString = null;


    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        if (event.getSlashCommandInteraction().getCommandName().equals("change")) {
            if(event.getSlashCommandInteraction().getOptionStringValueByName("KEY").isPresent()) {
                var key = event.getSlashCommandInteraction().getOptionStringValueByName("KEY").get();

            }
        }
    }
}
