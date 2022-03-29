package services;

import com.demo.grpc.User;
import com.demo.grpc.userGrpc;
import database.Database;
import hash.Hash;
import io.grpc.stub.StreamObserver;
import java.sql.SQLException;
import java.util.logging.Logger;

public class userService extends userGrpc.userImplBase{
    private static final Logger logger= Logger.getLogger(userService.class.getName());

    @Override
    public void logIn(User.LogInRequest request, StreamObserver<User.LogInResponse> responseObserver) {
        String email=request.getEmail();
        String password=request.getPassword();
        Database database =  Database.getInstance();
        User.LogInResponse.Builder response=User.LogInResponse.newBuilder();
        if(database.check(Hash.getHashed(email),Hash.getHashed(password))){

            response.setMessage("Log in successfully");
            response.setCode("200");
            response.setToken("Token");
            logger.info("Log in successfully");

        }
        else{
            response.setMessage("Log in failed");
            response.setCode("400");
            response.setToken("");
            logger.info("Log in failed");
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void signUp(User.SignUpRequest request, StreamObserver<User.SignUpResponse> responseObserver) {
        String email=request.getEmail();
        String password=request.getPassword();
        Database database =  Database.getInstance();
        User.SignUpResponse.Builder response=User.SignUpResponse.newBuilder();
        try{
            database.insertUser(Hash.getHashed(email),Hash.getHashed(password));
            response.setMessage("Sign up successfully");
            response.setCode("200");
            response.setToken("Token");
            logger.info("Sign up successfully");

        } catch (SQLException e) {
            response.setMessage("Sign up failed");
            response.setCode("400");
            response.setToken("");
            logger.info("Sign up failed");
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
