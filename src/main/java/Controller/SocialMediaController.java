package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    SocialMediaService socialMediaService;

    public SocialMediaController(){
        this.socialMediaService = new SocialMediaService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::userRegisterHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageGivenIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageGivenIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageGivenIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessageFromUserHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void userRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = socialMediaService.userRegistration(account);
        if(newAccount==null){
            ctx.status(400);
        }
        else {
            ctx.json(mapper.writeValueAsString(newAccount));
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = socialMediaService.login(account);
        if(loginAccount==null){
            ctx.status(401);
        }
        else {
            ctx.json(mapper.writeValueAsString(loginAccount));
        }  
    }   

    private void createNewMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = socialMediaService.createNewMessage(message);
        if(newMessage==null){
            ctx.status(400);
        }
        else {
            ctx.json(mapper.writeValueAsString(newMessage));
        }  
    }    

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messageList = socialMediaService.getAllMessages();
        ctx.json(mapper.writeValueAsString(messageList));
    }
        
    private void getMessageGivenIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = socialMediaService.getMessageGivenId(Integer.parseInt(ctx.pathParam("message_id")));
        if(message != null){
            ctx.json(mapper.writeValueAsString(message));
        }
    }
 
    private void deleteMessageGivenIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = socialMediaService.deleteMessageGivenId(Integer.parseInt(ctx.pathParam("message_id")));
        if(message != null){
            ctx.json(mapper.writeValueAsString(message));
        }
        else{
            ctx.json(mapper.writeValueAsString(new Message()));
        }
    }
   
    private void updateMessageGivenIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        //Message message = mapper.readValue(ctx.body(), Message.class);
        String newMessageText = mapper.readValue(ctx.body(), String.class);
        //Message newMessage = socialMediaService.updateMessageGivenId(Integer.parseInt(ctx.pathParam("message_id")), message);
        Message newMessage = socialMediaService.updateMessageGivenId(Integer.parseInt(ctx.pathParam("message_id")), newMessageText);
        if(newMessage==null){
            ctx.status(400);
        }
        else {
            ctx.json(mapper.writeValueAsString(newMessage));
        }   
    }
        
    private void getAllMessageFromUserHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messageList = socialMediaService.getAllMessagesGivenId(Integer.parseInt(ctx.pathParam("account_id")));
        ctx.json(mapper.writeValueAsString(messageList));
    }
}