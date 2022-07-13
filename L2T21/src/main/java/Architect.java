// This is a subclass of Person.
public class Architect extends Person{

    // Only one attribute is declared.
    String type;

    // type is set to "Architect" in the constructor.
    Architect(String name, String phone, String email, String address) {
        super(name, phone, email, address);
        this.type = "Architect";
    }

    // This toString() method overrides the method in the Person superclass.
    @Override
    public String toString(){

        String output = "\n\n" + type;
        output += "\n" + super.toString();

        return output;

    }
}
