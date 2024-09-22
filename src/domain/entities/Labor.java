package domain.entities;

public class Labor extends Component{
    private int id;
    private double hourlyRate;
    private double workHours;
    private double workerProductivity;


    public Labor(String name, String componentType, double vatRate,Project project, double hourlyRate, double workHours, double workerProductivity) {
        super(name, componentType, vatRate,project);
        this.hourlyRate = hourlyRate;
        this.workHours = workHours;
        this.workerProductivity = workerProductivity;
    }
    public Labor(int id){
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

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getWorkHours() {
        return workHours;
    }

    public void setWorkHours(double workHours) {
        this.workHours = workHours;
    }

    public Labor() {
    }

    public double gethourlyRate() {
        return hourlyRate;
    }

    public void sethourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getworkHours() {
        return workHours;
    }

    public void setworkHours(double workHours) {
        this.workHours = workHours;
    }

    public double getWorkerProductivity() {
        return workerProductivity;
    }

    public void setWorkerProductivity(double workerProductivity) {
        this.workerProductivity = workerProductivity;
    }

    @Override
    public String toString() {
        return super.toString()+"\n"+
                "Labor{" +
                "id=" + id +
                "hourlyRate=" + hourlyRate +
                ", workHours=" + workHours +
                ", workerProductivity=" + workerProductivity +
                '}';
    }
}
