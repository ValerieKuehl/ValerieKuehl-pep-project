package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;
import java.util.List;


public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterAccountHandler);
        app.post("/login", this::validateLoginAccountHandler);
        app.post("messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIDHandler);

        return app;
    }

    /**
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */

    private void postRegisterAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount != null){
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void validateLoginAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account extantAccount = accountService.validateLogin(account);
        if(extantAccount != null){
            ctx.json(mapper.writeValueAsString(extantAccount));
            ctx.status(200);
        }else{
            ctx.status(401);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message postedMessage = messageService.addMessage(message);
        if(postedMessage != null){
            ctx.json(mapper.writeValueAsString(postedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    private void getMessageByIDHandler(Context ctx){
        int message_id = Integer.valueOf(ctx.pathParam("message_id")); 
        Message message =  messageService.getMessageByID(message_id);
        if (message == null){
            ctx.json("");
        }
        else{
            ctx.json(message);
        }
        ctx.status(200);
    }

    private void deleteMessageByIDHandler(Context ctx){
        int message_id = Integer.valueOf(ctx.pathParam("message_id")); 
        Message deletedMessage =  messageService.deleteMessageByID(message_id);
        if (deletedMessage == null){
            ctx.json("");
        }
        else{
            ctx.json(deletedMessage);
        }
        ctx.status(200);
    }

     
    private void patchMessageByIDHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.valueOf(ctx.pathParam("message_id")); 
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        String message_text = message.getMessage_text();

        Message postedMessage = messageService.patchMessageByID(message_id, message_text);
        if(postedMessage != null){
            ctx.json(mapper.writeValueAsString(postedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void getAllMessagesByAccountIDHandler(Context ctx){
        int message_id = Integer.valueOf(ctx.pathParam("account_id")); 
        List<Message> messages = messageService.getAllMessagesByAccountID(message_id);
        if (messages == null){
            ctx.json("");
        }
        else{
            ctx.json(messages);
        }
        ctx.status(200);
    }
}