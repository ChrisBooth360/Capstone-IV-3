// This is a subclass of Person.
public class Contractor extends Person{

    // Only one attribute is declared.
    String type;

    // type is set to "Contractor" in the constructor.
    Contractor(String name, String phone, String email, String address) {
        super(name, phone, email, address);
        this.type = "Contractor";
    }

    // This toString() method overrides the method in the Person superclass.
    @Override
    public String toString(){

        String output = "\n" + type;
        output += "\n" + super.toString();

        return output;

    }
}
