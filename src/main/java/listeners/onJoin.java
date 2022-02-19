package listeners;

import Sql.SQLFunctions;

import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.server.ServerJoinListener;

public class onJoin implements ServerJoinListener {
    SQLFunctions sqlFunctions;
    public onJoin(SQLFunctions sqlFunctions) {
        this.sqlFunctions = sqlFunctions;
    }
    @Override
    public void onServerJoin(ServerJoinEvent event) {
        Server server = event.getServer();

        if(sqlFunctions.setupServer(server.getId(), server.getName())){
            System.out.println("Setup server " + server.getName());
        } else {
            System.out.println("Failed to setup server " + server.getName());
        }
    }
}
