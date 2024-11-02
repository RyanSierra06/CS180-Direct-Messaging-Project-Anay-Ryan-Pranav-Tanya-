public interface ApplicationInterface {
    synchronized void actionsAfterLogin(User currentUser);
    User createUserMain();
}