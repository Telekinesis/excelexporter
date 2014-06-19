package excelexporter.ganglia.log;

public interface TaskRecordFilter
{
    public boolean accept(TaskRecord record);
    public void doSomething(TaskRecord record);
}
