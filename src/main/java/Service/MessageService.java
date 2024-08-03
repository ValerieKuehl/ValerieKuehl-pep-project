package Service;

import java.util.List;
import DAO.MessageDAO;
import DAO.AccountDAO;
import Model.Message;
import Model.Account;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO){
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public Message addMessage(Message message){ //postMessage
        int posted_by = message.getPosted_by();
        String message_text = message.getMessage_text();
        Account extantAccount = accountDAO.getAccountByID(posted_by);

        // Message cant be blank, cant be longer than 255 characters, and the associated account must exist.
        if (message_text != "" && message_text.length() <= 255 && extantAccount != null){
            Message postedMessage = messageDAO.insertMessage(message);
            return postedMessage;
        } 
        else{
            return null;
        }
    }

    public List<Message> getAllMessages(){ //getAllMessages
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int message_id){ //getMessageByID
        return messageDAO.getMessageByID(message_id);
    }

    public Message deleteMessageByID(int message_id){ //deleteMessageByID
        Message extantMessage = messageDAO.getMessageByID(message_id);
        if (extantMessage != null){
            messageDAO.deleteMessageByID(message_id);
            return extantMessage;
        }
        else{
            return null;
        }
    }

    public Message patchMessageByID(int message_id, String message_text){ //patchMessageByID
        Message extantMessage = messageDAO.getMessageByID(message_id);
        if (extantMessage != null && message_text != "" && message_text.length() <= 255){
            messageDAO.patchMessageByID(message_id, message_text);
            return messageDAO.getMessageByID(message_id);
        }
        else{
            return null;
        }    
    }

    public List<Message> getAllMessagesByAccountID(int message_id){ //getAllMessagesByAccountID
        return messageDAO.getAllMessagesByAccountID(message_id);
    }
}
