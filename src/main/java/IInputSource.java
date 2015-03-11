public interface IInputSource {
    public boolean hasNextLine();

    public String getNextLine();
    
    public void closeSource();
}
