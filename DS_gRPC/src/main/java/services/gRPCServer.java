package services;

import database.Database;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class gRPCServer {
    private static final Logger logger = Logger.getLogger(gRPCServer.class.getName());

    public static void main(String[] args) {

        Server server = ServerBuilder.forPort(1234).addService(new service.userService()).build();
        try {
            server.start();
            logger.info("Server started, listening on " + server.getPort());
        } catch (IOException e) {
            logger.severe("Server failed to start");
            e.printStackTrace();
        }
        try {
            server.awaitTermination(120, TimeUnit.SECONDS);
            Database.getInstance().close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
