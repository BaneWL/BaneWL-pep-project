package Service;

import java.util.List;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;
    
    public SocialMediaService(){
        socialMediaDAO = new SocialMediaDAO();
    }

    public Account userRegistration(Account account){
        if (((account.getPassword().length()) < 4) || (account.getUsername() == "")){ // Password shorter than 4 characters, or username blank
            return null;
        }
        // Username field being unique is enforced by database, no need to check on service layer
        return socialMediaDAO.userRegistration(account);
    }

    public Account login(Account account){
        return socialMediaDAO.login(account);
    }

    public Message createNewMessage(Message message){
        if ((message.getMessage_text()) == ""){ // Message text is blank
            return null;
        }
        // Message length is enforced by database, no need to check on service layer
        return socialMediaDAO.createNewMessage(message);
    }

    public List<Message> getAllMessages(){
        return socialMediaDAO.getAllMessages();
    }

    public Message getMessageGivenId(int id){
        return socialMediaDAO.getMessageGivenId(id);
    }

    public Message deleteMessageGivenId(int id){
        return socialMediaDAO.deleteMessageGivenId(id);
    }

    public Message updateMessageGivenId(int id, String newMessageText){
        if (newMessageText == ""){ // Message text is blank
            return null;
        }
        // Message length is enforced by database, no need to check on service layer
        return socialMediaDAO.updateMessageGivenId(id, newMessageText);
    }

    public List<Message> getAllMessagesGivenId(int id){
        return socialMediaDAO.getAllMessagesGivenId(id);
    }
}
