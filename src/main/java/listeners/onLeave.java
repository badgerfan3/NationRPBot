package listeners;

import Sql.SQLFunctions;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerLeaveEvent;
import org.javacord.api.listener.server.ServerLeaveListener;

public class onLeave implements ServerLeaveListener {
    SQLFunctions sqlFunctions;

    public onLeave(SQLFunctions sqlFunctions){
        this.sqlFunctions = sqlFunctions;
    }
    @Override
    public void onServerLeave(ServerLeaveEvent event) {
        Server server = event.getServer();

        if(sqlFunctions.deleteServer(server.getId())) {
            System.out.println("Left server: " + server.getName() + " successfully");
        } else {
            System.out.println("Tried to leave server: " + server.getName() + " unsuccessfully");
        }
    }
}
