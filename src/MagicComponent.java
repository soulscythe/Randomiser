/**
 * Created by Soulscythe on 17/05/2016.
 */
public class MagicComponent {

    public String content;
    public String category;

    public MagicComponent (String content, String category) {
        this.content = content;
        this.category = category;
    }

    public String toString() {
        return content;
    }
    public static MagicComponent empty() {
        return new MagicComponent("-","-");
    }
    public MagicComponent duplicate()  {return new MagicComponent(content,category);}
}
