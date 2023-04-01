package Client;

public class Main {
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.startClient();

        client.sendTestData("My name is Duy");
        try {
            client.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}