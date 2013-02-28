/**
 * User: David
 * Date: 2/25/13
 * Time: 11:39 PM
 */
public class NodeData {
    private User userWhoAnswered;
    private String question;

    private String celebrity;

    private static final int CELEBRITY_NAME_SIZE = 40;

    private static final int QUESTION_SIZE       = 100;
    public static final int RECORD_SIZE          = CELEBRITY_NAME_SIZE + QUESTION_SIZE + User.RECORD_SIZE;
    public NodeData(User userWhoAnswered, String question, String celebrity) {
        this.userWhoAnswered = userWhoAnswered;
        this.celebrity = celebrity;
        this.question = question;
    }

    public String getCelebrity() {
        return celebrity;
    }

    public void setCelebrity(String celebrity) {
        this.celebrity = celebrity;
    }

    public User getUserWhoAnswered() {
        return userWhoAnswered;
    }

    public void setUserWhoAnswered(User userWhoAnswered) {
        this.userWhoAnswered = userWhoAnswered;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public byte[] getBytes() {
        byte[] result = new byte[RECORD_SIZE];
        byte[] userWhoAnsweredBytes = new byte[User.RECORD_SIZE];
        byte[] questionBytes = new byte[QUESTION_SIZE];
        byte[] celebrityBytes = new byte[CELEBRITY_NAME_SIZE];

        if(userWhoAnswered != null) System.arraycopy(userWhoAnswered.getBytes(),0,userWhoAnsweredBytes,0,userWhoAnswered.getBytes().length);
        if(question != null) System.arraycopy(question.toString().getBytes(),0,questionBytes,0,question.toString().getBytes().length);
        if(celebrity != null) System.arraycopy(celebrity.toString().getBytes(),0,celebrityBytes,0,celebrity.toString().getBytes().length);
        System.arraycopy(userWhoAnsweredBytes,0,result,0,userWhoAnsweredBytes.length);
        System.arraycopy(questionBytes,0,result,User.RECORD_SIZE,questionBytes.length);
        System.arraycopy(celebrityBytes,0,result,User.RECORD_SIZE+QUESTION_SIZE,celebrityBytes.length);

        return result;
    }
}
