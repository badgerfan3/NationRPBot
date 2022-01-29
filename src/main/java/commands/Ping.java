package commands;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class Ping implements MessageCreateListener {
    String prefix;
    public Ping(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageAuthor().isUser() && event.getMessageContent().equalsIgnoreCase(prefix+"ping")) {
            event.getChannel().sendMessage("Pong!");
        }
    }
}
