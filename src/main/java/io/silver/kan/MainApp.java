package io.silver.kan;

/**
 * @author Silver.Kan
 * @date 2018/6/3
 */
public class MainApp {

    public String getSysPath(){
        String path = "target/classes/io/silver/kan/";
        String resource = this.getClass().getResource("").toString();
        int len = resource.length();

        return resource.substring(0,len-path.length());

    }

    public static void main(String[] args) {
        MainApp app = new MainApp();
        System.out.print(app.getSysPath());
    }
}
