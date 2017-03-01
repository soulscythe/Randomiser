/**
 * Created by Soulscythe on 26/05/2016.
 */
public class WordComponent {

    String component;
    String[] descriptors;

    public WordComponent (String component, String[] descriptors) {
        this.component = component;
        this.descriptors = descriptors;
    }

    public WordComponent (String[] full) {
        this.component = full[0];

        if (full.length > 1) {
            String[] desc = new String[full.length - 1];

            for (int i = 1; i < full.length; i++) {
                desc[i -1] = full[i];
            }
        } else descriptors = new String[0];
    }

}
