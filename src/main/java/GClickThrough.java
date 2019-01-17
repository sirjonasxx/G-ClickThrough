import gearth.extensions.Extension;
import gearth.extensions.ExtensionInfo;
import gearth.extensions.extra.harble.ChatConsole;
import gearth.extensions.extra.harble.HashSupport;
import gearth.protocol.HMessage;

/**
 * Created by Jonas on 17/01/2019.
 */

@ExtensionInfo(
        Title = "G-ClickThrough",
        Description = "Click through other habbo's",
        Version = "1.0",
        Author = "sirjonasxx"
)
public class GClickThrough extends Extension {

    public static void main(String[] args) {
        new GClickThrough(args).run();
    }
    public GClickThrough(String[] args) {
        super(args);
    }

    private HashSupport hashSupport;
    private volatile boolean isStart = false;

    @Override
    protected void initExtension() {
        hashSupport = new HashSupport(this);

        String initmsg =
                "Welcome to G-ClickThrough, a tool you can use to click through people.\n" +
                        "--------------------------------------\n" +
                        "\n" +
                        "* Activate this by typing \":start\", you can also stop it using \":stop\". The effect lasts as long as you stay in the room\n";
        final ChatConsole chatConsole = new ChatConsole(hashSupport, this, initmsg);

        hashSupport.intercept(HMessage.Side.TOCLIENT, "GuideSessionPartnerIsPlaying", new MessageListener() {
            public void act(HMessage hMessage) {
                if (isStart && !hMessage.getPacket().readBoolean()) {
                    hMessage.setBlocked(true);
                }
            }
        });

        chatConsole.onInput(new ChatConsole.ChatInputListener() {
            public void inputEntered(String s) {
                if (s.equals(":start")) {
                    isStart = true;
                    hashSupport.sendToClient("GuideSessionPartnerIsPlaying", true);
                }
                else if (s.equals(":stop")) {
                    isStart = false;
                    hashSupport.sendToClient("GuideSessionPartnerIsPlaying", false);
                }
            }
        });

    }

}
