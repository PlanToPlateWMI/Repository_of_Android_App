package pl.plantoplate.repository.remote.models;

public class Message {

    private String message;

    public Message() {
    }

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(){
        this.message = message;
    }
}
