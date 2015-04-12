public interface IInputSource {
    //@author A0134704M
    public boolean hasNextLine();

    //@author A0134704M
    public String getNextLine();
    
    //@author UNKNOWN
    public void addCommand(String s);

    //@author A0134704M
    public void closeSource();
    
}
