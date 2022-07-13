public class test3 {


    public static void main(String[] args){

        String banana = "banana";

        String colour = "yellow";

        String toHash = banana + colour;


        int bananaHash = hashCode(toHash);

        System.out.println(bananaHash);

        int bananaCompressed = compressHashCode(bananaHash);

        System.out.println(bananaCompressed);



    }

    public static int hashCode(String keyString){

        int hashCode = 0;

        int n = keyString.length();

        for(int i = 0; i < keyString.length(); i++){

            hashCode += keyString.charAt(i) * 31 ^ (n - (i + 1));

        }

        return hashCode;

    }

    public static int compressHashCode(int hashCode){

        hashCode = hashCode % 11;

        return hashCode;

    }

}
