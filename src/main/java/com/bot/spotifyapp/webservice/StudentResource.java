package com.bot.spotifyapp.webservice;

import com.bot.spotifyapp.util.BotLogger;
import com.bot.spotifyapp.util.BotThreadPoolExecutor;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ThreadPoolExecutor;

@Path("/student")
public class StudentResource {

    private final ThreadPoolExecutor executor = BotThreadPoolExecutor.getInstance();
    private final Logger logger = BotLogger.getInstance().getLogger();
    private static final String STUDENT_RESOURCE = "STUDENT_RESOURCE";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void getStudent(@Suspended final AsyncResponse asyncResponse) {
        executor.execute(() -> {
            logger.log(Level.INFO, () -> STUDENT_RESOURCE + " getStudent API called");
            asyncResponse.resume("1001");
        });
    }

}
