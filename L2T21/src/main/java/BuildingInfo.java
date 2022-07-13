public class BuildingInfo {

    String buildingType;
    String address;
    int erfNumber;

    BuildingInfo(String buildingType, String address, int erfNumber){

        this.buildingType = buildingType;
        this.address = address;
        this.erfNumber = erfNumber;

    }

    public String getBuildingType(){
        return buildingType;
    }

    public String getAddress(){
        return address;
    }

    public int getErfNumber(){
        return erfNumber;
    }

}
