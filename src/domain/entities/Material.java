package domain.entities;

public class Material extends Component{
    private  int id;
    private double unitCost;
    private double quantity;
    private double transportCost;
    private double qualityCoefficient;


    public Material(String name, String componentType, double vatRate, double unitCost, double quantity, double transportCost, double qualityCoefficient) {
        super(name, componentType, vatRate);
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.transportCost = transportCost;
        this.qualityCoefficient = qualityCoefficient;
    }
    public Material(int id){
        this.id=id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getQualityCoefficient() {
        return qualityCoefficient;
    }

    public void setQualityCoefficient(double qualityCoefficient) {
        this.qualityCoefficient = qualityCoefficient;
    }

    public Material() {
    }

    public double getunitCost() {
        return unitCost;
    }

    public void setunitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(double transportCost) {
        this.transportCost = transportCost;
    }

    public double getqualityCoefficient() {
        return qualityCoefficient;
    }

    public void setqualityCoefficient(double qualityCoefficient) {
        this.qualityCoefficient = qualityCoefficient;
    }

    @Override
    public String toString() {
        return super.toString()+"\n"+
                "Material{" +
                "unitCost=" + unitCost +
                ", quantity=" + quantity +
                ", transportCost=" + transportCost +
                ", qualityCoefficient=" + qualityCoefficient +
                '}';
    }
}
