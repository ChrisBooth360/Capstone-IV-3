// This is a subclass of Person.
public class Customer extends Person{

    // Only one attribute is declared.
    String type;

    // type is set to "Customer" in the constructor.
    Customer(String name, String phone, String email, String address) {
        super(name, phone, email, address);
        this.type = "Customer";
    }

    // This toString() method overrides the method in the Person superclass.
    @Override
    public String toString(){

        String output = "\n" + type;
        output += "\n" + super.toString();

        return output;

    }
}
