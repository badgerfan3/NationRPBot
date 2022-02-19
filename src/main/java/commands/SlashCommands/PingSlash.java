package commands.SlashCommands;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

public class PingSlash implements SlashCommandCreateListener {


    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        if(event.getSlashCommandInteraction().getCommandName().equals("ping")){
            event.getSlashCommandInteraction()
                    .createFollowupMessageBuilder()
                    .setContent("Pong!")
                    .send();
        }
    }
}
